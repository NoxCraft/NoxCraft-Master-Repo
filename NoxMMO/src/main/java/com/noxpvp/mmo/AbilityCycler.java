package com.noxpvp.mmo;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.utils.Cycler;
import com.noxpvp.mmo.abilities.Ability;

public class AbilityCycler extends Cycler<Ability> {
	private Reference<MMOPlayer> player;
	protected final String player_name;
	private ItemStack cycleItem;
	ItemMeta cycleItemMeta;
	
	private static boolean checkMeta = true, checkDisplayName = true, checkEnchants = true, checkLore = true;
	
	/**
	 * @return the checkMeta
	 */
	public static synchronized final boolean isCheckMeta() {
		return checkMeta;
	}

	/**
	 * @return the checkDisplayName
	 */
	public static synchronized final boolean isCheckDisplayName() {
		return checkDisplayName;
	}

	/**
	 * @return the checkEnchants
	 */
	public static synchronized final boolean isCheckEnchants() {
		return checkEnchants;
	}

	/**
	 * @return the checkLore
	 */
	public static synchronized final boolean isCheckLore() {
		return checkLore;
	}

	/**
	 * @param checkMeta the checkMeta to set
	 */
	public static synchronized final void setCheckMeta(boolean checkMeta) {
		AbilityCycler.checkMeta = checkMeta;
	}

	/**
	 * @param checkDisplayName the checkDisplayName to set
	 */
	public static synchronized final void setCheckDisplayName(boolean checkDisplayName) {
		AbilityCycler.checkDisplayName = checkDisplayName;
	}

	/**
	 * @param checkEnchants the checkEnchants to set
	 */
	public static synchronized final void setCheckEnchants(boolean checkEnchants) {
		AbilityCycler.checkEnchants = checkEnchants;
	}

	/**
	 * @param checkLore the checkLore to set
	 */
	public static synchronized final void setCheckLore(boolean checkLore) {
		AbilityCycler.checkLore = checkLore;
	}

	public AbilityCycler setCycleMetaAndItem(ItemStack item)
	{
		return setCycleMetaAndItem(item, item.getItemMeta());
	}
	
	public AbilityCycler setCycleMetaAndItem(ItemStack item, ItemMeta itemMeta)
	{
		cycleItemMeta = itemMeta;
		cycleItem = item;
		return this;
	}
	
	public AbilityCycler setCycleItem(ItemStack item)
	{
		cycleItem = item;
		return this;
	}
	
	public ItemStack getCycleItem(){ return cycleItem; }
	public ItemMeta getCycleItemMeta() { return cycleItemMeta; }
	
	public MMOPlayer getMMOPlayer() { return (player==null)? null: player.get(); }
	public final Player getPlayer() { return Bukkit.getPlayer(player_name); }
	public final String getPlayerName() { return player_name; }
	
	public AbilityCycler(NoxPlayerAdapter adapter, Collection<Ability> data) {
		super(data);
		this.player = new SoftReference<MMOPlayer>(fetchMMOPlayer(adapter));
		this.player_name = ((getMMOPlayer()!=null)?getMMOPlayer().getPlayerName():null);
		if (this.player_name == null)
			throw new IllegalArgumentException("The player adapter is invalid and does not have a name. OR is non existant.");
		
	}
	
	public AbilityCycler(NoxPlayerAdapter adapter, int size)
	{
		super(size);
		this.player = new SoftReference<MMOPlayer>(fetchMMOPlayer(adapter));
		this.player_name = ((getMMOPlayer()!=null)?getMMOPlayer().getPlayerName():null);
		if (this.player_name == null)
			throw new IllegalArgumentException("The player adapter is invalid and does not have a name. OR is non existant.");

	}
	
	private MMOPlayer fetchMMOPlayer(NoxPlayerAdapter adapter) {
		return NoxMMO.getInstance().getPlayerManager().getMMOPlayer(adapter);
	}
	
	public boolean isCycleItem(ItemStack stackToCheck)
	{
		ItemStack s = stackToCheck;
		
		if (!s.getType().equals(getCycleItem().getType()))
			return false;
		if (!checkMeta)
			return true;
		ItemMeta sm = s.getItemMeta();
		
		ItemMeta cim = getCycleItemMeta();
		
		if ((checkDisplayName && sm.hasDisplayName() != cim.hasDisplayName()) || //I normally don't format this way but it looks neater.
				(checkEnchants && sm.hasEnchants() != cim.hasEnchants() ||
				(checkMeta && sm.hasLore() != cim.hasLore()))
			)
			return false;
		
		
				
		if (sm.hasEnchants() && checkEnchants)
		{
			if (sm.getEnchants().size() != cim.getEnchants().size())
				return false;
			for (Entry<Enchantment, Integer> entry : cim.getEnchants().entrySet())
				if (!(sm.hasEnchant(entry.getKey()) && sm.getEnchantLevel(entry.getKey()) == entry.getValue()))
					return false;
		}
		
		if (checkDisplayName && sm.hasDisplayName() && !sm.getDisplayName().equals(cim.getDisplayName()))
			return false;
			
		if (sm.hasLore() && checkLore && sm.getLore().size() != cim.getLore().size())
			return false;
			
		return true;
	}
}
