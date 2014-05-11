package com.noxpvp.mmo.abilities.player;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.vortex.BaseVortex;
import com.noxpvp.mmo.vortex.BaseVortexEntity;

public class TornadoAbility extends BasePlayerAbility implements PVPAbility {

	public final static String ABILITY_NAME = "Tornado";
	public final static String PERM_NODE = "tornado";
	
	private int range;
	private int time;
	
	public TornadoAbility(Player p, int range, int time) {
		super(ABILITY_NAME, p);
		
		this.range = range;
		this.time = time;
	}

	public boolean execute() {
		Location loc;
		if ((loc = LineOfSightUtil.getTargetBlockLocation(getPlayer(), range, (Material) null)) != null) {
			new TornadoVortex(getPlayer(), loc, time).start();
			
			return true;
		}
		
		return false;
	}
	
	private class TornadoVortex extends BaseVortex {

		Block currentBlock;
		
		public TornadoVortex(Player user, Location loc, int time) {
			super(user, loc, time);
			
			setWidth(2);
			setHeightGain(0.8);
			setMaxSize(100);
			
			this.currentBlock = getNewCurrentBlock(loc);
		}
		
		public Block getNewCurrentBlock(Location loc) {
			loc.add(0, 2, 0);
			while (!loc.getBlock().getType().isSolid())
				loc.add(0, -1, 0);
			
			return loc.getBlock();
		}

		public void onRun() {
			Location loc = getLocation();
			
			// Spawns 10 blocks at a time.
			for (int i = 0; i < 10; i++) {
				addEntity(new TornadoVortexEntity(this, loc, currentBlock.getType()));
				
			}
			
		}
		
	}
	
	private class TornadoVortexEntity extends BaseVortexEntity {

		public TornadoVortexEntity(BaseVortex parent, Location loc, Material type) {
			super(parent, loc, loc.add(0, 1, 0).getWorld().spawnFallingBlock(loc, type, (byte) 0));
		}
		
		public TornadoVortexEntity(BaseVortex parent, Location loc, Block block) {
			super(parent, loc, loc.clone().add(0, 1, 0).getWorld().spawnFallingBlock(loc, block.getType(), block.getData()));
		}
		
		public TornadoVortexEntity(BaseVortex parent, Location loc, Entity base) {
			super(parent, loc, base);
		}

		public boolean onRemove() {
			return true;
		}

		public boolean onCreation() {
			return true;
		}

		public HashSet<TornadoVortexEntity> onTick() {
			Entity e = getEntity();
			BaseVortex parent = getParent();
			Player user = parent.getUser();
			
			//suck nearby entities into the tornado, if a player give them confusion >:D
			HashSet<TornadoVortexEntity> nearby_pickups = new HashSet<TornadoAbility.TornadoVortexEntity>();
			for (Entity it : e.getNearbyEntities(4, 4, 4)) {
				if (!(it instanceof LivingEntity))
					continue;
				
				if (isVortexEntity(it) || it.equals(user))
					continue;
				
				nearby_pickups.add(new TornadoVortexEntity(parent, parent.getLocation(), it));
				((LivingEntity) it).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8, 2));
			}
			
			return nearby_pickups;
		}
		
	}

}
