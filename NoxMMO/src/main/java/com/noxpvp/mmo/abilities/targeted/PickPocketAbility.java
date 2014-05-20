package com.noxpvp.mmo.abilities.targeted;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class PickPocketAbility extends BaseTargetedPlayerAbility {
	
	public static final String PERM_NODE = "pick-pocket";
	public static final String ABILITY_NAME = "Pick Pocket";
	
	private float chance = 15;
	private float calChance = 5;
	private int pocketamount = 1;
	private boolean takeFullStack = false;
	
	/**
	 * 
	 * 
	 * @return double The currently set chance for pick pocket success
	 */
	public float getChance() {return chance;}
	
	public float getCalChance() {return calChance;}
	
	/**
	 * 
	 * 
	 * @param chance The double chance this ability has for a successful pick pocket
	 * should be between 0.00 and 100.00
	 * @return
	 */
	public PickPocketAbility setChance(float chance) {this.chance = chance; this.calChance = chance / 100; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer The amount of an item stack to take if pick is successful
	 * (Will always return 64 if TakeFullStack() ha been set to true)
	 */
	public int getPocketamount() {
		if (isTakeFullStack())
			return 64;
		else 
			return this.pocketamount;
	}
	
	/**
	 * 
	 * 
	 * @param pocketamount Integer amount that the player should take from the item stack if pick is successful
	 * (Will always be overridden with 64 if TakeFullStack() has been set to true)
	 * @return PickPocketAbility This instance, used for chaining
	 */
	public PickPocketAbility setPocketamount(int pocketamount){this.pocketamount = pocketamount; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean If pocketamount will always be overriden with 64
	 */
	public boolean isTakeFullStack() {return takeFullStack;}
	
	/**
	 * 
	 * 
	 * @param takeFullStack Boolean if s successful pick will always take the full amount of the item (Else falls back onto the setPocketAmount() )
	 * @return PickPocketAbility This instance, used for chaining
	 */
	public PickPocketAbility setTakeFullStack(boolean takeFullStack) {this.takeFullStack = takeFullStack; return this;}
	
	/**
	 * 
	 * 
	 * @param player The Player type user for this ability instance
	 * (The PickPocket)
	 */
	public PickPocketAbility(Player player){
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity target = getTarget();
		
		if (!(target instanceof InventoryHolder)) return false;
		
		final Player p = getPlayer();
		
		double tYaw = target.getLocation().getYaw();
		double pYaw = p.getLocation().getYaw();
		
		if (!(pYaw <= (tYaw + 20)) && (pYaw >= (tYaw - 20)))//must be behind target
			return false;
		
		if (!(p.isSneaking()))//and sneaking
			return false;
		
//		if (Math.random() > getCalChance())//chance to pick
//			return false;
		
		Inventory inv = ((InventoryHolder) target).getInventory();//get target inventory
		int runs = 0;//limit for trying to find an itemstack below
		
		ItemStack item = null;
		while (runs < 200) {
			int i = RandomUtils.nextInt(((35 - 9) + 1)) + 9;
			
			if ((item = inv.getItem(i)) == null || item.getType() == Material.AIR){
				runs++;
				continue;
			} else break;
		}
		
		if (item == null || item.getType() == Material.AIR)
			return false;
		
		item.setAmount(getPocketamount());//set itemstack amount
		
		inv.removeItem(item);//take it from target
		p.getInventory().addItem(item);//give it to player
		
		CommonUtil.nextTick(new Runnable() {
			public void run() { p.updateInventory(); }
		});
		
		return true;
	}
	
}
