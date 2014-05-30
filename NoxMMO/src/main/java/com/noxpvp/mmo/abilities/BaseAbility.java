package com.noxpvp.mmo.abilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.events.EntityAbilityExecutedEvent;
import com.noxpvp.mmo.events.EntityTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseAbility implements Ability {
	private final String name;
	private ItemStack identifingItem;

	public BaseAbility(final String name) {
		this.name = name;
	}

	/**
	 * This implementation returns {@link MMOLocale#ABIL_DISPLAY_NAME} using method {@link MMOLocale#get(String...)} with the parameters as [getName(), getName()]
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
	
	public String getUniqueId() {
		return getName() + "-" + hashCode();
	}

	public String getDescription() {
		return "\"Cryptic message here\"";
	}

	public List<String> getLore(int linesLength) {
		return getLore(ChatColor.WHITE, linesLength);
	}

	public List<String> getLore(ChatColor color, int linesLength) {
		List<String> ret = new ArrayList<String>();

		for (String cur : MessageUtil.convertStringForLore(getDescription(), linesLength)) {
			ret.add(color + cur);
		}

		return ret;
	}
	
	public ItemStack getIdentifiableItem(boolean canUse) {
		ItemStack item = getIdentifiableItem();
		item.setType(canUse? Material.BLAZE_POWDER : Material.SULPHUR);
		
		if (!canUse) {
			ItemMeta meta = item.getItemMeta();
			MessageBuilder obfuscated = new MessageBuilder();
			
			for (String s : getDescription().split(" ")) {
				if (RandomUtils.nextFloat() > .2)
					obfuscated.gold("").magic(s);
				else
					obfuscated.gold(s);
				
				obfuscated.append(' ');
			}
			
			meta.setLore(MessageUtil.convertStringForLore(obfuscated.toString(), 40));
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	public ItemStack getIdentifiableItem() {
		if (identifingItem == null) {
			identifingItem = new ItemStack(Material.BLAZE_POWDER);

			ItemMeta meta = identifingItem.getItemMeta();
			
			meta.setDisplayName(new MessageBuilder().gold(ChatColor.BOLD + "Ability: ").red(getName()).toString());
			meta.setLore(getLore(ChatColor.GOLD, 28));
			
			identifingItem.setItemMeta(meta);
		}
		
		return identifingItem.clone();
	}

	public boolean mayExecute() {
		if (isCancelled())
			return false;

		return true;
	}
	
	public class AbilityResult {
		private Ability executer;
		private boolean result;
		private String[] messages;
		
		public AbilityResult(Ability executer, boolean result, String... messages) {
			this.executer = executer;
			this.result = result;
			this.messages = messages;
			
			callEvent();
		}
		
		public void callEvent() {
			Event event = null;
			
			if (executer instanceof BaseTargetedPlayerAbility)
				event = new PlayerTargetedAbilityExecutedEvent(((BaseTargetedPlayerAbility) executer).getPlayer(), this);
			else if (executer instanceof BaseTargetedEntityAbility) {
				event = new EntityTargetedAbilityExecutedEvent(((BaseTargetedEntityAbility) executer).getEntity(), this);	
			} else if (executer instanceof BasePlayerAbility) {
				event = new PlayerAbilityExecutedEvent(((BasePlayerAbility) executer).getPlayer(), this);
			} else if (executer instanceof BaseEntityAbility) {
				event = new EntityAbilityExecutedEvent(((BaseEntityAbility) executer).getEntity(), this);
			}
			
			if (event == null)
				throw new NullPointerException("executer could not be casted to an ability type");
			
			CommonUtil.callEvent(event);
		}
		
		public Ability getExecuter() {
			return executer;
		}
		
		public boolean getResult() {
			return result;
		}
		
		public String[] getMessages() {
			return messages;
		}
	}
}
