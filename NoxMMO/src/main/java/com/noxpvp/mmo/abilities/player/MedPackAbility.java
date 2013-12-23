package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class MedPackAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Med Pack";
	public final static String PERM_NODE = "med-pack";
	
	public static Map<Item, String> packs = new HashMap<Item, String>();
	
	private int health;

	public int getHealth() {return health;}

	public MedPackAbility setHealth(int health) {
		this.health = health; return this;}

	public MedPackAbility(Player player) {
		super(ABILITY_NAME, player);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		ItemStack pack = new ItemStack(Material.EMERALD, 1);
		pack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		Item drop = p.getWorld().dropItem(p.getLocation(), pack);
		drop.setPickupDelay(30);
		
		MedPackAbility.packs.put(drop, p.getName());
		
		return false;
	}

}
