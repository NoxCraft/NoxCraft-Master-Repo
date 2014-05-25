package com.noxpvp.core.effect.vortex;

import java.util.ArrayDeque;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxPlugin;

public interface IVortex {

	public void start();

	public void stop();
	
	public void onStop();

	public void onRun();

	public Player getUser();

	public Location getLocation();

	public void setLocation(Location loc);

	public int getSpeed();

	public void setSpeed(int speedInTicks);

	public int getMaxSize();

	public void setMaxSize(int size);

	public double getWidth();

	public void setWidth(double width);

	public double getHeightGain();

	public void setHeightGain(double height);

	public void addEntity(BaseVortexEntity entity);

	public ArrayDeque<? extends BaseVortexEntity> getEntities();

	public void clearEntities();

	public NoxPlugin getPlugin();

}
