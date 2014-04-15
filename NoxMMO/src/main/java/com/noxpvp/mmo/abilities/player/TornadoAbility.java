package com.noxpvp.mmo.abilities.player;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class TornadoAbility extends BasePlayerAbility implements PVPAbility {

	public final static String ABILITY_NAME = "Tornado";
	public final static String PERM_NODE = "tornado";
	
	private final static HashMap<Integer, Double[]> lookup = new HashMap<Integer, Double[]>();
	
	private NoxMMO mmo;
	private Location loc;
	private int amount_of_blocks; 
	private Material material;
	private long time;
	
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
	public TornadoAbility(Player p, Location loc, Material type, int blockLimit, Vector direction, long time) {
		super(ABILITY_NAME, p);
		
		this.loc = loc;
		this.mmo = NoxMMO.getInstance();
		this.amount_of_blocks = blockLimit;
		this.material = type;
		this.time = time;
		
		this.data = 0;
		this.direction = direction;
		this.speed = 0.50;
		
	}
	
	private static void generateLookup() {
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
				entity.setMetadata("vortex", new FixedMetadataValue(mmo, "protected"));
			}
			
			public void remove() {
				if(removable) {
					entity.remove();
				}
				entity.removeMetadata("vortex", mmo);
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
						new_blocks.add(new VortexBlock(e));
						if (e instanceof LivingEntity && !e.equals(getPlayer()))
							((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 35, 1));
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
		}.runTaskTimer(mmo, 5L, 5L).getTaskId();
	
		// Stop the "tornado" after the given time.
		new BukkitRunnable() {
			public void run() {
				for(VortexBlock vb : clear) {
					vb.remove();
				}
				mmo.getServer().getScheduler().cancelTask(id);
			}
		}.runTaskLater(mmo, time);
		
		return true;
	}

}
