package com.noxpvp.mmo.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.events.EntityAbilityExecutedEvent;
import com.noxpvp.mmo.events.EntityTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BaseAbility implements Ability {
	private final String name;

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
			
			if (executer instanceof BaseTargetedAbility) {
				if (executer instanceof BaseTargetedPlayerAbility)
					event = new PlayerTargetedAbilityExecutedEvent(((BaseTargetedPlayerAbility) executer).getPlayer(), this);
				else if (executer instanceof BaseTargetedEntityAbility)
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
