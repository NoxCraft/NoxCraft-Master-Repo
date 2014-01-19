package com.noxpvp.core.data;

import me.confuser.barapi.BarAPI;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;

public class CoreBar{
	
	private final Entry currentEntry = new Entry();
	
	public final Player p;
	PlayerManager pm;
	
	/**
	 * 
	 * @param p - The player to give this bar to
	 */
	public CoreBar(NoxCore core, Player p){
		this.p = p;
		this.pm = core.getPlayerManager();
		
		String name = p.getName();
		if (pm.hasCoreBar(name)){
				pm.removeCoreBar(name);
		}
		
		pm.addCoreBar(this);
	}
	
	public void newFlasher(String text) {
		this.new Flasher(null);
	}
	
	public void newLivingTracker(LivingEntity e, String text, ChatColor color) {
		this.new LivingTracker(e, text, color);
	}
	
	public void newScroller(String text, int length, ChatColor color) {
		this.new Scroller(text, length, color);
	}
	
	public void newShine(String text, int delay) {
		this.new Shine(text, delay);
	}
	
	private class Entry{
		float percentFilled;
		String text;
		
		public Entry(){
			this.text = "";
			this.percentFilled = 100F;
		}
		
		public void update(float percentFilled, String text){
			BarAPI.setMessage(p, text, percentFilled);
			
			if (percentFilled <= 0) 
				this.text = "";
			
		}
		
		public void update(String text){
			this.update(percentFilled, text);
			
		}
		
	}
	

	private class Flasher extends BukkitRunnable{

		private String text;
		
		public Flasher(String text){
			this.text = text;
			
			currentEntry.update(100F, text);
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 6);
		}
		
		public void run() {
			if (currentEntry.text != text)
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
		
		private LivingEntity e;
		private StringBuilder text;
		
		public LivingTracker(LivingEntity e, String text, ChatColor color) {
			this.e = e;
			distance = p.getLocation().distance(e.getLocation());
			
			this.text = new StringBuilder(color + text).append(" - ").append(distance);
			
			currentEntry.update((float) (e.getHealth() / e.getMaxHealth() * 100), text.toString());
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 10);
		}
		
		public void run() {
			if (!currentEntry.text.equals(text.toString()) || p == null || !p.isOnline() || p.isDead() || e == null || e.isDead())
			{
				safeCancel();
				return;
			}
			
			distance = p.getLocation().distance(e.getLocation());
			int tLength = text.length();
			
			text.replace(tLength - (3 + Double.toString(distance).length()), tLength, " - " + distance);
			
			currentEntry.update((float) ((e.getHealth() / e.getMaxHealth()) * 100), text.toString());
			
		}
		
		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
		
	}
	
	private class Scroller extends BukkitRunnable{

		private char cChar;
		
		private String sc;
		private StringBuilder text;
		
		private boolean useScrollColor;
		private int v;
		
		public Scroller(String text, int visibleLength, ChatColor color){
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
		
		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}} 	
		
	}

	private class Shine extends BukkitRunnable{

		
		private int currentIndex;
		private int delay;
		
		private int i1, i2, i3;
		
		private String one = ChatColor.GOLD.toString(), two = ChatColor.YELLOW.toString(), three = ChatColor.RED.toString();
		private StringBuilder text;
		
		public Shine(String text, int delay){
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
		
		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}} 	
		
	}
	
}
