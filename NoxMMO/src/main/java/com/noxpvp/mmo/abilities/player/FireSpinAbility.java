package com.noxpvp.mmo.abilities.player;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.effect.BaseVortex;
import com.noxpvp.core.effect.BaseVortexEntity;
import com.noxpvp.core.listeners.NoxPLPacketListener;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class FireSpinAbility extends BasePlayerAbility {
	
	public final static String ABILITY_NAME = "FireSpin";
	public final static String PERM_NODE = "fire-spin";
	
	@Override
	public String getDescription() {
		return "Surrounds the user with a power ring of spinning fire, Scorching anyone in your path";
	}
	
	private int time;
	
	public FireSpinAbility(Player p) {
		super(ABILITY_NAME, p);
		
		this.time = 20 * 10;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		new FireSpinVortex(getPlayer(), time).start();
		
		return true;
		
	}
	
	private class FireSpinVortex extends BaseVortex {
		
		private NoxPLPacketListener handler;
		
		public NoxPlugin getPlugin() {
			return NoxMMO.getInstance();
		}
		
		public FireSpinVortex(Player user, int time) {
			super(user, user.getLocation(), time);
			
			setWidth(1.5);
			setHeightGain(0.2);
			setMaxSize(50);
			setSpeed(2);
			
			this.handler = new NoxPLPacketListener(NoxMMO.getInstance(), PacketType.Play.Server.ENTITY_METADATA) {
				
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer packet = event.getPacket();
					
					if (packet.getEntityModifier(event).read(0) instanceof Item) {
						WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
						ItemStack stack = watcher.getItemStack(10);
						
						if (stack == null || !stack.hasItemMeta())
							return;
						
						if (!stack.getItemMeta().hasLore() || !stack.getItemMeta().getLore().contains(dummyItemMeta))
							return;
						
						watcher = watcher.deepClone();
						watcher.setObject(10, new ItemStack(Material.FIRE));
						
						packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
					}
				}

			};
			
			handler.register();
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				
				public void run() {
					handler.unRegister();
					
				}
			}, time);

			for (int i = 0; i < 50; i++) {
				addEntity(new FireSpinVortexEntity(this));
				
			}
			
			
		}

		public void onRun() {
			setLocation((getUser().getLocation()));
			
			// Spawns 1 bats at a time.
			for (int i = 0; i < 5; i++) {
				addEntity(new FireSpinVortexEntity(this));
				
			}
			
			// Make all entities in the list spin, and set all the things on fire
			ArrayDeque<BaseVortexEntity> que = new ArrayDeque<BaseVortexEntity>();

			for (BaseVortexEntity ve : getEntities()) {
				if (ve == null)
					continue;
				
				HashSet<? extends BaseVortexEntity> new_entities = ve.tick();
				if (new_entities == null || new_entities.size() < 1)
					continue;
				
				for(BaseVortexEntity temp : new_entities) {
					que.add(temp);
				}
			}
			
			// Add the new entities, if any
			for(BaseVortexEntity vb : que) {
				addEntity(vb);
			}
		}
		
	}
	
	private class FireSpinVortexEntity extends BaseVortexEntity {
		
		public FireSpinVortexEntity(FireSpinVortex parent) {
			super(parent, parent.getLocation(), 
					parent.getLocation().getWorld().dropItem(parent.getLocation(), BaseVortex.dummySpinItem));
			
			Item e = (Item) getEntity();
			e.setPickupDelay(Short.MAX_VALUE);
			
			new ParticleRunner(ParticleType.dripLava, e, false, 0, 1, 0).start(0, parent.getSpeed());
		}
		
		public FireSpinVortexEntity(BaseVortex parent, Location loc, Entity base) {
			super(parent, loc, base);
			
		}

		public boolean onRemove() {
			if (getEntity() instanceof Item) {
				return true;
			}
			
			return false;
		}

		public boolean onCreation() {
			return true;
		}

		public HashSet<? extends BaseVortexEntity> onTick() {
			Player user = getParent().getUser();
			
			for (Entity it : getEntity().getNearbyEntities(1, 1, 1)) {
				if (!(it instanceof Damageable))
					continue;
				
				if (isVortexEntity(it) || it.equals(user))
					continue;
				
				if (it.getFireTicks() < 50)
					it.setFireTicks(100);
			}
			
			return null;
			
		}
		
	}

}
