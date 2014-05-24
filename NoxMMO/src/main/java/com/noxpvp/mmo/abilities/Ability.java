package com.noxpvp.mmo.abilities;

import java.util.List;

import org.bukkit.ChatColor;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;

/**
 * <b>IF this is not a dynamic ability. <br/> With changing names. Please specify a static getName() method. </b>
 *
 * @author Chris
 */
public interface Ability {

	/**
	 * Retrieves the ability name.
	 *
	 * @return Ability Name
	 */
	public String getName();
	
	public String getUniqueId();

	/**
	 * Retrieves the description of the ability.
	 * <p> Mainly used for command information purpose.
	 *
	 * @return description of the ability.
	 */
	public String getDescription();

	/**
	 * Retrieves the description of the ability for use as itemmeta.
	 * <p> Mainly used for itemmeta and other uses of List<String>.
	 *
	 * @return Description of the ability;
	 */
	public List<String> getLore(int lineLength);

	/**
	 * Retrieves the description of the ability for use as itemmeta in the specified color.
	 * <p> Mainly used for itemmeta and other uses of List<String>.
	 *
	 * @return Description of the ability;
	 */
	public List<String> getLore(ChatColor color, int lineLength);

	/**
	 * Executes an ability
	 *
	 * @return true if successful and false if not.
	 */
	public AbilityResult execute();

	/**
	 * Retrieves the display name locale of this ability.
	 *
	 * @return Colored String Value
	 */
	public String getDisplayName();

	/**
	 * The same as {@link #getDisplayName()} but prefixed with a specific color
	 *
	 * @param color
	 * @return
	 */
	public String getDisplayName(ChatColor color);

	/**
	 * Tells whether or not an ability should be allowed to be executed.
	 *
	 * @return true if allowed and false if not.
	 */
	public boolean mayExecute();

	/**
	 * Returns the result of this ability when its is thrown
	 *
	 * @return {@link Boolean} cancelled
	 */
	public boolean isCancelled();
}
