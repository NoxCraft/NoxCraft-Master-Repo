package com.noxpvp.core.effect.shaped;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.noxpvp.core.NoxPlugin;

public interface IHelix {

	public NoxPlugin getPlugin();

	public void render(int maxRuns);

	public void start(int delay);

	public void onStart();

	public void onStop();

	public void run();

	public void onRun();

	public Location getLocation();

	public void setLocation(Location loc);

	public Vector getDirection();

	public void setDirection(Vector direction);

	public double getRadiusGain();

	public void setRadiusGain(double gain);

	public double getForwardGain();

	public void setForwardGain(double gain);

	public boolean getCanRender();

	public void setCanRender(boolean can);
}
