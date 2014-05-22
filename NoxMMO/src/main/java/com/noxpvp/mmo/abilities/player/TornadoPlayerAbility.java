
package com.noxpvp.mmo.abilities.player;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.effect.vortex.BaseVortex;
import com.noxpvp.core.effect.vortex.BaseVortexEntity;
import com.noxpvp.core.listeners.NoxPLPacketListener;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class TornadoPlayerAbility extends BasePlayerAbility implements IPVPAbility {

	public final static String ABILITY_NAME = "Tornado";
	public final static String PERM_NODE = "tornado";
	
	@Override
	public String getDescription() {
		return "The Tornado Lord is capable of summoning a vast amount of high power wind abilities";
	}
	
	private int range;
	private int time;
	
	public TornadoPlayerAbility(Player p, int range, int time) {
		super(ABILITY_NAME, p);
		
		this.range = range;
		this.time = time;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Location loc;
		if ((loc = LineOfSightUtil.getTargetBlockLocation(getPlayer(), range, Material.AIR)) != null) {
			new TornadoVortex(getPlayer(), loc, time).start();
			
			return true;
		}
		
		return false;
	}
	
	private class TornadoVortex extends BaseVortex {
		
		private NoxPLPacketListener itemSpawnHandler;
		private Block currentBlock;
		private Vector direction;
		
		public NoxPlugin getPlugin() {
			return NoxMMO.getInstance();
		}
		
		public TornadoVortex(Player user, Location loc, int time) {
			super(user, loc, time);
			
			setWidth(1.5);
			setHeightGain(0.4);
			setMaxSize(100);
			setSpeed(3);
			
			this.direction = user.getLocation().getDirection().normalize().multiply(1).setY(0);
			this.currentBlock = getNewCurrentBlock(loc);
			
			this.itemSpawnHandler = new NoxPLPacketListener(getPlugin(), PacketType.Play.Server.ENTITY_METADATA) {
				
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer packet = event.getPacket();
					
					if (packet.getEntityModifier(event).read(0) instanceof Item) {
						WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
						ItemStack stack = watcher.getItemStack(10);
						
						if (stack != null && stack.getItemMeta().hasLore()) {
							watcher = watcher.deepClone();
							watcher.setObject(10, new ItemStack(Material.FEATHER));
							
							packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
						}
					}
				}

			};
			
			itemSpawnHandler.register();
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				
				public void run() {
					itemSpawnHandler.unRegister();
					
				}
			}, time);

		}
		
		public Block getNewCurrentBlock(Location loc) {
			loc.add(0, 2, 0);
			
			int i = 0;
			while (!loc.getBlock().getType().isSolid() && i++ < 10)
				loc.add(0, -1, 0);
			
			return loc.getBlock();
		}

		public void onRun() {
			Location loc = getLocation();
			setLocation((loc = loc.add(direction)));
			
			currentBlock = getNewCurrentBlock(loc);
			
			// Spawns 10 blocks at a time.
			for (int i = 0; i < 10; i++) {
				addEntity(new TornadoVortexEntity(this, loc, currentBlock));
				
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
		
		public TornadoVortexEntity(BaseVortex parent, Location loc, Block block)  {
			super(parent, loc, loc.getWorld().dropItem(loc.clone().add(0, 1, 0), BaseVortex.dummySpinItem));
			
			Item e = (Item) getEntity();
			
			e.setPickupDelay(Short.MAX_VALUE);
			ItemMeta meta = e.getItemStack().getItemMeta();
			meta.setLore(Arrays.asList((Integer.toString(hashCode()))));
			e.getItemStack().setItemMeta(meta);
			
			new ParticleRunner(ParticleType.largesmoke, getEntity(), false, 0, 1, 0).start(0, getParent().getSpeed());
			new ParticleRunner(ParticleType.splash, getEntity(), true, 0, 1, 0).start(0, getParent().getSpeed());
		}
		
		public TornadoVortexEntity(BaseVortex parent, Location loc, Entity base) {
			super(parent, loc, base);
		}

		public boolean onRemove() {
			return getEntity() instanceof Item;

		}

		public boolean onCreation() {
			return true;
		}

		public HashSet<TornadoVortexEntity> onTick() {
			Entity e = getEntity();
			BaseVortex parent = getParent();
			Player user = parent.getUser();
			
			//suck nearby entities into the tornado, if a player give them confusion >:D
			HashSet<TornadoVortexEntity> nearby_pickups = new HashSet<TornadoPlayerAbility.TornadoVortexEntity>();
			for (Entity it : e.getNearbyEntities(4, 4, 4)) {
				if (!(it instanceof LivingEntity))
					continue;
				
				if (isVortexEntity(it) || it.equals(user))
					continue;
				
				nearby_pickups.add(new TornadoVortexEntity(parent, parent.getLocation(), it));
				((LivingEntity) it).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8 * 20, 2));
			}
			
			return nearby_pickups;
		}
		
	}

}