package com.noxpvp.mmo.abilities.player;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.vortex.BaseVortex;
import com.noxpvp.mmo.vortex.BaseVortexEntity;

public class TornadoAbility extends BasePlayerAbility implements PVPAbility {

	public final static String ABILITY_NAME = "Tornado";
	public final static String PERM_NODE = "tornado";
	
	@Override
	public String getDescription() {
		return "The tornado lord is capable of summoning a vast amount of high power wind abilities";
	}
	
	private int range;
	private int time;
	
	public TornadoAbility(Player p, int range, int time) {
		super(ABILITY_NAME, p);
		
		this.range = range;
		this.time = time;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Location loc;
		if ((loc = LineOfSightUtil.getTargetBlockLocation(getPlayer(), range, (Material) null)) != null) {
			new TornadoVortex(getPlayer(), loc, time).start();
			
			return true;
		}
		
		return false;
	}
	
	private class TornadoVortex extends BaseVortex {

		BaseMMOEventHandler<EntityChangeBlockEvent> handler;
		Block currentBlock;
		Vector direction;
		
		public TornadoVortex(Player user, Location loc, int time) {
			super(user, loc, time);
			
			setWidth(2);
			setHeightGain(0.8);
			setMaxSize(100);
			setSpeed(5);
			
			this.direction = user.getLocation().getDirection().normalize().multiply(1.5).setY(0);
			this.currentBlock = getNewCurrentBlock(loc);
			
			handler = new BaseMMOEventHandler<EntityChangeBlockEvent>(
					new StringBuilder().append(user.getName()).append(ABILITY_NAME).append("EntityChangeBlockEvent").toString(),
					EventPriority.NORMAL, 1) {
				
				public boolean ignoreCancelled() {
					return true;
				}
				
				public Class<EntityChangeBlockEvent> getEventType() {
					return EntityChangeBlockEvent.class;
				}
				
				public String getEventName() {
					return "EntityChangeBlockEvent";
				}
				
				public void execute(EntityChangeBlockEvent event) {
					Entity e;
					if ((e = event.getEntity()) == null)
						return;
					
					if (e.getMetadata(BaseVortexEntity.uniqueMetaKey).contains(this.hashCode())) {
						event.setCancelled(true);
						e.remove();
					}
					
				}
			};
			
			registerHandler(handler);
		}
		
		public Block getNewCurrentBlock(Location loc) {
			loc.add(0, 2, 0);
			while (!loc.getBlock().getType().isSolid())
				loc.add(0, -1, 0);
			
			return loc.getBlock();
		}

		public void onRun() {
			Location loc;
			setLocation((loc = getLocation().add(direction)));
			
			currentBlock = getNewCurrentBlock(loc);
			
			// Spawns 10 blocks at a time.
			for (int i = 0; i < 10; i++) {
				addEntity(new TornadoVortexEntity(this, loc, currentBlock.getType()));
				
			}
			
			// Make all entities in the list spin, and suck up any close by stuff
			ArrayDeque<BaseVortexEntity> que = new ArrayDeque<BaseVortexEntity>();

			for (BaseVortexEntity ve : getEntities()) {
				HashSet<? extends BaseVortexEntity> new_entities = ve.tick();
				for(BaseVortexEntity temp : new_entities) {
					que.add(temp);
				}
			}
			
			// Add the new entities we sucked in
			for(BaseVortexEntity vb : que) {
				addEntity(vb);
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
			if (getEntity() instanceof FallingBlock)
				return true;
			
			getEntity().removeMetadata(uniqueMetaKey, NoxMMO.getInstance());
			return false;
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
