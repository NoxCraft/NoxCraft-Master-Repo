package com.noxpvp.core.gui.corebar;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.google.common.collect.Sets;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;

public class LivingEntityTracker extends BukkitRunnable {
	private final static int runPeriod = 10;
	
	public final static String separater = " || ";
	public final static String color = ChatColor.GREEN.toString();
	
	private LivingEntity e;
	private Player p;
	private CoreBar bar;
	
	private double distance;
	private int maxDistance;
	
	private boolean ignoreLOS;
	private Set<Material> transparents;
	
	private StringBuilder text;		
	private String stringDist;

	private int displayTicks;
	private int runs;		
	
	public LivingEntityTracker(
				Player p,
				LivingEntity e,
				String text) {
		this(p, e, text, 25);
	}
	
	public LivingEntityTracker(
				Player p,
				LivingEntity e,
				String text,
				int maxDistance) {
		this(p, e, text, maxDistance, false);
	}
	
	public LivingEntityTracker(
				Player p,
				LivingEntity e,
				String text,
				int maxDistance,
				boolean ignoreLOS) {
		this(p, e, text, maxDistance, ignoreLOS, 300);
	}
	
	public LivingEntityTracker(
				Player p,
				LivingEntity e,
				String text,
				int maxDistance,
				boolean ignoreLOS,
				int displayTicks) {
		this(p, e, text, maxDistance, ignoreLOS, displayTicks, true);
	}
	
	public LivingEntityTracker(Player p, LivingEntity e, String text, int maxDistance, boolean ignoreLOS, int displayTicks, boolean canBeOverridden) {
		
		this.e = e;
		this.p = p;
		this.bar = PlayerManager.getInstance().getPlayer(p).getCoreBar();
		if (!bar.setLock(this, canBeOverridden))
			return;
		
		this.distance = p.getLocation().distance(e.getLocation());
		this.maxDistance = maxDistance;
		
		this.ignoreLOS = ignoreLOS;
		this.transparents = Sets.newHashSet();
		for (Material cur : Material.values()) {
			if (!MaterialUtil.ISSOLID.get(cur))
				continue;
			
			transparents.add(cur);
		}
		
		this.text = new StringBuilder().append(text).append(separater).append((stringDist = String.format("%0$.1f", distance)));
		
		this.displayTicks = canBeOverridden? displayTicks : (displayTicks <= 0? 500 : displayTicks);
		this.runs = 0;
		
		this.runTaskTimer(NoxCore.getInstance(), 0, runPeriod);
	}
	
	public boolean checkValid() {
		if (
			e == null  || p == null ||
			!bar.hasLock(this) || (displayTicks > 0 && ((runs++ * runPeriod) > displayTicks)) ||
			!e.isValid() || !p.isValid())
			return false;
		
		return true;
	}
	
	public void run() {

		if (!checkValid()){
			safeCancel();
			return;
		}
		
		distance = p.getLocation().distance(e.getLocation());
		
		if (distance > maxDistance || (!ignoreLOS && !LineOfSightUtil.hasLineOfSight(p, e.getEyeLocation(), transparents))){
			safeCancel();
			return;
		}
		
		int tLength = text.length();
		
		text.replace((tLength - (color.length() + separater.length() + stringDist.length())), tLength, (color + separater + (stringDist = String.format("%.1f", distance))));
		
		bar.getCurrentEntry().update(e, text.toString());
		
	}
	
	public void safeCancel() {
		try {
			bar.removeIfLockedBy(this);
			cancel();
		} catch (IllegalStateException e) {}
	}
	
}