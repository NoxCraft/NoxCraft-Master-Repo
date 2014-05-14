package com.noxpvp.core.effect;

import java.util.ArrayDeque;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxPlugin;

public interface IVortex {
	
	public void start();
	
	public void stop();
	
	public void onRun();
	
	public Player getUser();
	
	public void setLocation(Location loc);
	
	public Location getLocation();
	
	public void setSpeed(int speedInTicks);
	
	public int getSpeed();
	
	public void setMaxSize(int size);
	
	public int getMaxSize();
	
	public void setWidth(double width);
	
	public double getWidth();
	
	public void setHeightGain(double height);
	
	public double getHeightGain();
	
	public void addEntity(BaseVortexEntity entity);
	
	public ArrayDeque<? extends BaseVortexEntity> getEntities();
	
	public void clearEntities();
	
	public NoxPlugin getPlugin();

}
