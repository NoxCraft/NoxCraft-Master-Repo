package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.PassiveAbility;

public class PlayerManager implements Persistant {
	private WeakHashMap<String, MMOPlayer> players;
	
	private Map<String, Class<? extends Ability>> abilityClasses = new HashMap<String, Class<? extends Ability>>();
	
	public PlayerManager()
	{
		players = new WeakHashMap<String, MMOPlayer>();
	}
	
	public MMOPlayer getMMOPlayer(final String name)
	{
		if (players.containsKey(name))
			return players.get(name);
		else
			return getMMOPlayer(getNoxPlayer(name));
	}
	
	public MMOPlayer getMMOPlayer(NoxPlayerAdapter adapter)
	{
		if (getNoxPlayer(adapter) == null) throw new IllegalArgumentException("Null player object given!");
		
		if (adapter instanceof MMOPlayer)
			return (MMOPlayer) adapter;
		
		final String name = getNoxPlayer(adapter).getName();
		if (players.containsKey(getNoxPlayer(adapter).getName()))
			return players.get(name);

		MMOPlayer player = new MMOPlayer(adapter);
		players.put(name, player);
		
		return player;
	}

	public MMOPlayer getMMOPlayer(OfflinePlayer player) {
		return getMMOPlayer(getNoxPlayer(player));
	}
	
	public Ability[] getAbilities(Player player) {
		return getMMOPlayer(player).getAbilities();
	}
	
	public Ability[] getAbilities(String playerName) {
		return getMMOPlayer(playerName).getAbilities();
	}
	
	public Ability getAbility(NoxPlayerAdapter player, String abilityName) {
		return getMMOPlayer(player).getAbility(abilityName);
	}
	
	public Ability getAbility(Player player, String abilityName) {
		return getAbility(getMMOPlayer(player), abilityName);
	}
	
	public Ability getAbility(String playerName, String abilityName) {
		return getAbility(getMMOPlayer(playerName), abilityName);
	}
	
	public Class<? extends Ability> getAbilityClass(String abilityName) {
		return abilityClasses.get(abilityName);
	}
	
	public PassiveAbility[] getPassiveAbilities(Player player) {
		return getMMOPlayer(player).getPassiveAbilities();
	}
	
	public PassiveAbility[] getPassiveAbilities(String playerName) {
		return getMMOPlayer(playerName).getPassiveAbilities();
	}
	
	public Player getPlayer(NoxPlayerAdapter player) {
		return player.getNoxPlayer().getPlayer();
	}
	
	public Player getPlayer(String playerName) {
		return Bukkit.getPlayer(playerName);
	}
	
	public Ability[] getAbilities(NoxPlayerAdapter player) {
		return getMMOPlayer(player).getAbilities();
	}
	
	public PassiveAbility[] getPassiveAbilities(NoxPlayerAdapter player) {
		return getMMOPlayer(player).getPassiveAbilities();
	}	
	
	private static NoxPlayer getNoxPlayer(NoxPlayerAdapter adapter)
	{
		return adapter.getNoxPlayer();
	}
	
	private static NoxPlayer getNoxPlayer(String name) {
		return NoxCore.getInstance().getPlayerManager().getPlayer(name);
	}
	
	private static NoxPlayer getNoxPlayer(OfflinePlayer player)
	{
		return getNoxPlayer(player.getName());
	}

	public void save() {
		for (MMOPlayer player : players.values())
			player.save();
	}

	public void load() {
		// TODO Auto-generated method stub
	}
}
