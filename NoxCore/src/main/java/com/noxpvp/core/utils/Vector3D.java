package com.noxpvp.core.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.bergerkiller.bukkit.common.bases.mutable.VectorAbstract;
 
public class Vector3D extends VectorAbstract{
	private final double x,y,z;
	
	/**
	 * Represents the null (0, 0, 0) origin.
	 */
	public static final Vector3D ORIGIN = new Vector3D(0, 0, 0);
 
	/**
	 * Construct an immutable 3D vector.
	 */
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	public VectorAbstract setX(double x) {
		return new Vector3D(x, y, z);
	}
	
	@Override
	public VectorAbstract setY(double y) {
		return new Vector3D(x, y, z);
	}
	
	@Override
	public VectorAbstract setZ(double z) {
		return new Vector3D(x, y, z);
	}
 
	/**
	 * Construct an immutable floating point 3D vector from a location object.
	 * @param location - the location to copy.
	 */
	public Vector3D(Location location) {
		this(location.toVector());
	}
 
	/**
	 * Construct an immutable floating point 3D vector from a mutable Bukkit vector.
	 * @param vector - the mutable real Bukkit vector to copy.
	 */
	public Vector3D(Vector vector) {
		if (vector == null)
			throw new IllegalArgumentException("Vector cannot be NULL.");
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
	}
 
	/**
	 * Convert this instance to an equivalent real 3D vector.
	 * @return Real 3D vector.
	 */
	public Vector toVector() {
		return new Vector(x, y, z);
	}
 
	/**
	 * Retrieve the absolute value of this vector.
	 * @return The new result.
	 */
	public Vector3D abs() {
		return new Vector3D(Math.abs(x), Math.abs(y), Math.abs(z));
	}
 
	@Override
	public String toString() {
		return String.format("[x: %s, y: %s, z: %s]", x, y, z);
	}
}