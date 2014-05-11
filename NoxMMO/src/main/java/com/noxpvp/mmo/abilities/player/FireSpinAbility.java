package com.noxpvp.mmo.abilities.player;

import java.util.ArrayDeque;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.vortex.BaseVortex;
import com.noxpvp.mmo.vortex.BaseVortexEntity;

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
		
		public FireSpinVortex(Player user, int time) {
			super(user, user.getLocation(), time);
			
			setWidth(3.0);
			setHeightGain(0.1);
			setMaxSize(30);
			setSpeed(2);

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
		
		public FireSpinVortexEntity(BaseVortex parent) {
			super(parent, parent.getLocation(), parent.getLocation().getWorld().spawnEntity(parent.getLocation(), EntityType.CHICKEN));
			
			
			((Chicken) getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Short.MAX_VALUE, 10, true));
			
			new ParticleRunner(ParticleType.flame, getEntity(), false, 0, 5, 0).start(0, getParent().getSpeed());
		}
		
		public FireSpinVortexEntity(BaseVortex parent, Location loc, Entity base) {
			super(parent, loc, base);
			
		}

		public boolean onRemove() {
			if (getEntity() instanceof Chicken)
				return true;
			
			getEntity().removeMetadata(uniqueMetaKey, NoxMMO.getInstance());
			return false;
		}

		public boolean onCreation() {
			return true;
		}

		public HashSet<? extends BaseVortexEntity> onTick() {
			Entity e = getEntity();
			BaseVortex parent = getParent();
			Player user = parent.getUser();
			
			for (Entity it : user.getNearbyEntities(1, 1, 1)) {
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
