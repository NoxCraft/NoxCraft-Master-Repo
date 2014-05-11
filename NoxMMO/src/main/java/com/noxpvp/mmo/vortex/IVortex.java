package com.noxpvp.mmo.vortex;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IVortex {
	
	public void start();
	
	public void stop();
	
	public void onRun();
	
	public Player getUser();
	
	public void setLocation(Location loc);
	
	public Location getLocation();
	
	public void setMaxSize(int size);
	
	public int getMaxSize();
	
	public void setWidth(double width);
	
	public double getWidth();
	
	public void setHeightGain(double height);
	
	public double getHeightGain();
	
	public void addEntity(BaseVortexEntity entity);
	
	public void clearEntities();

}
