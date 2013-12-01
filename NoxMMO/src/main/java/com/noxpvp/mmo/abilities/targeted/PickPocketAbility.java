package com.noxpvp.mmo.abilities.targeted;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class PickPocketAbility extends BaseTargetedPlayerAbility{
	
	public static final String PERM_NODE = "pick-pocket";
	public static final String ABILITY_NAME = "Pick Pocket";
	
	private float chance = 15;
	private float calChance;
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
		super(ABILITY_NAME, player, NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player).getTarget());
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		LivingEntity target = getTarget();
		
		if (!(target instanceof InventoryHolder)) return false;
		
		Player p = getPlayer();
		
		double tYaw = target.getLocation().getYaw();
		double pYaw = p.getLocation().getYaw();
		
		if (!(pYaw <= (tYaw + 20)) && (pYaw >= (tYaw - 20)))//must be behind target
			return false;
		
		if (!(p.isSneaking()))//and sneaking
			return false;
		
		if (Math.random() > getCalChance())//chance to pick
			return false;
		
		Inventory inv = ((InventoryHolder) target).getInventory();//get target inventory
		int placeHolder = 0;
		int runs = 0;//limit for trying to find an itemstack below
		
		while (placeHolder == 0 && (runs < 200)){
			int i = RandomUtils.nextInt(inv.getSize());
			
			if (inv.getItem(i) == null){
				runs++;
				continue;
			}
			
			placeHolder = i;
			break;
		}

		if (placeHolder == 0)//be sure it found an item
			return false;
		
		ItemStack item = inv.getItem(placeHolder); //store item stack
		item.setAmount(getPocketamount());//set itemstack amount
		
		p.getInventory().addItem(item);//give it to player
		inv.removeItem(item);//take it from target
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}
	
}
