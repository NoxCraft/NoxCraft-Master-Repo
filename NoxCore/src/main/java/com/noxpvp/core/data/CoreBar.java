package com.noxpvp.core.data;

import java.util.regex.Pattern;

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
	
	private class Entry{
		String text;
		float percentFilled;
		
		public Entry(){
			this.text = "";
			this.percentFilled = 100F;
		}
		
		public void update(float percentFilled, String text){
			BarAPI.setMessage(p, text, percentFilled);
			
			if (percentFilled <= 0) 
				this.text = "";
			
		}
		
	}
	

	public class Scroller extends BukkitRunnable{

		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
		
		private StringBuilder text;
		private int v;
		
		private boolean useScrollColor;
		private String sc;
		
		private char cChar;
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
			if (currentEntry.text != text.toString())
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
		
	}
	

	public class Flasher extends BukkitRunnable{

		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
		
		private String text;
		
		public Flasher(String text){
			this.text = text;
			
			currentEntry.update(100F, text);
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 4);
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
		
	}

	private class LivingTracker extends BukkitRunnable{

		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
		
		private double distance;
		
		private LivingEntity e;
		private StringBuilder text;
		
		public LivingTracker(LivingEntity e, String text){
			this.e = e;
			distance = p.getLocation().distance(e.getLocation());
			
			this.text = new StringBuilder(text).append(" - ").append(distance);
			
			currentEntry.update((float) (e.getHealth() / e.getMaxHealth() * 100), text.toString());
			
			this.runTaskTimer(NoxCore.getInstance(), 0, 4);
		}
		
		public void run() {
			if (currentEntry.text != text.toString() || !p.isOnline() || p == null)
			{
				safeCancel();
				return;
			}
			
			distance = p.getLocation().distance(e.getLocation());
			int tLength = text.length();
			
			text.replace(tLength - (3 + Double.toString(distance).length()), tLength, " - " + distance);
			
			currentEntry.update((float) ((e.getHealth() / e.getMaxHealth()) * 100), text.toString());
			
		}
		
	}
	
}
