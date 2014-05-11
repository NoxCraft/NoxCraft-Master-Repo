package com.noxpvp.mmo.vortex;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;

public abstract class BaseVortexEntity implements IVortexEntity {
	
	private FixedMetadataValue uniqueMeta;
	public static final String uniqueMetaKey = "VortexMeta";
	
	private BaseVortex parent;
	private Entity base;
	
	private double width, height;
	
	private int ticker_vertical;
	private int ticker_horisontal;
	
	public BaseVortexEntity(BaseVortex parent, Location loc, Entity base) {
		this.uniqueMeta = new FixedMetadataValue(NoxMMO.getInstance(), parent.hashCode());
		this.parent = parent;
		this.base = base;
		
		this.width = parent.getWidth();
		this.height = parent.getHeightGain();
		
		ticker_vertical = 0;
		ticker_horisontal = (int) Math.round((Math.random() * 360));
		
		base.teleport(loc);
		base.setMetadata(uniqueMetaKey, uniqueMeta);
	}
	
	public HashSet<? extends BaseVortexEntity> tick() {
		double radius = BaseVortex.lookup.get(verticalTicker())[0] * width;
		int	horisontal = horizontalTicker();
		
		double verticalDif = 0;
		if (height == 0D)
			verticalDif = getParent().getUser().getLocation().getY() - getEntity().getLocation().getY();
		
		Vector v = new Vector(radius * BaseVortex.lookup.get(horisontal)[1], (verticalDif == 0? height : verticalDif), radius * BaseVortex.lookup.get(horisontal)[0]);
		
		setVelo(v);
		
		return onTick();
	}
	
	public boolean isVortexEntity(Entity e) {
		return e.getMetadata(uniqueMetaKey).size() > 0;
	}
	
	public BaseVortex getParent() {
		return this.parent;
	}
	
	public Entity getEntity() {
		return this.base;
	}
	
	public void remove() {
		if (onRemove()) {
			this.base.removeMetadata(uniqueMetaKey, NoxMMO.getInstance());
			this.base.remove();
		}
	}
	
	public int verticalTicker() {
		if (ticker_vertical < 90) {
			ticker_vertical += 5;
		}
		return ticker_vertical;
	}
	
	public int horizontalTicker() {
		ticker_horisontal = (ticker_horisontal + 45) % 360;
		return ticker_horisontal;
	}

	public void setVelo(Vector velo) {
		this.base.setVelocity(velo);
	}

	public Vector getVelo() {
		return this.base.getVelocity();
	}

}
