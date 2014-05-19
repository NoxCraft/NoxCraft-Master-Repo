package com.noxpvp.core.effect.shaped;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public abstract class BaseHelix extends BukkitRunnable implements IHelix {
	
	protected final static Map<Integer, double[]> lookup = new HashMap<Integer, double[]>();
	
	static {
		generateLookup();
	}
	
	protected static void generateLookup() {
		
		for (int i = 0; i < 360; i++) {
			double[] velos = new double[2];
			
			velos[0] = Math.sin(Math.toRadians(i));
			velos[1] = Math.cos(Math.toRadians(i));
			
			lookup.put(i, velos);
		}
	}
	
	private Location loc;
	private Vector direction;
	
	private double widthGain;
	private double forwardGain;
	private int speed;
	private int time;
	
	private int verticalTicker;
	private int horizontalTicker;
	
	public BaseHelix(Location loc, int time) {
		this.loc = loc;
		this.direction = loc.getDirection();
		this.time = time;
		
		this.widthGain = 1;
		this.forwardGain = 1;
		this.speed = 2;
		
	}
	
	public int verticalTicker() {
		if (verticalTicker < 90)
			verticalTicker += 1;
		
		return verticalTicker;
	}
	
	public int horizontalTicker() {
		return horizontalTicker = (int) ((horizontalTicker + 22.5) % 360);
	}

	public void render(int particleAmount) {
		int i = 0;
		while (i++ < particleAmount)
			run();
		
	}
	
	public void renderBeforeStart(int particleAmount) {
		render(particleAmount);
		start(0);
	}
	
	public void start(int delay) {
		this.runTaskTimer(getPlugin(), delay, speed);
		
		Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
			
			public void run() {
				try {
					BaseHelix.this.onStop();
					BaseHelix.this.cancel();
				} catch(IllegalStateException e) {}
				
			}
		}, time);
		
		onStart();
	}

	public void run() {
		this.loc.add(direction);
		
		double radius = BaseHelix.lookup.get(verticalTicker())[0] * widthGain;
		int	horizontal = horizontalTicker();
		
		double yawX = direction.getX(), yawZ = direction.getZ();
		
		Vector v = new Vector(
				(radius * BaseHelix.lookup.get(horizontal)[0]) * yawZ,
				(radius * BaseHelix.lookup.get(horizontal)[1]),
				(radius * BaseHelix.lookup.get(horizontal)[0]) * (yawX * -1));
		
		this.loc.add(v);
		this.loc.add(direction.clone().multiply(forwardGain));
		
		onRun();
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getLocation() {
		return this.loc.clone();
	}
	
	public void setDirection(Vector direction) {
		this.direction = direction;
	}
	
	public Vector getDirection() {
		return this.direction.clone();
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return this.speed;
	}

	public void setRadiusGain(double gain) {
		this.widthGain = gain;
		
	}

	public double getRadiusGain() {
		return widthGain;
	}

	public void setForwardGain(double gain) {
		this.forwardGain = gain;
		
	}

	public double getForwardGain() {
		return forwardGain;
	}

}
