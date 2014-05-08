package com.noxpvp.mmo.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.mmo.classes.internal.ClassType;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.classes.tiers.*;

public class AxesPlayerClass extends PlayerClass {
	
	public static final String className = "Axes";
	
	public static final String uniqueID = "f8c26f34-fc36-427a-b92e-94090b146db1";	//RANDOMLY GENERATED DO NOT CHANGE!

	public AxesPlayerClass(Player player) {
		super(uniqueID, className, player);
		tiers.put(1, new AxesBasherClassTier(this));
		tiers.put(2, new AxesChampionClassTier(this));
		tiers.put(3, new AxesBerserkerClassTier(this));
		tiers.put(4, new AxesWarlordClassTier(this));
	}

	public AxesPlayerClass(String playerName,
			Player player) {
		super(uniqueID, className, playerName, player);
	}

	public AxesPlayerClass(String playerName) {
		super(uniqueID, className, playerName);
	}

	private ItemStack identiferItem;
	
	public boolean isPrimaryClass() {
		return true;
	}

	public ClassType getPrimaryClassType() {
		return ClassType.Axes;
	}

	public ClassType[] getSubClassTypes() {
		return new ClassType[0];
	}
	
	public int getHighestPossibleTier() {
		return 4;
	}

	public ItemStack getIdentifingItem() {
		if (identiferItem == null) {
			identiferItem = new ItemStack(Material.DIAMOND_AXE);
			
			ItemMeta meta = identiferItem.getItemMeta();
			meta.setDisplayName(getDisplayName());
			meta.setLore(getLore());
			
			identiferItem.setItemMeta(meta);
		}
		
		return identiferItem.clone();
	}

	public Color getRBGColor() {
		return Color.fromRGB(215, 0, 0);
	}

	public Color getBaseArmourColor() {
		return ((LeatherArmorMeta) new ItemStack(Material.LEATHER_HELMET).getItemMeta()).getColor();
	}

	public boolean canUseClass() {
		// TODO Auto-generated method stub
		return false;
	}

	public ChatColor getColor() {
		return ChatColor.RED;
	}

	public ExperienceType[] getExpTypes() {
		return getTier().getExpTypes();
	}

	public boolean canUseTier(int tier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Map<Integer, IClassTier> craftClassTiers() {
		return new HashMap<Integer, IClassTier>();
	}

	@Override
	public void load(ConfigurationNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(ConfigurationNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected FileConfiguration getClassConfig() {
		// TODO Auto-generated method stub
		return null;
	}
}
