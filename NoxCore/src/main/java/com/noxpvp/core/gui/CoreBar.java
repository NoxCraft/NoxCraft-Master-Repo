package com.noxpvp.core.gui;

import me.confuser.barapi.BarAPI;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.manager.PlayerManager;

public class CoreBar{
	
	private final Entry currentEntry = new Entry();
	public final Player p;
	private Object lock = null;
	private Runnable updater = null;
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
		
		pm.addCoreBar(this);
	}
	
	public void newFlasher(String text) {
		if (lock == null || lock != text)
			this.new Flasher(null);
	}
	
	public void newLivingTracker(LivingEntity e, String text, String color){
		newLivingTracker(e, text, color, false);
	}
	
	public void newLivingTracker(LivingEntity e, String text, String color, boolean ignoreLOS) {
		if (lock == null || lock != e.getUniqueId()){
			this.new LivingTracker(e, text, color, ignoreLOS);
			p.sendMessage("new tracker started");
		}
		else if (updater != null)
			updater.run();
	}
	
	public void newScroller(String text, int length, ChatColor color) {
		if (lock == null || lock != text)
			this.new Scroller(text, length, color);
	}
	
	public void newShine(String text, int delay) {
		if (lock == null || lock != text)
			this.new Shine(text, delay);
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

		private String text;
		
		public Flasher(String text){
			lock = text;
			updater = this;
			this.text = text;
			
			
			currentEntry.update(100F, text);
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 6);
		}
		
		public void run() {
			if (!currentEntry.text.equals(text))
			{
				safeCancel();
				return;
			}
			
			text = ChatColor.stripColor(text);
			text = ChatColor.COLOR_CHAR + RandomUtils.nextInt(9) + text;
			
			currentEntry.update(100F, text.toString());
			
		}
		
		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}} 	
		
	}
	

	private class LivingTracker extends BukkitRunnable{

		private double distance;
		private String stringDist;
		private boolean ignoreLOS;
		
		private LivingEntity e;
		
		private StringBuilder text;
		private String separator;
		
		public LivingTracker(LivingEntity e, String text, String color, boolean ignoreLOS) {
			lock = e.getUniqueId();
			updater = this;
			
			this.e = e;
			
			this.separator = ChatColor.GOLD + " - " + ChatColor.RESET;
			this.distance = p.getLocation().distance(e.getLocation());
			this.ignoreLOS = ignoreLOS;
			
			this.text = new StringBuilder(text).append(separator).append((stringDist = String.format("%0$.1f", distance)));
			
			currentEntry.update(e, text.toString());
			this.runTaskTimer(NoxCore.getInstance(), 0, 8);
		}
		
		public void run() {
			distance = p.getLocation().distance(e.getLocation());
			
			if (lock != e.getUniqueId() || distance > 75 || p == null || e == null || p.isDead() || e.isDead())
			{
				safeCancel();
				return;
			}
			
			if (!ignoreLOS && !p.hasLineOfSight(e)){
				safeCancel();
				return;
			}
			
			int tLength = text.length();
			
			text.replace(tLength - (separator.length() + stringDist.length()), tLength, separator + (stringDist = String.format("%0$.1f", distance)));
			
			currentEntry.update(e, text.toString());
			
		}
		
		public void safeCancel() {try {
			lock = null;
			currentEntry.hide();
			cancel();
		} catch (IllegalStateException e) {}}
		
	}
	
	private class Scroller extends BukkitRunnable{

		private char cChar;
		
		private String sc;
		private StringBuilder text;
		
		private boolean useScrollColor;
		private int v;
		
		public Scroller(String text, int visibleLength, ChatColor color){
			lock = text;
			updater = this;
			this.v = visibleLength <= 64 ? visibleLength : 64;
			this.useScrollColor = color != null ? true : false;
			this.sc = color.toString();
			this.cChar = ChatColor.COLOR_CHAR;
			
			this.text = new StringBuilder(this.sc + "    " + text);
			currentEntry.update(100F, text.substring(0, v));
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 5);
		}
		public void run() {
			if (!currentEntry.text.equals(text.toString()))
			{
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

		
		private int currentIndex;
		private int delay;
		
		private int i1, i2, i3;
		
		private String one = ChatColor.GOLD.toString(), two = ChatColor.YELLOW.toString(), three = ChatColor.RED.toString();
		private StringBuilder text;
		
		public Shine(String text, int delay){
			lock = text;
			updater = this;
			
			this.text = new StringBuilder();
			this.delay = delay;
			
			currentEntry.update(100F, text);
			this.currentIndex = 0;
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 1);
		}
		
		public void run() {
			if ((!currentEntry.text.equals(text.toString())) || (text.length() <= 7))
			{
				safeCancel();
				return;
			}
			if (currentIndex+7 > text.length()){
				safeCancel();
				new Shine(this.text.toString(), 1).runTaskLater(NoxCore.getInstance(), delay);
				
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
		
		public void safeCancel() {try {
			lock = null;
			currentEntry.hide();
			cancel();
		} catch (IllegalStateException e) {}} 	
		
	}
	
}
