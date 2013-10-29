package com.noxpvp.mmo.abilities.player;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PickPocketAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Pick Pocket";
	private Player target;
	private double chance = 15;
	private int pocketamount = 1;
	private boolean takeFullStack = false;
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Player - The currently set target for this ability instance
	 */
	public Player getTarget() {return target;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param target - the Player type target that this ability should use
	 * @return PickPocketAbility - This instance, used for chaining
	 */
	public PickPocketAbility setTarget(Player target) {this.target = target; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return double - The currently set chance for pick pocket success
	 */
	public double getChance() {return chance;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param chance - The double chance this ability has for a successful pick pocket
	 * should be between 0.00 and 100.00
	 * @return
	 */
	public PickPocketAbility setChance(double chance) {this.chance = chance; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Integer - The amount of an item stack to take if pick is successful
	 * (Will always return 64 if TakeFullStack() ha been set to true)
	 */
	public int getPocketamount() {
		if (isTakeFullStack())
			return 64;
		else 
			return this.pocketamount;
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param pocketamount - Integer amount that the player should take from the item stack if pick is successful
	 * (Will always be overridden with 64 if TakeFullStack() has been set to true)
	 * @return PickPocketAbility - This instance, used for chaining
	 */
	public PickPocketAbility setPocketamount(int pocketamount){this.pocketamount = pocketamount; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If pocketamount will always be overriden with 64
	 */
	public boolean isTakeFullStack() {return takeFullStack;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param takeFullStack - Boolean if s successful pick will always take the full amount of the item
	 * (Else falls back onto the setPocketAmount() )
	 * @return PickPocketAbility - This instance, used for chaining
	 */
	public PickPocketAbility setTakeFullStack(boolean takeFullStack) {this.takeFullStack = takeFullStack; return this;}
	
	/**
	 * @author Connor Stone
	 * 
	 * @param player - The Player type user for this ability instance
	 * (The PickPocket)
	 */
	public PickPocketAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player t = getTarget();
		Player p = getPlayer();
		
		double tYaw = t.getLocation().getYaw();
		double pYaw = p.getLocation().getYaw();
		
		if (!(pYaw <= (tYaw + 20)) && (pYaw >= (tYaw - 20)))//must be behind target
			return false;
		
		if (!(p.isSneaking()))//and sneaking
			return false;
		
		if (Math.random() * 100 > getChance())//chance to pick
			return false;
		
		Inventory inv = getTarget().getInventory();//get target inventory
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
	 * @author Connor Stone
	 * 
	 * @return Boolean - If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}
	
}
