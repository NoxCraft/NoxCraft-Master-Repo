package com.noxpvp.mmo.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseAbility implements Ability {
	private final String name;
	
	public BaseAbility(final String name) {
		this.name = name;
	}
	
	/**
	 * This implementation returns {@link MMOLocale#ABIL_DISPLAY_NAME} using method {@link MMOLocale#get(String...)} with the parameters as [getName(), ""]
	 * <hr>
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return MMOLocale.ABIL_DISPLAY_NAME.get(getName(), "");
	}
	
	public String getDisplayName(ChatColor color) {
		return color + getDisplayName();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return "\"Cryptic message here\"";
	}
	
	public List<String> getLore() {
		return getLore(ChatColor.WHITE);
	}
	
	public List<String> getLore(ChatColor color) {
		List<String> ret = new ArrayList<String>();
		
		for (String cur : MessageUtil.convertStringForLore(getDescription())) {
			ret.add(color + cur);
		}
			
		return ret;
	}
}
