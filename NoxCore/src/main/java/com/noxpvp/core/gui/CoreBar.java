package com.noxpvp.core.gui;

import java.util.Arrays;

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
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;

public class CoreBar{
	
	private final Entry currentEntry = new Entry();
	public final Player p;
	private ObjectLock lock;
	private Runnable updater;
	PlayerManager pm;
	
	/**
	 * 
	 * @param p - The player to give this bar to
	 */
	public CoreBar(NoxCore core, Player p){
		this.p = p;
		this.pm = PlayerManager.getInstance();
		
		String name = p.getName();
		if (pm.hasCoreBar(name)){
				pm.removeCoreBar(name);
		}
		
		lock = new ObjectLock(null);
		updater = null;
		
		pm.addCoreBar(this);
	}
	
	public void newFlasher(String text, int displayTicks) {
		if (lock.lock == null || (lock.lock != text && lock.canUnlock))
			newFlasher(text, displayTicks, true);
	}
	
	public void newFlasher(String text, int displayTicks, boolean canBeOverridden){
		new Flasher(text, displayTicks, canBeOverridden);
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
	
	public void newScroller(String text, int length, ChatColor color, int displayTicks, boolean canBeOverridden) {
		if (lock == null || (lock.lock != text && lock.canUnlock))
			this.new Scroller(text, length, color, displayTicks, canBeOverridden);
	}
	
	public void newShine(String text, int delay, int displayTicks, boolean canBeOverridden) {
		if (lock == null || (lock.lock != text && lock.canUnlock))
			this.new Shine(text, delay, displayTicks, canBeOverridden);
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
	

	private class Flasher extends BukkitRunnable {
		private final static int runPeriod = 2;
		
		private Cycler<Character> colors;
		private StringBuilder text;
		private String newInsert;

		private int displayTicks;
		private int runs;
		
		public Flasher(String text, int displayTicks, boolean canBeOverridden){
			lock = new ObjectLock(text, canBeOverridden);
			updater = this;
			
			colors = new Cycler<Character>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));
			this.text = new StringBuilder(text);
			
			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(100F, text);
			this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
		}
		
		public void run() {
			if (!currentEntry.text.equals(text.toString()) || (displayTicks != 0 && ((runs * runPeriod) > displayTicks)) || p == null || !p.isOnline())
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
			} catch (IllegalStateException e) {}} 	
		
	}
	

	private class LivingTracker extends BukkitRunnable{
		private final static int runPeriod = 10;

		private LivingEntity e;
		private double distance;
		private int maxDistance;
		private boolean ignoreLOS;

		private String color;
		private String separator;
		private StringBuilder text;		
		private String stringDist;

		private int displayTicks;
		private int runs;		
		
		public LivingTracker(LivingEntity e, String text, int maxDistance, boolean ignoreLOS, int displayTicks, boolean canBeOverridden) {
			lock = new ObjectLock(e.getUniqueId(), canBeOverridden);
			updater = this;
			
			NoxCore core = NoxCore.getInstance();
			
			this.e = e;
			
			this.color = core.getCoreConfig().get("gui.corebar.color", String.class, "&a");
			this.separator = color + core.getCoreConfig().get("gui.corebar.separater", String.class, " - ");;
			this.distance = p.getLocation().distance(e.getLocation());
			this.maxDistance = maxDistance;
			this.ignoreLOS = ignoreLOS;
			this.text = new StringBuilder(color).append(text).append(separator).append((stringDist = String.format("%0$.1f", distance)));
			
			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(e, text.toString());
			this.runTaskTimer(core, 0, runPeriod);
		}
		
		public void run() {
			distance = p.getLocation().distance(e.getLocation());

			if (!lock.lock.equals(e.getUniqueId()) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || distance > maxDistance || e == null || p == null || !e.isValid() || !p.isValid()){
				safeCancel();
				return;
			}
			
			if (!ignoreLOS && !LineOfSightUtil.hasLineOfSight(p, e.getLocation(), Sets.newHashSet((byte) 0, (byte) 8, (byte) 9, (byte) 10, (byte) 11))){
				safeCancel();
				return;
			}
			
			int tLength = text.length();
			
			text.replace(tLength - (separator.length() + stringDist.length()), tLength, separator + (stringDist = String.format("%0$.1f", distance)));
			
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

		private char cChar;
		
		private String sc;
		private StringBuilder text;
		
		private boolean useScrollColor;
		private int v;

		private int displayTicks;
		private int runs;
		
		public Scroller(String text, int visibleLength, ChatColor color, int displayTicks, boolean canBeOverridden){
			lock = new ObjectLock(text, canBeOverridden);
			updater = this;
			
			this.v = visibleLength <= 64 ? visibleLength : 64;
			this.useScrollColor = color != null ? true : false;
			this.sc = color.toString();
			this.cChar = ChatColor.COLOR_CHAR;
			this.text = new StringBuilder(this.sc + "    " + text);

			this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
			this.runs = 0;
			
			currentEntry.update(100F, text.substring(0, v));
			
			this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
		}
		public void run() {
			
			if (!currentEntry.text.equals(text.substring(0, v)) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || p == null || !p.isOnline()){
				safeCancel();
				return;
			}
				
			if (useScrollColor){
				text.append(text.charAt(2));
				text.replace(0, 2, this.sc);
			} else {
				if (text.substring(0, 1).matches("[&" + cChar + "][0-9a-f]")){
					text.append(text.substring(0, 2)).delete(0, 2);
				} else {
					text.append(text.substring(0)).deleteCharAt(0);
				}
				
			}
			
			currentEntry.update(100F, text.substring(0, v));
			
		}
		
		public void safeCancel() {try {
			lock = null;
			currentEntry.hide();
			cancel();
		} catch (IllegalStateException e) {}} 	
		
	}

	private class Shine extends BukkitRunnable{
		private final static int runPeriod = 1;
		
		private int currentIndex;
		private int delay;
		private boolean canBeOveridden;
		
		private int i1, i2, i3;
		
		private String one = ChatColor.GOLD.toString(), two = ChatColor.YELLOW.toString(), three = ChatColor.RED.toString();
		private StringBuilder text;
		
		private int displayTicks;
		private int runs;
		
		public Shine(String text, int delay, int displayTicks, boolean canBeOverridden){
			lock = new ObjectLock(text, canBeOverridden);
			updater = this;
			
			this.text = new StringBuilder();
			this.delay = delay;
			this.canBeOveridden = canBeOverridden;
			
			this.displayTicks = canBeOverridden? displayTicks : displayTicks <= 0? 500 : displayTicks;
			this.runs = 0;
			
			currentEntry.update(100F, text);
			this.currentIndex = 0;
			
			this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
		}
		
		public void run() {
			if ((!currentEntry.text.equals(text.toString())) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || (text.length() <= 7))
			{
				safeCancel();
				return;
			}
			
			if (currentIndex+7 > text.length()){
				int newDisplayTicks = (displayTicks - (runs * runPeriod));
				if (newDisplayTicks > 0){
					safeCancelNoHide();
					new Shine(this.text.toString(), delay, newDisplayTicks, canBeOveridden).runTaskLater(NoxCore.getInstance(), delay);
					return;
				}
				
				safeCancel();
				return;
			}
			
			text.delete(i1, i1+1).delete(i2, i2+1).delete(i3, i3+1);//remove old colors
			
			currentIndex = currentIndex+2;
			i1 = currentIndex;
			i2 = currentIndex+3;
			i3 = currentIndex+6;
			
			text.insert(i1, one).insert(i1+3, two).insert(i1+6, three);//add again
			
			currentEntry.update(100F, text.toString());
			
		}
		
		public void safeCancelNoHide() {
			try {
				lock = null;
				updater = null;
				cancel();
			} catch (IllegalStateException e) {}	
		}
		
		public void safeCancel() {
			try {
				lock = null;
				currentEntry.hide();
				cancel();
			} catch (IllegalStateException e) {}
		}
	}
	
}
