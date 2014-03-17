package com.noxpvp.core.gui;

import java.util.Arrays;
import java.util.Set;

import me.confuser.barapi.BarAPI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.google.common.collect.Sets;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.data.ObjectLock;
import com.noxpvp.core.utils.ColoredStringScroller;
import com.noxpvp.core.utils.ShiningStringScroller;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;

public class CoreBar{
	
	private final Entry currentEntry = new Entry();
	
	public final Player p;
	private ObjectLock lock;
	private Runnable updater;
	
	private NoxCore plugin;
	
	public String separater;
	public String color;
	
	/**
	 * 
	 * @param p - The player to give this bar to
	 */
	public CoreBar(NoxCore core, Player p){
		this.p = p;
		
		this.plugin = NoxCore.getInstance();
		
		String name = p.getName();
		
		lock = new ObjectLock(null);
		updater = null;
		
		this.separater = core.getCoreConfig().get("gui.corebar.separater", String.class, "||");
		this.color = core.getCoreConfig().get("gui.corebar.default-color", String.class, ChatColor.GREEN.toString());
		
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
		private Set<Material> transparents;
		
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
			this.transparents = Sets.newHashSet(Material.AIR, Material.WATER, Material.LAVA);
			
			this.text = new StringBuilder(color).append(text).append(separater).append((stringDist = String.format("%0$.1f", distance)));
			
			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(e, text.toString());
			this.runTaskTimer(plugin, 0, runPeriod);
		}
		
		public void run() {

			if (e == null  || p == null || lock == null || !lock.lock.equals(e.getUniqueId()) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || !e.isValid() || !p.isValid()){
				safeCancel();
				return;
			}
			
			distance = p.getLocation().distance(e.getLocation());
			
			if (distance > maxDistance || (!ignoreLOS && !LineOfSightUtil.hasLineOfSight(p, e.getLocation(), transparents))){
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
		private final static int runPeriod = 3;
		
		private ObjectLock locked;
		private int v;

		private int displayTicks;
		private int runs;
		
		private ColoredStringScroller text;
		
		public Scroller(String text, int visibleLength, int displayTicks, boolean canBeOverridden){
			lock = (this.locked = new ObjectLock(text, canBeOverridden));
			updater = this;
			
			{
				int left = (62 - text.length());
				StringBuilder addon = new StringBuilder();
				
				while (addon.length() < left)
					addon.append(' ');
				
				text = (addon.toString() + ChatColor.RESET) + text;
			}
			
			if (visibleLength > 64)
				visibleLength = 64;
			this.v = ((visibleLength > text.length())? text.length() : visibleLength);
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
		private final static int runPeriod = 1;
		
		private ObjectLock locker;
		private boolean canBeOveridden;
		private int delay;
		
		private ShiningStringScroller text;
		
		private int displayTicks;
		private int runs;
		
		public Shine(String text, int delay, int displayTicks, boolean canBeOverridden){
			lock = (this.locker = new ObjectLock(text, canBeOverridden));
			updater = this;
			this.canBeOveridden = canBeOverridden;
			this.delay = delay;
			
			this.text = new ShiningStringScroller(text);
			
			this.displayTicks = canBeOverridden? displayTicks : displayTicks <= 0? 500 : displayTicks;
			this.runs = 0;
			
			this.runTaskTimer(plugin, delay, runPeriod);
		}
		
		public void run() {
			if (lock != locker || (displayTicks > 0 && ((runs * runPeriod) > displayTicks)) || p == null || !p.isOnline())
			{
				safeCancel();
				return;
			}

			currentEntry.update(100F, text.shine());			
			runs++;
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
