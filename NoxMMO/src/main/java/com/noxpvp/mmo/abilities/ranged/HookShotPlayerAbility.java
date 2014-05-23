package com.noxpvp.mmo.abilities.ranged;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.runnables.BlockTimerRunnable;

/**
 * @author NoxPVP
 */
public class HookShotPlayerAbility extends BaseRangedPlayerAbility {

	//TODO make this;

	public static final String ABILITY_NAME = "Hook Shot";
	public static final String PERM_NODE = "hook-shot";

	private ItemStack pullRegent = new ItemStack(Material.STRING, 1);
	private ItemStack shootRegent = new ItemStack(Material.ARROW, 1);

	private BaseMMOEventHandler<ProjectileHitEvent> hitHandler;

	private int blockTime;
	private Material holdingBlockType;
	private int batId;
	private Arrow arrow;
	private boolean active = false;

	/**
	 * @param player Player used for the ability
	 */
	public HookShotPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		setRange(50);
		this.blockTime = 20 * 6;
		this.holdingBlockType = Material.GLASS;
		this.batId = NoxPacketUtil.getNewEntityId(1);

		this.hitHandler = new BaseMMOEventHandler<ProjectileHitEvent>(
				new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("ProjectileHitEvent").toString(),
				EventPriority.NORMAL, 1) {

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
					return;
				}

				if (!arrow.equals(event.getEntity()))
					return;

				Player p;

				if ((p = HookShotPlayerAbility.this.getPlayer()) == null)
					return;

				NoxPacketUtil.spawnFakeEntity(EntityType.BAT, batId, arrow.getLocation().add(0, -1, 0), true);
				NoxPacketUtil.spawnRope(p.getEntityId(), batId);

			}
		};
	}

	private void setActive(boolean active) {
		boolean changed = this.active != active;
		this.active = active;

		if (changed)
			if (active) {
				registerHandler(hitHandler);
			} else {
				unregisterHandler(hitHandler);
			}
	}

	/**
	 * @return Material Type of block used to support player
	 */
	public Material getHoldingBlockType() {
		return holdingBlockType;
	}

	/**
	 * @param block Material type that should be used to support the player
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setHoldingBlockType(Material block) {
		this.holdingBlockType = block;
		return this;
	}

	/**
	 * @return Integer Amount of seconds the supporting block will last before removal
	 */
	public int getBlockTime() {
		return blockTime;
	}

	/**
	 * @param blockTime Time before the supporting block is removed
	 * @return HookShotAbility This instance used for chaining
	 */
	public HookShotPlayerAbility setBlockTime(int blockTime) {
		this.blockTime = blockTime;
		return this;
	}

	public boolean execute() {
//		if (!mayExecute())
//			return false;

		if (active && arrow != null && arrow.isOnGround()) {
			return eventExecute();
		} else if (!active) {	
			Player p = getPlayer();
			PlayerInventory inv = p.getInventory();

			if (!inv.containsAtLeast(shootRegent, shootRegent.getAmount())) {
				MessageUtil.sendLocale(p, MMOLocale.ABIL_NOT_ENOUGH_REGENT, Integer.toString(shootRegent.getAmount()), shootRegent.getType().name().toLowerCase());
				return false;
			}

			inv.removeItem(shootRegent);
			p.updateInventory();

			this.arrow = p.launchProjectile(Arrow.class);
			arrow.setVelocity(p.getLocation().getDirection().multiply(2));
			setActive(true);
			
			return true;
		}
		
		return false;
	}
	
	private boolean eventExecute() {
		Block hBlock = arrow.getLocation().getBlock();
		Player p = getPlayer();
		Inventory inv = p.getInventory();
		NoxPacketUtil.removeEntity(batId);


		if (hBlock.getType() != Material.AIR || hBlock.getRelative(0, 1, 0).getType() != Material.AIR || hBlock.getRelative(0, 2, 0).getType() != Material.AIR) {
			arrow.remove();
			setActive(false);
			return false;
		}

		if (getDistance() > getRange()) {
			MessageUtil.sendLocale(p, MMOLocale.ABIL_RANGED_TOO_FAR, Double.toString(getRange()));
			arrow.remove();
			setActive(false);
			return false;
		}

		if (!hasLOS()) {
			MessageUtil.sendLocale(p, MMOLocale.ABIL_NO_LOS, getName());
			arrow.remove();
			setActive(false);
			return false;
		}

		if (!inv.containsAtLeast(pullRegent, pullRegent.getAmount())) {
			MessageUtil.sendLocale(p, MMOLocale.ABIL_NOT_ENOUGH_REGENT,
					Integer.toString(pullRegent.getAmount()), pullRegent.getType().name().toLowerCase());
			arrow.remove();
			setActive(false);
			return false;
		}

		inv.removeItem(pullRegent);
		p.updateInventory();

		hBlock.setType(holdingBlockType);
		new BlockTimerRunnable(hBlock, Material.AIR, holdingBlockType).runTaskLater(NoxMMO.getInstance(), blockTime);

		p.teleport(hBlock.getRelative(0, 1, 0).getLocation().add(.50, .05, .50), TeleportCause.PLUGIN);
		arrow.remove();

		setActive(false);
		return true;
	}
	
	public double getDistance() {
		double distance = getPlayer().getLocation().distance(arrow.getLocation());
		
		return distance;
	}
	
	public boolean hasLOS() {
		Location loc = getPlayer().getLocation();
		loc.add(loc.getDirection().setY(0).normalize());
		
		return LineOfSightUtil.getTargetBlock(loc, (int) Math.floor(getDistance()), Material.AIR) == null;
	}

}