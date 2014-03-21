package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.noxpvp.core.utils.PlayerUtils;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

/**
 * @author NoxPVP
 *
 */
public class HookShotAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "hookshot";
	public static final String ABILITY_NAME = "Hook Shot";
	public static int batId = Short.MAX_VALUE + 1000;
	
	private ItemStack pullRegent = new ItemStack(Material.STRING, 1);
	private ItemStack shootRegent = new ItemStack(Material.ARROW, 1);
	
	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;
	private BaseMMOEventHandler<PlayerInteractEvent> pullHandler;
	
	private double maxDistance;
	private int blockTime;
	private Material holdingBlockType;
	private Arrow arrow;

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
	public HookShotAbility setMaxDistance(double maxDistance) {this.maxDistance = maxDistance; return this;}

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
	public HookShotAbility setHoldingBlockType(Material block) {this.holdingBlockType = block; return this;}

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
	public HookShotAbility setBlockTime(int blockTime) {this.blockTime = blockTime * 20; return this;}

	/**
	 * 
	 * @param player Player used for the ability
	 */
	public HookShotAbility(Player player){
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
				if (!isActive()){
					NoxMMO.getInstance().getMasterListener().unregisterHandler(this);
				}
				
				if (arrow != event.getEntity())
					return;
				
				Player p;
				Arrow arrow;
				
				if ((p = HookShotAbility.this.getPlayer()) == null || !p.isValid())
					return;
				
				arrow = (Arrow) event.getEntity();
				CommonPacket rope = new CommonPacket(PacketType.OUT_ENTITY_ATTACH),
						holder = new CommonPacket(PacketType.OUT_ENTITY_SPAWN_LIVING);
				
				DataWatcher dw = new DataWatcher();
				dw.set(0, (byte) 0x20);
				dw.set(6, (float) 2);
				dw.set(12, 1);
				
				if (batId > (Short.MAX_VALUE + 10000))
					batId = Short.MAX_VALUE + 1000;
				
				int id = batId++;
				
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityId, id);
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityType, 65);
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.x, batId);
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.x, batId);
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.x, batId);
				holder.write(PacketType.OUT_ENTITY_SPAWN_LIVING.dataWatcher, dw);
				
				rope.write(PacketType.OUT_ENTITY_ATTACH.lead, 1);
				rope.write(PacketType.OUT_ENTITY_ATTACH.passengerId, id);
				rope.write(PacketType.OUT_ENTITY_ATTACH.vehicleId, p.getEntityId());
				
				
				PacketUtil.broadcastPacketNearby(p.getLocation(), 100, holder);
				PacketUtil.broadcastPacketNearby(p.getLocation(), 100, rope);
				
			}
		};
		
		this.pullHandler = new BaseMMOEventHandler<PlayerInteractEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("PlayerInteractEvent").toString(),
				EventPriority.NORMAL,
				1) {

					public boolean ignoreCancelled() {
						return false;
					}

					public void execute(PlayerInteractEvent event) {
						if (!isActive()){
							NoxMMO.getInstance().getMasterListener().unregisterHandler(this);
						}
						
						Block hBlock = arrow.getLocation().getBlock();
						Player p = getPlayer();
						Inventory inv = p.getInventory();
						
						
						if (!PlayerUtils.hasAtleast(inv, pullRegent, pullRegent.getAmount()))
							return;
						if (!p.hasLineOfSight(arrow))
							return;
						if (hBlock.getType() != Material.AIR || hBlock.getRelative(0, 1, 0).getType() != Material.AIR || hBlock.getRelative(0, 2, 0).getType() != Material.AIR){
							NoxMMO.getInstance().getMasterListener().unregisterHandler(this);
							arrow.remove();
							return;
						}
						
						inv.removeItem(pullRegent);
						
						hBlock.setType(holdingBlockType);
						BlockTimerRunnable remover = new BlockTimerRunnable(hBlock, Material.AIR, holdingBlockType);
						remover.runTaskLater(NoxMMO.getInstance(), blockTime);
						
						p.teleport(hBlock.getRelative(0, 1, 0).getLocation(), TeleportCause.PLUGIN);
						arrow.remove();
						
						
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
		arrow.setBounce(true);
		arrow.setVelocity(p.getLocation().getDirection());
		
		NoxMMO.getInstance().getMasterListener().registerHandler(hitHandler);
		
		return true;
	}
	
	private boolean isActive(){
		if (!mayExecute() || !getPlayer().isValid())
			return false;
		
		return true;
	}

}