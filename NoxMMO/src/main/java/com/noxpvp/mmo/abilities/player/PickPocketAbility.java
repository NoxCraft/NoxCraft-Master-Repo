package com.noxpvp.mmo.abilities.player;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PickPocketAbility extends BasePlayerAbility{
	
	public final static String PERM_NODE = "pick-pocket";
	private final static String ABILITY_NAME = "Pick Pocket";
	private Player target;
	private double chance = 15;
	private int pocketamount = 1;
	private boolean takeFullStack = false;
	
	public Player getTarget() {return target;}
	public PickPocketAbility setTarget(Player target) {this.target = target; return this;}
	
	public double getChance() {return chance;}
	public PickPocketAbility setChance(double chance) {this.chance = chance; return this;}
	
	public int getPocketamount() {
		if (isTakeFullStack())
			return 64;
		else 
			return this.pocketamount;
	}
	public PickPocketAbility setPocketamount(int pocketamount){this.pocketamount = pocketamount; return this;}
	
	public boolean isTakeFullStack() {return takeFullStack;}
	public PickPocketAbility setTakeFullStack(boolean takeFullStack) {this.takeFullStack = takeFullStack; return this;}
	
	public PickPocketAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
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
	
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}
	
}
