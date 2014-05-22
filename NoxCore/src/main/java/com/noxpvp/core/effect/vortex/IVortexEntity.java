package com.noxpvp.core.effect.vortex;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface IVortexEntity {

	public HashSet<? extends BaseVortexEntity> tick();

	public BaseVortex getParent();

	public Entity getEntity();

	public void remove();

	public int verticalTicker();

	public int horizontalTicker();

	public Vector getVelo();

	public void setVelo(Vector velo);

	public boolean onRemove();

	public boolean onCreation();

	public HashSet<? extends BaseVortexEntity> onTick();

}
