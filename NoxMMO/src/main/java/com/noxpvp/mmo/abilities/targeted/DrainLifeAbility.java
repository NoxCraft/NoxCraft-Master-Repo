package com.noxpvp.mmo.abilities.targeted;

import java.util.ArrayDeque;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class DrainLifeAbility extends BaseTargetedPlayerAbility {
	
	public final static String ABILITY_NAME = "Drain Life";
	public final static String PERM_NODE = "drain-life";
	
	private int time;
	private int period;
	
	public DrainLifeAbility(Player p) {
		this(p, 5);
	}
	
	public DrainLifeAbility(Player p, double range) {
		super(ABILITY_NAME, p, range, MMOPlayerManager.getInstance().getPlayer(p).getTarget());
		
		this.time = 20 * 10;
		this.period = 20;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		new DrainingLifePipe(getPlayer(), getTarget(), time).start(0);
		
		return true;
		
	}
	
	private class DrainingLifePipe extends BukkitRunnable {
		private ArrayDeque<heart> ents;
		private Player player;
		private LivingEntity target;
		
		private Location pLoc, tLoc;
		
		private int counter;
		private int time;
		private int speed;
		
		public DrainingLifePipe(Player p, LivingEntity target, int time) {
			this.player = p;
			this.pLoc = p.getLocation();
			this.target = target;
			this.tLoc = target.getLocation();
			this.ents = new ArrayDeque<heart>();
			this.time = time;
			this.counter = 0;
			this.speed = 1;
		}

		public void start(int delay) {
			this.runTaskTimer(NoxMMO.getInstance(), delay, speed);
			
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				
				public void run() {
					stop();
				}
			}, time);
		}
		
		public void stop() {
			for (heart h : ents)
				h.remove();
			
			ents.clear();
			
			try {
				cancel();
			} catch (IllegalStateException e) {}
		}

		public void run() {
			if (!mayExecute())
				stop();
			
			if ((counter % period) == 0  && target.getHealth() > getDamage() && player.getHealth() < player.getMaxHealth()) {
				target.damage(getDamage(), player);
				player.setHealth(Math.min(player.getHealth() + getDamage(), player.getMaximumAir()));
			}
			
			ents.add(new heart(tLoc));
			
			while (ents.size() > 50) {
					heart first = ents.getFirst();
					first.remove();
					ents.remove(first);
			}
			
			for (heart heart : ents) {
				if (heart.loc.distance(player.getLocation()) < 1) {
					heart.remove();
					continue;
				}
				
				Vector dir = pLoc.toVector().subtract(heart.loc.toVector()).normalize().multiply(0.5);
				heart.tick(dir);
			}
			
			counter += speed;
			
		}
		
		public Hologram newHeart() {
			return HoloAPI.getManager().createSimpleHologram(tLoc, 2, ChatColor.RED + "♥");
		}
		
		private class heart {
			Location loc;
			Hologram h;
			
			public heart(Location loc) {
				this.loc = loc;
				this.h = newHeart();
			}
			
			public void remove() {
				this.h.clearAllPlayerViews();
				ents.remove(this.h);
			}
			
			public void tick(Vector direction) {
				this.loc.add(direction);
//				new ParticleRunner(ParticleType.happyVillager, loc, false, 0, 1, 1).start(0);
				this.h.move(loc);
			}
		}
		
	}

}
