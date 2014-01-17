package com.noxpvp.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.scoreboards.CommonObjective;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard.Display;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;

public class CoreBoard{
	private CommonScoreboard sb;
	private CommonObjective ob;
	public Player p;
	
	
	private Map<String, BoardScroller> scrollers;
	private Map<String, BoardTimer> timers;
	private Map<String, BoardEntry> entries;
	PlayerManager pm;
	
	private List<Integer> takenSlots;
	
	/**
	 * 
	 * @param objName - Main name displayed on the score board
	 */
	public CoreBoard(NoxCore core, String objName, Player p){
		p.setScoreboard(null); // Always get a new scoreboard
		this.sb = CommonScoreboard.get(p);
		
		this.ob = sb.getObjective(Display.SIDEBAR);
		this.ob.setDisplayName(objName);
		this.p = p;

		this.scrollers = new HashMap<String, BoardScroller>();
		this.timers = new HashMap<String, BoardTimer>();
		this.entries = new HashMap<String, BoardEntry>();
		this.pm = core.getPlayerManager();
		
		this.takenSlots = new ArrayList<Integer>();
		
		
		
		String name = p.getName();
		if (pm.hasCoreBoard(name)){
			pm.removeCoreBoard(name);
		}
		
		pm.addCoreBoard(this);
	}
	
	private int getNewSlot(){
		int n = 50;
		
		while (takenSlots.contains(n)) {n--;}
		this.takenSlots.add(n);
		
		return  n;
	}
	
	private CoreBoard removeSlotID(int id)
	{
		try {takenSlots.remove(takenSlots.indexOf(id));} catch (ArrayIndexOutOfBoundsException e) {}
		return this;
	}
	
	/**
	 * Makes the scoreboard visible
	 * 
	 * @return CoreBoard - This instance
	 */
	public CoreBoard show(){this.ob.show(); return this;}
	
	/**
	 * Makes the scoreboard hidden
	 * 
	 * @return CoreBoard - This instance
	 */
	public CoreBoard hide(){this.ob.hide(); return this;}
	
	/**
	 * Updates the objectives scores
	 * 
	 * @return CoreBoard - This instance
	 */
	public CoreBoard updateScores() {this.ob.update(); return this;}
	
	/**
	 * Cancels all running timers and scrollers on this CoreBoard
	 * 
	 * @return CoreBoard - This instance
	 */
	public CoreBoard cancelTimers(){
		for (BoardTimer timer: timers.values())
		{
			timer.safeCancel();
		}
		for (BoardScroller scroller: scrollers.values())
		{
			scroller.safeCancel();
		}
		timers.clear();
		scrollers.clear();
		return this;
	}
	
	/**
	 * 
	 * @param name - The name of the entry to remove
	 * @return CoreBoard - This instance
	 */
	public CoreBoard removeEntry(String name){
		
		BoardEntry entry = entries.get(name);
		if (entry == null)
			return this;
		
		entry.remove();
		
		return this;
	}
	
	/**
	* 
	* @param entry - The entry to remove
	* @return CoreBoard - This instance
	*/
	public CoreBoard removeEntry(BoardEntry entry)
	{
		return removeEntry(entry.getName());
	}
	
	/**
	 * 
	 * @param name - A unique name to help the CoreBoard remove/track the score
	 * @param displayName - The name shown for the score
	 * @param score - The score itself. Yes, this supports double types - unlike normal scoreboards
	 * @param nameColor - The color to display the name
	 * @param scoreColor - The color to display the score
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addScore(String name, String displayName, String score, ChatColor nameColor, ChatColor scoreColor){
		String displayedName = nameColor + displayName;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(scoreColor).append(score);
		
		while (sb.length() < 14){
			sb.insert(0, " ");
			sb.append(" ");
		}
		String scoreName = sb.toString();
		
		
		new BoardEntry(name, displayedName, scoreName);
		
		return this;
	}
	
	/**
	 * 
	 * @param name - The name for the score, only used internally for tracking and removing the score
	 * @param displayedName - The name of the scroller to be displayed on the scoreboard
	 * @param visibleLength - The length of the scroller to be displayed
	 * @param nameColor - The color of the scroller name
	 * @param scrollerColor - The color of the scroller being displayed
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addScroller(String name, String displayedName, String scrollText, int visibleLength, ChatColor nameColor, ChatColor scrollerColor){
		
		BoardScroller scroller = new BoardScroller(name, displayedName, scrollText, visibleLength, nameColor, scrollerColor);
		scroller.runTaskTimer(NoxCore.getInstance(), 0, 5);
		scrollers.put(scroller.name, scroller);
		
		return this;
	}
	
	/**
	 * 
	 * @param name - The name for the score, only used internally for tracking and removing the score
	 * @param displayedName - The name of the timer to be displayed on the scoreboard
	 * @param seconds - The total time in seconds
	 * @param nameColor - The color of the timer name
	 * @param scoreColor - The color of the time counter being displayed
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addTimer(String name, String displayedName, int seconds, ChatColor nameColor, ChatColor scoreColor){
		
		BoardTimer timer = new BoardTimer(name, displayedName, seconds, nameColor, scoreColor);
		timer.runTaskTimer(NoxCore.getInstance(), 0, 20);
		timers.put(timer.name, timer);
		
		return this;
	}
	
	private class BoardEntry {
		private final String name;
		private String scoreName;
		private String displayedName;
		private int slot1, slot2;
		private boolean active;
		private boolean isGood = true;
		
		public int getSlot1(){ return slot1;}
		public int getSlot2(){ return slot2;}
		
		public BoardEntry(String name, String displayedName, String scoreName)
		{
			for (BoardEntry e : entries.values()){
				if (e.displayedName == displayedName || e.displayedName.contains(displayedName)){
					e.remove();
					break;
				}
			}
			
			this.name = name;
			this.displayedName = displayedName;
			this.scoreName = scoreName;
			
			entries.put(name, this);
			show();
		}
		
		public String getName() { return name; }

		public String getValue() { return scoreName; }
		
		public BoardEntry setValue(String value)
		{
			this.scoreName = value;
			hide();
			show();
			return this;
		}
		
		public BoardEntry getNewSlots()
		{
			ob.removeScore(name + slot1);
			ob.removeScore(name + slot2);
			
			
			removeSlotID(slot1).removeSlotID(slot2);
			slot1 = getNewSlot();
			slot2 = getNewSlot();
			
			ob.createScore(name + slot1, displayedName, slot1);
			ob.createScore(name + slot2, scoreName, slot2);
			
			return this;
		}
		
		public BoardEntry setDisplayName(String name){
			this.displayedName = name;
			hide();
			show();
			return this;
		}
		
		public boolean isRendering(){ return active; }
		
		public BoardEntry hide()
		{
			if (!active || !isGood)
				return this;
			ob.removeScore(name + slot1);
			ob.removeScore(name + slot2);
			
			active = false;
			return this;
		}
		
		public BoardEntry show()
		{
			if (active || !isGood)
				return this;
			
			active = true;
			
			ob.createScore(name + slot1, displayedName, slot1);
			ob.createScore(name + slot2, scoreName, slot2);
			
			return this;
		}
		
		public void remove() {
			hide();
			
			removeSlotID(slot1).removeSlotID(slot2);
			
			isGood = false;
			entries.remove(name);
		}
	}

	private class BoardScroller extends BukkitRunnable{

		public void safeCancel() {try {cancel();} catch (IllegalStateException e) {}}
		
		public final String name;
		private StringBuilder text;
		private int v;
		
		private BoardEntry entry;
		
		private boolean useScrollColor;
		private String sc;
		
		public BoardScroller(String name, String displayedName, String scrollText, int visibleLength, ChatColor nameColor, ChatColor scrollerColor){
			this.name = name;
			this.text = new StringBuilder("    " + scrollText);
			this.v = visibleLength <= 14 ? visibleLength : 14;
			
			this.useScrollColor = scrollerColor != null ? true : false;
			this.sc = scrollerColor.toString();

			entry = new BoardEntry(name, (nameColor + displayedName), this.text.substring(0, this.v));
		}
		
		public void run() {
			if (entry == null || !entries.containsValue(entry)){
				safeCancel();
				return;
			}
			
			if (useScrollColor){
				text.append(text.charAt(2));
				text.replace(0, 2, this.sc);
			} else {
				text.append(text.charAt(0));
				text.deleteCharAt(0);
				
			}
			entry.setValue(text.substring(0, this.v));
		}
		
	}
	
	private class BoardTimer extends BukkitRunnable{
		public final String name;
		private String timerString;
		private ChatColor sc;
		
		private BoardEntry entry;
		
		private long timeStamp;
		
		public BoardTimer(String name, String displayedName, int seconds, ChatColor nameColor, ChatColor scoreColor){
			this.name = name;
			this.sc = scoreColor;
			
			this.timeStamp = System.currentTimeMillis() + (seconds * 1000);
			
			long s = (timeStamp - System.currentTimeMillis()) / 1000;
			
			this.timerString = getFormattedTime(s);
			
			entry = new BoardEntry(name, (nameColor + displayedName), this.timerString);
		}
		
		public void safeCancel() { try { cancel(); } catch (IllegalStateException e) {} }
		
		public void run() {
			if (entry == null || !entries.containsValue(entry)){
				safeCancel();
				return;
			}
			
			if (System.currentTimeMillis() > timeStamp || p == null || !p.isValid() || !p.isOnline()) {
				if (p.isOnline()){
					entry.remove();
					return;
				}
				
				safeCancel();
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(getFormattedTime((timeStamp - System.currentTimeMillis())/1000));
			
			sb.insert(0, sc);
			timerString = sb.toString();
			
			while(sb.length() <= 14){
				sb.insert(0, " ");
				sb.append(" ");
			}
			timerString = sb.toString();
			
			entry.setValue(timerString);
		}
		
		private String getFormattedTime(long seconds)
		{
			int s = (int) (seconds % 60);
			seconds /= 60;
			
			int m= (int) (seconds % 60);
			seconds /= 60;
			
			int h= (int) (seconds % 24);
			seconds /= 24;
			
			return String.format("%1$02d:%2$02d:%3$02d", h, m, s);
			
		}
		
	}

}
