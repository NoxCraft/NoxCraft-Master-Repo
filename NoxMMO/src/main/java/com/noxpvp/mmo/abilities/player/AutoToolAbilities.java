package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class AutoToolAbilities {

	public static class AutoSword extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent>{
		
		public final static String ABILITY_NAME = "Auto Sword";
		public final static String PERM_NODE = "auto-sword";

		public AutoSword(Player player) {
			super(ABILITY_NAME, player);
		}
		
		public boolean execute(EntityDamageByEntityEvent event) {
			if (!mayExecute())
				return false;
			
			Player p = getPlayer();
			Entity e = event.getEntity();
			
			MMOPlayer mmoPlayer = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(p);
			
			int fireTicks = 50;
			
			PlayerClass clazz = mmoPlayer.getMainPlayerClass();
			
			fireTicks = (int) (((mmoPlayer != null) && clazz != null)? clazz.getTotalLevels() / 2.5 : fireTicks);
			
			e.setFireTicks(fireTicks);
			
			return true;
		}
		
		public boolean execute() {
			return true;
		}
	}
	
	public static class AutoTool extends BasePlayerAbility implements PassiveAbility<BlockBreakEvent> {
		
		public final static String ABILITY_NAME = "Auto Tool";
		public final static String PERM_NODE = "auto-tool";
		
		public AutoTool(Player player) {
			super(ABILITY_NAME, player);
		}
		
		private void breakBlockBurned(Block block, Material type, int amount, short data) {
			block.setType(Material.AIR);
			block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(type, amount, data));
		}
		
		public boolean execute() {
			return true;
		}
		
		public boolean execute(BlockBreakEvent event) {
			if (!mayExecute())
				return false;
			
			Block block = event.getBlock();
			Material tool = getPlayer().getItemInHand().getType();
			
			switch (tool) {
			
			case GOLD_PICKAXE:
				switch (block.getType()) {
				case IRON_ORE:
					event.setCancelled(true);
					breakBlockBurned(block, Material.IRON_INGOT, 1, (short) 0);
					
					break;
				case GOLD_ORE:
					event.setCancelled(true);
					breakBlockBurned(block, Material.GOLD_INGOT, 1, (short) 0);
					
					break;
				case COBBLESTONE:
					event.setCancelled(true);
					breakBlockBurned(block, Material.STONE, 1, (short) 0);
					
					break;
				case NETHERRACK:
					event.setCancelled(true);
					breakBlockBurned(block, Material.NETHER_BRICK_ITEM, 1, (short) 0);
					
					break;
				default:
					return false;
					
				}
				
				break;
			case GOLD_AXE:
				switch (block.getType()) {
				
				case LOG:
					event.setCancelled(true);
					breakBlockBurned(block, Material.COAL, 1, (short) 0);
					
					break;
				case CACTUS:
					event.setCancelled(true);
					breakBlockBurned(block, Material.INK_SACK, 1, (short) 2);
					
					break;
				default:
					return false;
					
				}
					
				break;
			case GOLD_SPADE:
				switch (block.getType()) {
				
				case SAND:
					event.setCancelled(true);
					breakBlockBurned(block, Material.GLASS, 1, (short) 0);
					
					break;
				case CLAY:
					event.setCancelled(true);
					breakBlockBurned(block, Material.CLAY_BRICK, 3, (short) 0);
					
					break;
				default:
					return false;
					
				}
				
				break;
			default:
				return false;
			}
			
			return true;
		}
	}
	
	public static class AutoArmor extends BasePlayerAbility implements PassiveAbility<EntityDamageEvent> {
		
		public final static String ABILITY_NAME = "Auto Armor";
		public final static String PERM_NODE = "auto-armor";
		
		public AutoArmor(Player player) {
			super(ABILITY_NAME, player);
		}

		public boolean execute(EntityDamageEvent event) {
			return execute();
		}
		
		public boolean execute() {
			Player p = getPlayer();
			
			if (p.getEquipment().getHelmet().getType() != Material.GOLD_HELMET)
				return false;
						
			p.setRemainingAir(p.getMaximumAir());
			
			return true;
		}
	}

}
