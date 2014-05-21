package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.comphenix.packetwrapper.WrapperPlayServerAttachEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

/**
 * @author NoxPVP
 *
 */
public class HookShotPlayerAbility extends BasePlayerAbility{
	
	//TODO make this;
	
	public static final String ABILITY_NAME = "Hook Shot";
	public static final String PERM_NODE = "hook-shot";
	
	private int batId = NoxPacketUtil.getNewEntityId(1);
	private ItemStack pullRegent = new ItemStack(Material.STRING, 1);
	private ItemStack shootRegent = new ItemStack(Material.ARROW, 1);
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<PlayerInteractEvent> pullHandler;
	
	private double maxDistance;
	private int blockTime;
	private Material holdingBlockType;
	private Arrow arrow;
	private boolean active = false;
	
	private void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;
		
		if (changed)
			if (active) {
				registerHandler(hitHandler);
				registerHandler(pullHandler);
			} else {
				unregisterHandler(hitHandler);
				unregisterHandler(pullHandler);
			}
	}

	/**
	 * 
	 * 
	 * @return Integer Max distance set for ability execution <br/> returns null is setMaxDistance() has not been used
	 */
	public double getMaxDistance() {return maxDistance;}
	
	/**
	 * 
	 * 
	 * @param maxDistance The max distance a player can hook to
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setMaxDistance(double maxDistance) {this.maxDistance = maxDistance; return this;}

	/**
	 * 
	 * 
	 * @return Material Type of block used to support player
	 */
	public Material getHoldingBlockType() {return holdingBlockType;}
	
	/**
	 * 
	 * 
	 * @param block Material type that should be used to support the player
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setHoldingBlockType(Material block) {this.holdingBlockType = block; return this;}

	/**
	 * 
	 * 
	 * @return Integer Amount of seconds the supporting block will last before removal
	 */
	public int getBlockTime() {return blockTime;}
	
	/**
	 * 
	 * @param blockTime Time before the supporting block is removed
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setBlockTime(int blockTime) {this.blockTime = blockTime * 20; return this;}

	/**
	 * 
	 * @param player Player used for the ability
	 */
	public HookShotPlayerAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.maxDistance = 75;
		this.blockTime = 20 * 6;
		this.holdingBlockType = Material.GLASS;
		
		this.hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL,
				1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<ProjectileHitEvent> getEventType() {
				return ProjectileHitEvent.class;
			}
			
			public String getEventName() {
				return "ProjectileHitEvent";
			}
			
			public void execute(ProjectileHitEvent event) {
				if (!active) {
					unregisterHandler(this);
				}
				
				if (arrow.equals(event.getEntity()))
					return;
				
				Player p;
				
				if ((p = HookShotPlayerAbility.this.getPlayer()) == null)
					return;
				
				WrapperPlayServerAttachEntity rope = new WrapperPlayServerAttachEntity();
				WrapperPlayServerSpawnEntityLiving holder = new WrapperPlayServerSpawnEntityLiving();
				
				WrappedDataWatcher dw = new WrappedDataWatcher();
				dw.setObject(0, (byte) 0);
				dw.setObject(6, (float) 1);
				dw.setObject(12, 0);
				
				holder.setEntityID(batId);
				holder.setType(EntityType.BAT);
				holder.setMetadata(dw);
				
				Location loc = arrow.getLocation();
				holder.setX(loc.getX());
				holder.setY(loc.getY());
				holder.setZ(loc.getZ());
				
				rope.setLeached(true);
				rope.setEntityId(batId);
				rope.setVehicleId(p.getEntityId());
				
				holder.sendPacket(p);
				rope.sendPacket(p);
				
			}
		};
		
		this.pullHandler = new BaseMMOEventHandler<PlayerInteractEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("PlayerInteractEvent").toString(),
				EventPriority.NORMAL,
				1) {

					public boolean ignoreCancelled() {
						return true;
					}

					public void execute(PlayerInteractEvent event) {
						if (!active){
							unregisterHandler(this);
						}
						
						Block hBlock = arrow.getLocation().getBlock();
						Player p = getPlayer();
						Inventory inv = p.getInventory();
						
						
						if (!PlayerUtils.hasAtleast(inv, pullRegent, pullRegent.getAmount()))
							return;
						if (!LineOfSightUtil.hasLineOfSight(p, arrow.getLocation(), Material.AIR))
							return;
						if (hBlock.getType() != Material.AIR || hBlock.getRelative(0, 1, 0).getType() != Material.AIR || hBlock.getRelative(0, 2, 0).getType() != Material.AIR){
							unregisterHandler(this);
							arrow.remove();
							return;
						}
						
						inv.removeItem(pullRegent);
						
						hBlock.setType(holdingBlockType);
						BlockTimerRunnable remover = new BlockTimerRunnable(hBlock, Material.AIR, holdingBlockType);
						remover.runTaskLater(NoxMMO.getInstance(), blockTime);
						
						p.teleport(hBlock.getRelative(0, 1, 0).getLocation(), TeleportCause.PLUGIN);
						arrow.remove();
						setActive(false);
						
					}

					public Class<PlayerInteractEvent> getEventType() {
						return PlayerInteractEvent.class;
					}

					public String getEventName() {
						return "PlayerInteractEvent";
					}
		};
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		Inventory inv = p.getInventory();
		
		if (!PlayerUtils.hasAtleast(inv, shootRegent, shootRegent.getAmount()))
			return false;
		
		inv.removeItem(shootRegent);
		
		this.arrow = p.launchProjectile(Arrow.class);
		arrow.setVelocity(p.getLocation().getDirection().multiply(2));
		
		setActive(true);
		return true;
	}

}