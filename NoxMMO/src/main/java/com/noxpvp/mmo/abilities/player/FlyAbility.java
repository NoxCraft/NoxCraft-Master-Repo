package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.core.utils.InventoryUtils;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class FlyAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Fly";
	public static final String PERM_NODE = "fly";
	
	public static List<Player> flyers = new ArrayList<Player>();
	
	private ItemStack reg;
	private int regFreq;
	
	/**
	 * 
	 * @return ItemStack - The currently set Regent for this ability
	 */
	public ItemStack getReg() {return reg;}
	
	/**
	 * 
	 * @param reg - The ItemStack for this ability, including correct amount and type
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyAbility setReg(ItemStack reg) {this.reg = reg; return this;}
	
	/**
	 * 
	 * @return Integer - The amount of ticks to wait between regent collecting checks
	 */
	public int getRegFreq() {return regFreq;}
	
	/**
	 * 
	 * @param regFreq - The amount of ticks the ability should wait between collecting regents
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyAbility setRegFreq(int regFreq) {this.regFreq = regFreq; return this;}
	
	/**
	 * 
	 * @param player - This user for this ability instance
	 */
	public FlyAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.reg.setAmount(1);
		this.reg.setType(Material.FEATHER);
		this.regFreq = (1200);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		if (FlyAbility.flyers.contains(p)){
			FlyAbility.flyers.remove(p);
			p.setAllowFlight(false);
			p.setFlying(false);
			
			return true;
		}
		if (!p.getInventory().containsAtLeast(getReg(), getReg().getAmount())) return false;
		
		FlyAbility.flyers.add(p);
		p.setAllowFlight(true);
		p.setFlying(true);
		
		FlyRunnable regChecker = new FlyRunnable();
		regChecker.runTaskTimer(NoxMMO.getInstance(), 0, getRegFreq());
		
		return true;
	}
	
	private class FlyRunnable extends BukkitRunnable{
		private Player p;
		private Inventory i;
		
		public FlyRunnable(){
			this.p = getPlayer();
			this.i = p.getInventory();
		}
		
		public void safeCancel() {try { cancel(); } catch (IllegalStateException e) {}}
		
		public void run(){
			if (!InventoryUtils.hasItems(i, getReg())){
				p.setAllowFlight(false);
				p.setFlying(false);
				
				FlyAbility.flyers.remove(p);
				
				safeCancel();
				return;
			}else
			{
				i.removeItem(reg);
			}
		}
	}
}