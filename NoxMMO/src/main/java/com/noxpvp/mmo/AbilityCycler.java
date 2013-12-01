package com.noxpvp.mmo;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.utils.Cycler;
import com.noxpvp.mmo.abilities.Ability;

public class AbilityCycler extends Cycler<Ability> {
	private Reference<MMOPlayer> player;
	protected final String player_name;
	private ItemStack cycleItem;
	
	public AbilityCycler setCycleItem(ItemStack item)
	{
		cycleItem = item.clone();
		cycleItem.setAmount(1);
		cycleItem.setDurability((short)0);
		
		return this;
	}
	
	public ItemStack getCycleItem(){ return cycleItem; }
	
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
		return stackToCheck.isSimilar(getCycleItem());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cycleItem == null) ? 0 : cycleItem.hashCode());
		result = prime * result + ((player_name == null) ? 0 : player_name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbilityCycler))
			return false;
		
		AbilityCycler other = (AbilityCycler) obj;
		if (cycleItem == null) {
			if (other.cycleItem != null)
				return false;
		} else if (!cycleItem.equals(other.cycleItem))
			return false;
		if (player_name == null) {
			if (other.player_name != null)
				return false;
		} else if (!player_name.equals(other.player_name))
			return false;
		return true;
	}
}
