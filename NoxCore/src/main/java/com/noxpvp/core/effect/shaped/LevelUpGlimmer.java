package com.noxpvp.core.effect.shaped;

import org.bukkit.Location;
import org.bukkit.Sound;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;

public class LevelUpGlimmer extends BaseCorkScrew {
	
	public LevelUpGlimmer(Location loc, int time, double heightLimit) {
		super(loc.add(-1, 0, 0), time, heightLimit);
		
		setSpeed(1);
		setRadiusGain(0.5);
		setHeightGain(0.05);
	}

	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}
	
	public void onStart() {
		getLocation().getWorld().playSound(getLocation(), Sound.LEVEL_UP, 3, 0);
	}

	public void onRun() {
		new ParticleRunner(ParticleType.happyVillager, getLocation(), false, 0, 1, 1).start(0);		
		new ParticleRunner(ParticleType.fireworksSpark, getLocation(), false, 0, 10, 1).start(0);
		new ParticleRunner(ParticleType.cloud, getLocation(), true, 0, 10, 1).start(0);
		
	}
	
	public void onStop() {
	}

}
