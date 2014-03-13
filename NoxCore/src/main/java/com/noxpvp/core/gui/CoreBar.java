package com.noxpvp.core.gui;

import java.util.Arrays;
import java.util.Set;

import me.confuser.barapi.BarAPI;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.Sets;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.data.ObjectLock;
import com.noxpvp.core.data.ColoredStringScroller;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;

public class CoreBar{
	
	private final Entry currentEntry = new Entry();
	
	public final Player p;
	private ObjectLock lock;
	private Runnable updater;
	
	private NoxCore plugin;
	private PlayerManager pm;
	
	private String separater;
	private String color;
	
	/**
	 * 
	 * @param p - The player to give this bar to
	 */
	public CoreBar(NoxCore core, Player p){
		this.p = p;
		
		this.plugin = NoxCore.getInstance();
		this.pm = PlayerManager.getInstance();
		
		String name = p.getName();
		if (pm.hasCoreBar(name)){
				pm.removeCoreBar(name);
		}
		
		lock = new ObjectLock(null);
		updater = null;
		
		this.separater = core.getCoreConfig().get("gui.corebar.separater", String.class, ChatColor.GREEN + "    ||    " + ChatColor.RESET);
		this.color = core.getCoreConfig().get("gui.corebar.default-color", String.class, ChatColor.GREEN.toString());
		
		pm.addCoreBar(this);
	}
	
	private boolean isChangeable(){
		return (lock == null || lock.canUnlock);
	}
	
	
	
	public void newScroller(String text, int length, int displayTicks, boolean canBeOverridden) {
		if (isChangeable())
			new Scroller(text, length, displayTicks, canBeOverridden);	
	}
	
	public void newShine(String text, int delay, int displayTicks, boolean canBeOverridden) {
		if (isChangeable())
			new Shine(text, delay, displayTicks, canBeOverridden);
	}
	
	class Entry{
		float percentFilled;
		String text;
		
		public Entry(){
			this.text = "~x~";
			this.percentFilled = 100F;
		}
		
		public void update(float percentFilled, String text){
			this.percentFilled = percentFilled;
			this.text = text;
			
			BarAPI.setMessage(p, text, percentFilled);
			
		}
		
		public void update(LivingEntity health, String text){
			update((float) (health.getHealth() / health.getMaxHealth()) * 100, text);
		}
		
		public void update(String text){
			this.update(percentFilled, text);
			
		}
		
		public void update(float percentFilled){
			update(percentFilled, text);
		}
		
		public void update(LivingEntity health){
			update(health, text);
		}
		
		public void hide(){
			BarAPI.removeBar(p);
		}
		
	}
	
	public void newFlasher(String text, int displayTicks) {
		newFlasher(text, displayTicks, true);
	}
	
	public void newFlasher(String text, int displayTicks, boolean canBeOverridden){
		if (isChangeable())
			new Flasher(text, displayTicks, canBeOverridden);
	}

	private class Flasher extends BukkitRunnable {
		private final static int runPeriod = 2;
		
		private ObjectLock locker;
		
		private Cycler<Character> colors;
		private StringBuilder text;
		private String newInsert;

		private int displayTicks;
		private int runs;
		
		public Flasher(String text, int displayTicks, boolean canBeOverridden){
			lock = (locker = new ObjectLock(text, canBeOverridden));
			updater = this;
			
			colors = new Cycler<Character>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));
			this.text = new StringBuilder(text);
			
			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(100F, text);
			this.runTaskTimer(plugin, 0, runPeriod);
		}
		
		public void run() {
			if (lock != locker || (displayTicks != 0 && ((runs * runPeriod) > displayTicks)) || p == null || !p.isOnline())
			{
				safeCancel();
				return;
			}
			
			if (runs++ > 0)
				text.delete(0, newInsert.length());
			
			newInsert = ChatColor.COLOR_CHAR + colors.next().toString();
			text.insert(0, newInsert);
			
			currentEntry.update(100F, text.toString());
			
		}
		
		public void safeCancel() {
			try {
				lock = null;
				updater = null;
				currentEntry.hide();
				cancel();
			} catch (IllegalStateException e) {}
			
		}
		
	}
	
	public void newLivingTracker(LivingEntity e, String text, boolean ignoreLOS){
		newLivingTracker(e, text, ignoreLOS, 500, true);
	}
	
	public void newLivingTracker(LivingEntity e, String text, boolean ignoreLOS, int displayTicks){
		newLivingTracker(e, text, ignoreLOS, displayTicks, true);
	}
	
	public void newLivingTracker(LivingEntity e, String text, boolean ignoreLOS, int displayTicks, boolean canBeOveridden){
		newLivingTracker(e, text, NoxCore.getInstance().getCoreConfig().get("gui.corebar.default-distance", int.class, 25), ignoreLOS, displayTicks, canBeOveridden);
	}
	
	public void newLivingTracker(LivingEntity e, String text, int maxDistance, boolean ignoreLOS, int displayTicks, boolean canBeOverridden) {
		if (lock == null || (lock.lock != e.getUniqueId() && lock.canUnlock)){
			new LivingTracker(e, text, maxDistance, ignoreLOS, displayTicks, canBeOverridden);
		}
		else if (updater != null)
			CommonUtil.nextTick(updater);
	}

	private class LivingTracker extends BukkitRunnable{
		private final static int runPeriod = 10;

		private LivingEntity e;
		private double distance;
		private int maxDistance;
		
		private boolean ignoreLOS;
		private Set<Byte> transparents;
		
		private StringBuilder text;		
		private String stringDist;

		private int displayTicks;
		private int runs;		
		
		public LivingTracker(LivingEntity e, String text, int maxDistance, boolean ignoreLOS, int displayTicks, boolean canBeOverridden) {
			lock = new ObjectLock(e.getUniqueId(), canBeOverridden);
			updater = this;
			
			this.e = e;
			this.distance = p.getLocation().distance(e.getLocation());
			this.maxDistance = maxDistance;
			
			this.ignoreLOS = ignoreLOS;
			this.transparents = Sets.newHashSet((byte) 0, (byte) 8, (byte) 9, (byte) 10, (byte) 11);
			
			this.text = new StringBuilder(color).append(text).append(separater).append((stringDist = String.format("%0$.1f", distance)));
			
			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(e, text.toString());
			this.runTaskTimer(plugin, 0, runPeriod);
		}
		
		public void run() {
			distance = p.getLocation().distance(e.getLocation());

			if (!lock.lock.equals(e.getUniqueId()) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || distance > maxDistance || e == null || p == null || !e.isValid() || !p.isValid()){
				safeCancel();
				return;
			}
			
			if (!ignoreLOS && !LineOfSightUtil.hasLineOfSight(p, e.getLocation(), transparents)){
				safeCancel();
				return;
			}
			
			int tLength = text.length();
			
			text.replace(tLength - (separater.length() + stringDist.length()), tLength, separater + (stringDist = String.format("%0$.1f", distance)));
			
			currentEntry.update(e, text.toString());
			
		}
		
		public void safeCancel() {try {
			lock = null;
			updater = null;
			currentEntry.hide();
			cancel();
		} catch (IllegalStateException e) {}}
		
	}
	
	private class Scroller extends BukkitRunnable{
		private final static int runPeriod = 5;
		
		private ObjectLock locked;
		private int v;

		private int displayTicks;
		private int runs;
		
		private ColoredStringScroller text;
		
		public Scroller(String text, int visibleLength, int displayTicks, boolean canBeOverridden){
			lock = (this.locked = new ObjectLock(text, canBeOverridden));
			updater = this;
			
			text = text.concat(ChatColor.GREEN + "     ||     " + ChatColor.RESET);
			
			this.v = (visibleLength <= 64 && visibleLength <= text.length())? visibleLength : ((visibleLength > text.length())? text.length() : visibleLength);
			this.text = new ColoredStringScroller(text);

			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(100F, text.substring(0, v));
			
			this.runTaskTimer(plugin, 0, runPeriod);
		}
		public void run() {
			if (lock != locked || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || p == null || !p.isOnline()){
				safeCancel();
				return;
			}
			
			currentEntry.update(100F, text.scroll().substring(0, v));
			
		}
		
		public void safeCancel() {try {
			lock = null;
			updater = null;
			currentEntry.hide();
			cancel();
		} catch (IllegalStateException e) {}} 	
		
	}

	private class Shine extends BukkitRunnable{
		private final static int runPeriod = 2;
		
		private ObjectLock locker;
		private boolean canBeOveridden;
		private int delay;
		
		private StringBuilder text;
		private String regexColors;
		private int curIdx;
		private int[] taken;
		private String[] colors;
		
		private int displayTicks;
		private int runs;
		
		public Shine(String text, int delay, int displayTicks, boolean canBeOverridden){
			lock = (this.locker = new ObjectLock(text, canBeOverridden));
			updater = this;
			this.canBeOveridden = canBeOverridden;
			this.delay = delay;
			
			this.text = new StringBuilder(text);
			this.regexColors = "[&" + ChatColor.COLOR_CHAR + "][0-9a-frkmlo]";
			this.curIdx = 0;
			this.taken = new int[4];
			this.colors = new String[4];
			this.colors[0] = ChatColor.GOLD.toString();
			this.colors[1] = ChatColor.YELLOW.toString();
			this.colors[2] = ChatColor.RED.toString();
			this.colors[3] = ChatColor.RESET.toString();
			
			this.displayTicks = canBeOverridden? displayTicks : displayTicks <= 0? 500 : displayTicks;
			this.runs = 0;
			
			currentEntry.update(100F, text);
			
			this.runTaskTimer(plugin, 0, runPeriod);
		}
		
		public void run() {
			if (lock != locker || (displayTicks > 0 && ((runs * runPeriod) > displayTicks)) || (text.length() <= 7) || p == null || !p.isOnline())
			{
				safeCancel();
				return;
			}
			
			if (runs > 0){
				
				curIdx = runs;
				if (curIdx > text.length()){
					safeCancelWithNew();
					return;
				}
				
				int n = text.length();
				for (int i : taken) {
					if (i < 0)
						continue;
					if (text.length() < n)
						i = i - (n - text.length());
					
					text.delete(i, i + 2);
				}
				taken = new int[4];
				
			}
			
			int i = 0;
			for (String curColor : colors){
				if (curIdx + 2 <= text.length()){
					while (text.substring(curIdx, curIdx + 2).matches(regexColors)){
						colors[3] = text.substring(curIdx, curIdx + 2);
						curIdx = curIdx + 2;
					}
						
					text.insert(curIdx, curColor);
					taken[i++] = curIdx;
					curIdx = curIdx + 3;
					
				} else
					taken[i++] = -1;
			}
			
			currentEntry.update(100F, text.toString());			
			runs++;
		}
		
		public void safeCancelWithNew() {
			try {
				locker = null;
				updater = null;
				cancel();
				
				int newDisplayTicks = (displayTicks != 0? (displayTicks - (runs * runPeriod)) : 0);
				new Shine(this.text.toString(), this.delay, newDisplayTicks, this.canBeOveridden).runTaskLater(plugin, this.delay);

			} catch (IllegalStateException e) {}	
		}
		
		public void safeCancel() {
			try {
				locker = null;
				currentEntry.hide();
				cancel();
			} catch (IllegalStateException e) {}
		}
	}
	
}
