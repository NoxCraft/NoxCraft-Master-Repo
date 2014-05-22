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
	
	public void setLocation(Location loc);
	
	public Location getLocation();
	
	public void setDirection(Vector direction);
	
	public Vector getDirection();
	
	public void setRadiusGain(double gain);
	
	public double getRadiusGain();
	
	public void setForwardGain(double gain);
	
	public double getForwardGain();
	
	public void setCanRender(boolean can);
	
	public boolean getCanRender();
}