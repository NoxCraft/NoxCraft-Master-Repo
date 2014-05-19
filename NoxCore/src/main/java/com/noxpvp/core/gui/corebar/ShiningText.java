package com.noxpvp.core.gui.corebar;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.gui.ShiningStringScroller;

public class ShiningText extends BukkitRunnable {
	
	private final static int runPeriod = 1;
	
	private Player p;
	private CoreBar bar;
	
	private ShiningStringScroller text;
	
	private int displayTicks;
	private int runs;
	
	public ShiningText(Player p, String text) {
		this(p, text, 0, 300, true);
	}
	
	public ShiningText(Player p, String text, int delay, int displayTicks, boolean canBeOverridden){
		
		this.p = p;
		this.bar = CorePlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;
		
		this.text = new ShiningStringScroller(text);
		
		this.displayTicks = canBeOverridden? displayTicks : displayTicks <= 0? 500 : displayTicks;
		this.runs = 0;
		
		this.runTaskTimer(NoxCore.getInstance(), delay, runPeriod);
	}
	
	public boolean checkValid() {
		return !(p == null || !bar.hasLock(this) ||
				(displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) || !p.isValid() ||
				!p.isOnline());

	}
	
	public void run() {
		if (!checkValid())
		{
			safeCancel();
			return;
		}

		bar.getCurrentEntry().update(100F, text.shine());			
		runs++;
	}
	
	public void safeCancel() {
		try {
			bar.removeIfLockedBy(this);
			cancel();
		} catch (IllegalStateException e) {}
	}
}