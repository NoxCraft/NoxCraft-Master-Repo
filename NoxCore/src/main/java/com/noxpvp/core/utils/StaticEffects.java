package com.noxpvp.core.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.dsh105.holoapi.HoloAPI;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.packet.ParticleRunner;


public class StaticEffects {

	private static List<LivingEntity> frozenEntitys = new ArrayList<LivingEntity>();
	
	public static void DamageAmountParticle(LivingEntity e, double damage){
		DamageAmountParticle(e.getEyeLocation(), damage);
	}
	
	public static void DamageAmountParticle(Location loc, double damage){
		HoloAPI.getManager().createSimpleHologram(loc, 5, true, ChatColor.RED + String.format("%.1f", damage));
	}
	
	public static void SkullBreak(LivingEntity e){
		SkullBreak(e.getEyeLocation(), NoxCore.getInstance());
	}
	
	public static void SkullBreak(Location loc, NoxPlugin plugin){
		new ParticleRunner("blockcrack_155_0", loc, false, .1F, 20, 2).start(0);
	}
	
	public static void BloodEffect(Entity e){
		BloodEffect(e, NoxCore.getInstance());
	}
	
	public static void BloodEffect(Entity e, NoxPlugin plugin){
		new ParticleRunner("blockdust_35_14", e, false, .12F, 15, 1).runTaskTimer(plugin, 0, 0);
	}
	
	public static void BroadcastSound(Entity e, Sound sound){
		BroadcastSound(e.getLocation(), sound);
	}
	
	public static void BroadcastSound(Location loc, Sound sound){
		loc.getWorld().playSound(loc, sound, 10, 0);
	}
	
	public static void PlaySound(Player p, Sound sound){
		PlaySound(p, p.getLocation(), sound.name());
	}
	
	public static void playSound(Player p, String sound){
		PlaySound(p, p.getLocation(), sound);
	}
	
	public static void PlaySound(Player p, Location loc, String sound){
		CommonPacket packet = new CommonPacket(PacketType.OUT_NAMED_SOUND_EFFECT);
		
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.soundName, sound);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.volume, 10.0F);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.pitch, 2);
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.x, (int) loc.getX());
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.y, (int) loc.getY());
		packet.write(PacketType.OUT_NAMED_SOUND_EFFECT.z, (int) loc.getZ());
		
		PacketUtil.sendPacket(p, packet, false);
	}

	/**
	 * Freezes the entity (jump and movement) for the set ticks.
	 * If the entity has already been and is still frozen, will return false
	 * 
	 * @param e
	 * @param ticks
	 * 
	 * @return Boolean if freezing was successful
	 */
	public static boolean Freeze(final LivingEntity e, int ticks){
		if (frozenEntitys.contains(e))
			return false;
		
		final List<PotionEffect> preserve = new ArrayList<PotionEffect>();
		for (PotionEffect effect : e.getActivePotionEffects()){
			preserve.add(new PotionEffect(
					effect.getType(),
					effect.getDuration() - ticks,
					effect.getAmplifier(),
					effect.isAmbient()));
		}
		
		PotionEffect walking = new PotionEffect(PotionEffectType.SPEED, ticks, -50, false),
					jumping = new PotionEffect(PotionEffectType.JUMP, ticks, -50, false);
		
		e.addPotionEffect(walking, true);
		e.addPotionEffect(jumping, true);
		
		
		final NoxListener<NoxPlugin> freezeRemovalPrevention = new NoxListener<NoxPlugin>(NoxCore.getInstance()) {
			
			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionDrink(PlayerItemConsumeEvent event){
				if (!e.equals(event.getPlayer()) || event.getItem().getType() != Material.POTION)
					return;
				
				if (!frozenEntitys.contains(e)){
					this.unregister();
					return;
				}
				
				event.setCancelled(true);	
			}
			
			@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
			public void onPotionSplash(PotionSplashEvent event){
				if (!event.getAffectedEntities().contains(e))
					return;
				
				if (!frozenEntitys.contains(e)){
					this.unregister();
					return;
				}
				try {
					event.getAffectedEntities().remove(e);
					event.setCancelled(true);
				} catch (UnsupportedOperationException e) {}
					
			}
		};
		
		Bukkit.getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
			
			public void run() {
				if (e == null || !e.isValid()){
					return;
				}
				
				for (PotionEffect effect : preserve)
					if (effect.getDuration() > 0)
						e.addPotionEffect(effect, true);
				
				if (StaticEffects.frozenEntitys.contains(e))
					StaticEffects.frozenEntitys.remove(e);
			
				freezeRemovalPrevention.unregister();
			}
		}, ticks);
		
		
		freezeRemovalPrevention.register();
		
		return frozenEntitys.add(e);
	}
	
	public static class Tornado {
		
		private final static HashMap<Integer, Double[]> lookup = new HashMap<Integer, Double[]>();
		
		private NoxCore core;
		private Player p;
		
		private Location loc;
		private long time;
		private Material material;
		private int amount_of_blocks; 
		
		private byte data;
		private Vector direction;
		private double speed;
		
		/**
		 * Spawns a tornado at the given location l.
		 * 
		 * @param location
		 *            - Location to spawn the tornado.
		 * @param material
		 *            - The base material for the tornado.
		 * @param blockLimit
		 *            - The max amount of blocks that can exist in the tornado.
		 * @param direction
		 *            - The direction the tornado should move in.
		 * @param time
		 *            - The amount of ticks the tornado should be alive.
		 */
		public Tornado(Player p, Location loc, Material type, int blockLimit, Vector direction, long time) {
			
			this.core = NoxCore.getInstance();
			this.p = p;
			
			this.loc = loc;
			this.amount_of_blocks = blockLimit;
			this.material = type;
			this.time = time;
			
			this.data = 0;
			this.direction = direction;
			this.speed = 0.50;
			
		}
		
		private void generateLookup() {
			if(lookup.size() != 0) {
				return;
			}
			
			for(int i = 0 ; i < 360 ; i++) {
				Double[] data = new Double[2];
				data[0] = Math.sin(Math.toRadians(i));
				data[1] = Math.cos(Math.toRadians(i));
				lookup.put(i, data);
			}
		}

		public boolean execute() {
				
			generateLookup();
			
			class VortexBlock {
				
				private Entity entity;
				
				public boolean removable = true;
				
				private int ticker_vertical   = 0;
				private int ticker_horisontal = (int) Math.round((Math.random() * 360));
				
				public VortexBlock(Location l, Material m, byte d) {
					
					if (l.getBlock().getType() != Material.AIR) {
						
						Block b = l.getBlock();
						entity = l.getWorld().spawnFallingBlock(b.getRelative(BlockFace.UP).getLocation(), b.getType(), b.getData());
						
						
					}
					else {
						entity = l.getWorld().spawnFallingBlock(l.getBlock().getRelative(BlockFace.UP).getLocation(), m, d);
						removable = true;
					}
					
					addMetadata();
				}
				
				public VortexBlock(Entity e) {
					entity    = e;
					removable = false;
					addMetadata();
				}
				
				private void addMetadata() {
					entity.setMetadata("vortex", new FixedMetadataValue(core, "protected"));
				}
				
				public void remove() {
					if(removable) {
						entity.remove();
					}
					entity.removeMetadata("vortex", core);
				}
				
				public HashSet<VortexBlock> tick() {
					
					double radius     = lookup.get(verticalTicker())[0] * 2;
					int    horisontal = horisontalTicker();
					
					Vector v = new Vector(radius * lookup.get(horisontal)[1], 0.5D, radius * lookup.get(horisontal)[0]);
					
					HashSet<VortexBlock> new_blocks = new HashSet<VortexBlock>();
					
					// Pick up blocks
					Block b = entity.getLocation().add(v.clone().normalize()).getBlock();
					if(b.getType() != Material.AIR) {
						new_blocks.add(new VortexBlock(b.getLocation(), b.getType(), b.getData()));
					}
					
					// Pick up other entities
					List<Entity> entities = entity.getNearbyEntities(1.7D, 1.0D, 1.7D);
					for(Entity e : entities) {
						if(!e.hasMetadata("vortex")) {
							if (!e.equals(p)) {
								new_blocks.add(new VortexBlock(e));
								
								if (e instanceof LivingEntity)
									((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 35, 1));
							}
						}
					}
					
					setVelocity(v);
					
					return new_blocks;
				}
				
				private void setVelocity(Vector v) {
					entity.setVelocity(v);
				}
				
				private int verticalTicker() {
					if (ticker_vertical < 90) {
						ticker_vertical += 5;
					}
					return ticker_vertical;
				}
				
				private int horisontalTicker() {
					ticker_horisontal = (ticker_horisontal + 45) % 360;
					return ticker_horisontal;
				}
			}
			
			// Modify the direction vector using the speed argument.
			if (direction != null) {
				direction.normalize().multiply(speed);
			}
			
			// This set will contain every block created to make sure the metadata for each and everyone is removed.
			final HashSet<VortexBlock> clear = new HashSet<VortexBlock>();
			
			final int id = new BukkitRunnable() {
				
				private ArrayDeque<VortexBlock> blocks = new ArrayDeque<VortexBlock>();
				
				public void run() {
					
					if (direction != null) {
						loc.add(direction);
					}
		
					// Spawns 10 blocks at the time.
					for (int i = 0; i < 10; i++) {
						checkListSize();
						VortexBlock vb = new VortexBlock(loc, material, data);
						blocks.add(vb);
						clear.add(vb);
					}
					
					// Make all blocks in the list spin, and pick up any blocks that get in the way.
					ArrayDeque<VortexBlock> que = new ArrayDeque<VortexBlock>();
		
					for (VortexBlock vb : blocks) {
						HashSet<VortexBlock> new_blocks = vb.tick();
						for(VortexBlock temp : new_blocks) {
							que.add(temp);
						}
					}
					
					// Add the new blocks
					for(VortexBlock vb : que) {
						checkListSize();
						blocks.add(vb);
						clear.add(vb);
					}
				}
				
				// Removes the oldest block if the list goes over the limit.
				private void checkListSize() {
					while(blocks.size() >= amount_of_blocks) {
						VortexBlock vb = blocks.getFirst();
						vb.remove();
						blocks.remove(vb);
						clear.remove(vb);
					}
				}
			}.runTaskTimer(core, 5L, 5L).getTaskId();
		
			// Stop the "tornado" after the given time.
			new BukkitRunnable() {
				public void run() {
					for(VortexBlock vb : clear) {
						vb.remove();
					}
					core.getServer().getScheduler().cancelTask(id);
				}
			}.runTaskLater(core, time);
			
			return true;
		}

	}

	
}
