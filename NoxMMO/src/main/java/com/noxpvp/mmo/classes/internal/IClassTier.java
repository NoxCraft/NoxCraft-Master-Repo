package com.noxpvp.mmo.classes.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.core.annotation.Temporary;
import com.noxpvp.mmo.abilities.Ability;

public interface IClassTier {

	public int getTierLevel();

	public String getName();

	public boolean canUse();

	public PlayerClass getRetainingClass();

	public String getDisplayName();

	public void setDisplayName(String displayName);

	public List<String> getLore();

	public double getMaxHealth();

	public int getMaxExp();

	public int getMaxExp(int level);

	public int getNeededExp();

	public int getNeededExp(int level);

	public int getExp();

	public void setExp(int amount);

	public int getExp(int level);

	public int getTotalExp();

	public void setTotalExp(int amount);

	public int getLevel();

	public void setLevel(int level);

	public boolean canUseLevel(int level);

	public int getMaxLevel();

	public void addExp(int amount);

	public void removeExp(int amount);

	@Temporary
	public Map<String, Ability> getAbilityMap();

	@Temporary
	public Collection<Ability> getAbilities();

	public ExperienceType[] getExpTypes();

	public void onSave(ConfigurationNode node);

	public void onLoad(ConfigurationNode node);

	/**
	 * Used to cleanup data if max exp per level changes and such. <br/>
	 * This is a very intensive operation and must be used sparingly. <br/>
	 * <br/>
	 * This method should include usage of the setTotalExp(int value);
	 * <br/>
	 * This will allow auto fixing values when manually setting total exp. Thus cleanup can include that set.
	 */
	public void update();

	String getPermission();
}
