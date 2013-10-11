package com.noxpvp.mmo;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.PassiveAbility;

/**
 * The Interface PlayerAbilityManager.
 */
public interface PlayerAbilityManager {
	
	public final static String MAIN_PERM = "abilities";
	
	public MMOPlayer getMMOPlayer(OfflinePlayer player);
	public MMOPlayer getMMOPlayer(String playerName);
	
	/**
	 * Gets the player.
	 *
	 * @param player the player
	 * @return the player
	 */
	public Player getPlayer(NoxPlayer player);
	
	/**
	 * Gets the player.
	 *
	 * @param playerName the player name
	 * @return the player
	 */
	public Player getPlayer(String playerName);
	
	/**
	 * Gets the ability.
	 *
	 * @param player the player
	 * @param abilityName the ability name
	 * @return the ability
	 */
	public Ability getAbility(Player player, String abilityName);
	
	/**
	 * Gets the ability.
	 *
	 * @param player the player
	 * @param abilityName the ability name
	 * @return the ability
	 */
	public Ability getAbility(NoxPlayer player, String abilityName);
	
	/**
	 * Gets the ability.
	 *
	 * @param playerName the player name
	 * @param abilityName the ability name
	 * @return the ability
	 */
	public Ability getAbility(String playerName, String abilityName);
	
	/**
	 * Gets the abilities.
	 *
	 * @param player the player
	 * @return the abilities
	 */
	public Ability[] getAbilities(Player player);
	
	/**
	 * Gets the abilities.
	 *
	 * @param player the player
	 * @return the abilities
	 */
	public Ability[] getAbilities(NoxPlayer player);
	
	/**
	 * Gets the abilities.
	 *
	 * @param playerName the player name
	 * @return the abilities
	 */
	public Ability[] getAbilities(String playerName);
	
	/**
	 * Gets the passive abilities.
	 *
	 * @param player the player
	 * @return the passive abilities
	 */
	public PassiveAbility[] getPassiveAbilities(Player player);
	
	/**
	 * Gets the passive abilities.
	 *
	 * @param player the player
	 * @return the passive abilities
	 */
	public PassiveAbility[] getPassiveAbilities(NoxPlayer player);
	
	/**
	 * Gets the passive abilities.
	 *
	 * @param playerName the player name
	 * @return the passive abilities
	 */
	public PassiveAbility[] getPassiveAbilities(String playerName);

	/**
	 * Gets the ability class.
	 *
	 * @param abilityName the ability name
	 * @return the ability class
	 */
	public Class<? extends Ability> getAbilityClass(String abilityName);
}
