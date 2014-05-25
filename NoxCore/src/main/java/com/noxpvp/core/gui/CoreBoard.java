package com.noxpvp.core.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.scoreboards.CommonObjective;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard.Display;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.TimeUtils;
import com.noxpvp.core.utils.gui.MessageUtil;

public class CoreBoard {
	private static final int BOARD_MAX_ENTRIES = 15;
	private static final int BOARD_OBJ_NAME_LENGTH_MAX = 28;
	private static final int BOARD_SCORE_NAME_MAX_LENGTH = 16;
	
	private Player p;
	private CorePlayerManager pm;
	private Map<String, BoardEntry> entries;
	private List<Integer> takenSlots;
	private Map<String, BoardScroller> scrollers;
	private Map<String, BoardTimer> timers;
	private CommonScoreboard sb;
	private CommonObjective ob;

	public CoreBoard(NoxCore core, Player player) {
		this(core, MessageUtil.parseColor(core.getCoreConfig().get("gui.coreboard.default-name", String.class, "&cNox&6MMO")), player);
	}

	/**
	 * @param objName - Main name displayed on the score board
	 */
	public CoreBoard(NoxCore core, String objName, Player p) {
		CommonScoreboard.removePlayer(p);
		this.sb = CommonScoreboard.get(p);

		this.ob = sb.getObjective(Display.SIDEBAR);
		this.ob.setDisplayName(SpaceOut(objName, BOARD_OBJ_NAME_LENGTH_MAX));
		this.p = p;

		this.scrollers = new HashMap<String, BoardScroller>();
		this.timers = new HashMap<String, BoardTimer>();
		this.entries = new HashMap<String, BoardEntry>();
		this.pm = CorePlayerManager.getInstance();

		this.takenSlots = new ArrayList<Integer>();

	}

	private static String SpaceOut(String text, int maxLenth) {
		StringBuilder string = new StringBuilder(text);

		while (string.length() <= maxLenth - 2) {
			string.insert(0, " ").append(" ");
		}
		if (string.length() < maxLenth)
			string.append(" ");

		return string.toString();
	}

	/**
	 * @param name        - A unique name to help the CoreBoard remove/track the score
	 * @param displayName - The name shown for the score
	 * @param score       - The score itself. Yes, this supports double types - unlike normal scoreboards
	 * @param nameColor   - The color to display the name
	 * @param scoreColor  - The color to display the score
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addScore(String name, String displayName, String score, ChatColor nameColor, ChatColor scoreColor) {
		new BoardEntry(name, SpaceOut(nameColor + displayName, 16), SpaceOut(scoreColor + score, 16));

		show();
		return this;
	}

	/**
	 * @param name          - The name for the score, only used internally for tracking and removing the score
	 * @param displayedName - The name of the scroller to be displayed on the scoreboard
	 * @param visibleLength - The length of the scroller to be displayed
	 * @param nameColor     - The color of the scroller name
	 * @param scrollerColor - The color of the scroller being displayed
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addScroller(String name, String displayedName, String scrollText, int visibleLength, ChatColor nameColor, ChatColor scrollerColor) {

		BoardScroller scroller = new BoardScroller(name, displayedName, scrollText, visibleLength, nameColor, scrollerColor);
		scroller.runTaskTimer(NoxCore.getInstance(), 0, 4);
		scrollers.put(scroller.name, scroller);

		show();
		return this;
	}

	/**
	 * @param name          - The name for the score, only used internally for tracking and removing the score
	 * @param displayedName - The name of the timer to be displayed on the scoreboard
	 * @param seconds       - The total time in seconds
	 * @param nameColor     - The color of the timer name
	 * @param scoreColor    - The color of the time counter being displayed
	 * @return CoreBoard - This instance
	 */
	public CoreBoard addTimer(String name, String displayedName, int seconds, ChatColor nameColor, ChatColor scoreColor) {

		BoardTimer timer = new BoardTimer(name, displayedName, seconds, nameColor, scoreColor);
		timer.runTaskTimer(NoxCore.getInstance(), 0, 20);
		timers.put(timer.name, timer);

		show();
		return this;
	}

	/**
	 * Cancels all running timers and scrollers on this CoreBoard
	 *
	 * @return CoreBoard - This instance
	 */
	public CoreBoard cancelTimers() {
		for (BoardTimer timer : timers.values()) {
			timer.safeCancel();
		}
		for (BoardScroller scroller : scrollers.values()) {
			scroller.safeCancel();
		}
		timers.clear();
		scrollers.clear();
		return this;
	}

	/**
	 * @param entry - The entry to remove
	 * @return CoreBoard - This instance
	 */
	public CoreBoard removeEntry(BoardEntry entry) {
		return removeEntry(entry.getName());
	}

	/**
	 * @param name - The name of the entry to remove
	 * @return CoreBoard - This instance
	 */
	public CoreBoard removeEntry(String name) {

		BoardEntry entry = entries.get(name);
		if (entry == null)
			return this;

		entry.remove();
		
		if (entries.size() < 1)
			hide();

		return this;
	}

	/**
	 * Makes the scoreboard hidden
	 *
	 * @return CoreBoard - This instance
	 */
	public CoreBoard hide() {
		this.ob.hide();
		return this;
	}

	/**
	 * Makes the scoreboard visible
	 *
	 * @return CoreBoard - This instance
	 */
	public CoreBoard show() {
		this.ob.show();
		return this;
	}

	/**
	 * Updates the objectives scores
	 *
	 * @return CoreBoard - This instance
	 */
	public CoreBoard updateScores() {
		this.ob.update();
		return this;
	}

	private CoreBoard removeSlotID(int id) {
		try {
			takenSlots.remove(takenSlots.indexOf(id));
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return this;
	}

	private int getNewSlot() {
		int n = BOARD_MAX_ENTRIES;

		while (takenSlots.contains(n)) {
			n--;
		}
		this.takenSlots.add(n);

		return n;
	}

	private class BoardEntry {
		private final String name;
		private boolean active;
		private boolean isGood = true;
		private String displayedName;
		private String scoreName;
		private int slot1, slot2;

		public BoardEntry(String name, String displayedName, String scoreName) {
			for (BoardEntry e : entries.values()) {
				if (e.name.equals(name) || e.displayedName.equals(displayedName) || e.scoreName.equals(scoreName)) {
					e.remove();
				}
			}

			this.name = name;
			this.displayedName = SpaceOut(displayedName, BOARD_SCORE_NAME_MAX_LENGTH).substring(0, BOARD_SCORE_NAME_MAX_LENGTH);
			this.scoreName = SpaceOut(scoreName, 16).substring(0, BOARD_SCORE_NAME_MAX_LENGTH);

			this.slot1 = getNewSlot();
			this.slot2 = getNewSlot();

			entries.put(name, this);
			show();
		}

		public String getName() {
			return name;
		}

		public BoardEntry getNewSlots() {
			ob.removeScore(name + slot1);
			ob.removeScore(name + slot2);


			removeSlotID(slot1).removeSlotID(slot2);
			slot1 = getNewSlot();
			slot2 = getNewSlot();

			ob.createScore(name + slot1, displayedName, slot1);
			ob.createScore(name + slot2, scoreName, slot2);

			return this;
		}

		public int getSlot1() {
			return slot1;
		}

		public int getSlot2() {
			return slot2;
		}

		public String getValue() {
			return scoreName;
		}

		public BoardEntry setValue(String value) {
			this.scoreName = value;
			hide();
			show();
			return this;
		}

		public BoardEntry hide() {
			if (!active || !isGood)
				return this;
			
			ob.removeScore(name + slot1);
			ob.removeScore(name + slot2);

			active = false;
			return this;
		}

		public boolean isRendering() {
			return active;
		}

		public void remove() {
			hide();

			entries.remove(name);
			removeSlotID(slot1).removeSlotID(slot2);

			isGood = false;
		}

		public BoardEntry setDisplayName(String name) {
			this.displayedName = name;
			hide();
			show();
			return this;
		}

		public BoardEntry show() {
			if (active || !isGood)
				return this;

			active = true;

			ob.createScore(name + slot1, displayedName, slot1);
			ob.createScore(name + slot2, scoreName, slot2);

			return this;
		}
	}

	private class BoardScroller extends BukkitRunnable {

		public final String name;
		private BoardEntry entry;
		private String displayName;
		private ChatColor sc;
		private StringBuilder text;

		private boolean useScrollColor;

		private int v;

		public BoardScroller(String name, String displayedName, String scrollText, int visibleLength,
		                     @Nullable ChatColor nameColor, @Nullable ChatColor scrollerColor) {
			this.name = name;
			this.displayName = nameColor != null ? (nameColor + displayedName) : displayedName;

			this.text = new StringBuilder(scrollText + "  ||  ");

			this.useScrollColor = scrollerColor != null;
			this.sc = useScrollColor ? scrollerColor : null;

			this.v = useScrollColor ? (visibleLength <= 14 ? visibleLength : 14) : (visibleLength <= 16 ? visibleLength : 16);

			entry = new BoardEntry(name, displayName, this.text.substring(0, this.v));
		}

		public void run() {
			if (entry == null || !entries.containsValue(entry) || p == null || !p.isOnline()) {
				safeCancel();
				return;
			}

			text.append(text.charAt(0));
			text.deleteCharAt(0);

			entry.setValue(useScrollColor ? sc + text.substring(0, this.v) : text.substring(0, this.v));
		}

		public void safeCancel() {
			try {
				cancel();
			} catch (IllegalStateException e) {
			}
		}

	}

	private class BoardTimer extends BukkitRunnable {
		public final String name;
		private BoardEntry entry;
		private String displayName;
		private ChatColor sc;

		private String timerString;

		private boolean useScoreColor;
		private long timeStamp;

		public BoardTimer(String name, String displayedName, int seconds,
		                  @Nullable ChatColor nameColor, @Nullable ChatColor scoreColor) {
			this.name = name;
			this.displayName = nameColor != null ? (nameColor + displayedName) : displayedName;

			this.useScoreColor = scoreColor != null;
			this.sc = useScoreColor ? scoreColor : null;

			this.timeStamp = (System.currentTimeMillis() / 1000) + seconds;

			this.timerString = TimeUtils.getReadableSecTime(timeStamp - (System.currentTimeMillis() / 1000));

			entry = new BoardEntry(name, displayName, timerString);
		}

		public void run() {
			if (entry == null || !entries.containsValue(entry)) {
				safeCancel();
				return;
			}

			if ((System.currentTimeMillis() / 1000) > (timeStamp - 1) || p == null || !p.isValid() || !p.isOnline()) {
				if (p.isOnline()) {
					entry.remove();
				}

				safeCancel();
				return;
			}

			StringBuilder sb = new StringBuilder();

			sb.append(TimeUtils.getReadableSecTime(timeStamp - (System.currentTimeMillis() / 1000)));

			sb.insert(0, sc);

			entry.setValue(SpaceOut(sb.toString(), 16).substring(0, 16));
		}

		public void safeCancel() {
			try {
				cancel();
			} catch (IllegalStateException e) {
			}
		}

	}

}
