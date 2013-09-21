package com.noxpvp.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.bergerkiller.bukkit.common.bases.mutable.LocationAbstract;

public class SafeLocation extends LocationAbstract implements ConfigurationSerializable {

	private float pitch, yaw;
	private double x, y, z;
	private String worldName;
	private transient World world;
	
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("pitch", pitch);
		data.put("yaw", yaw);
		data.put("x", x);
		data.put("y", y);
		data.put("z", z);
		data.put("world", worldName);
		return data;
	}
	
	public static SafeLocation deserialize(Map<String, Object> data)
	{
		float pitch, yaw;
		double x, y, z;
		String world;
		try {
			try {
				pitch = ((Double) data.get("pitch")).floatValue();
				yaw = ((Double) data.get("yaw")).floatValue();
			} catch (ClassCastException e) {
				pitch = (Float) data.get("pitch");
				yaw = (Float) data.get("yaw");
			}
			x = (Double) data.get("x");
			y = (Double) data.get("y");
			z = (Double) data.get("z");
			world = (String) data.get("world");
		} catch (Throwable e) {
			NoxCore plugin = NoxCore.getInstance();
			plugin.log(Level.SEVERE, "Error occured while translating location info from config.");
			if (e instanceof ClassCastException)
				plugin.log(Level.SEVERE, "Data was of the wrong type.");
			plugin.handle(e);
			return null;
		}
		return new SafeLocation(world, x, y, z, pitch, yaw);
	}
	
	public SafeLocation(Location location)
	{
		pitch = location.getPitch();
		yaw = location.getYaw();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		world = location.getWorld();
		worldName = world.getName();
	}
	
	public SafeLocation(final String worldName2, final double x2, final double y2, final double z2, final float pitch2, final float yaw2) {
		worldName = worldName2;
		x = x2;
		y = y2;
		z = z2;
		pitch = pitch2;
		yaw = yaw2;
		world = Bukkit.getServer().getWorld(worldName2);
	}

	@Override
	public float getPitch() {
		return pitch;
	}

	@Override
	public World getWorld() {
		return (world!=null)?world:Bukkit.getServer().getWorld(worldName);
	}
	
	public String getWorldName(){
		return worldName;
	}

	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public LocationAbstract setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	@Override
	public LocationAbstract setWorld(World world) {
		this.world = world;
		this.worldName = world.getName();
		return this;
	}

	@Override
	public LocationAbstract setX(double x) {
		this.x = x;
		return this;
	}

	@Override
	public LocationAbstract setY(double y) {
		this.y = y;
		return this;
	}

	@Override
	public LocationAbstract setYaw(float yaw) {
		this.yaw = yaw;
		return this;
	}

	@Override
	public LocationAbstract setZ(double z) {
		this.z = z;
		return this;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		final String w = getWorldName();
		return new StringBuilder().append("{world=")
				.append((w==null?"null":w))
				.append(", x=").append(getX())
				.append(", y=").append(getY())
				.append(", z=").append(getZ())
				.append(", yaw=").append(getYaw())
				.append(", pitch=").append(getPitch())
				.append("}").toString();
	}
}
