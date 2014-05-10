package com.noxpvp.core.gui.corebar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.gui.ColoredStringScroller;

public class ScrollingText extends BukkitRunnable {
	
	private final static int runPeriod = 3;
	
	private Player p;
	private CoreBar bar;
	
	private ColoredStringScroller text;

	private int displayTicks;
	private int runs;
	
	public ScrollingText(Player p, String text, int displayTicks) {
			this(p, text, displayTicks, true);
	}
	
	public ScrollingText(Player p, String text, int displayTicks, boolean canBeOverridden) {
		
		this.p = p;
		this.bar = PlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;
		
		{
			int left = (62 - text.length());
			StringBuilder addon = new StringBuilder();
			
			while (addon.length() < left)
				addon.append(' ');
			
			text = (addon.toString() + ChatColor.RESET) + text;
		}
		
		this.text = new ColoredStringScroller(text);

		this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
		this.runs = 0;
		
		bar.getCurrentEntry().update(100F, text.substring(0, text.length()));
		
		this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
	}
	
	public boolean checkValid() {
		if (!bar.hasLock(this) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) ||
			p == null || !p.isOnline() || !p.isValid())			
			return false;
		
		return true;
	}
	
	public void run() {
		if (!checkValid()){
			safeCancel();
			return;
		}
		
		bar.getCurrentEntry().update(100F, text.scroll().substring(0, this.text.toString().length()));
		
	}
	
	public void safeCancel() {try {
		bar.removeIfLockedBy(this);
		cancel();
	} catch (IllegalStateException e) {}} 	
	
}