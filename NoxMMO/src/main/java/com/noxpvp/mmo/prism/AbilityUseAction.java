package com.noxpvp.mmo.prism;

import java.util.Map;

import org.bukkit.ChatColor;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;

import me.botsko.prism.actions.GenericAction;

public class AbilityUseAction extends GenericAction {

	@Override
	public String getNiceName() {
		if (type.getName() == UsedAbilityActionType.name && data != null && !data.isEmpty()) {
			MessageBuilder ret = new MessageBuilder();
			Map<String, Object> args = MMOPrismEventArgBuilder.getMapFromBuilder(data);
			
			String ability, target, damage, heal;
			String[] multiTarget;

			//Ability
			if ((ability = (String) args.get(MMOPrismEventArgBuilder.ABILITY_ARG)) != null)
				ret.gold(ability);
			
			//Target
			if ((target = (String) args.get(MMOPrismEventArgBuilder.TARGET_ARG)) != null)
				ret.white(" on ").gold(target);
			
			//Damage
			if ((damage = (String) args.get(MMOPrismEventArgBuilder.DAMAGE_ARG)) != null)
				ret.white(", damage: ").gold(damage); 
			
			//Heal
			if ((heal = (String) args.get(MMOPrismEventArgBuilder.HEAL_ARG)) != null)
				ret.white(", heal: ").gold(heal); 

			//Multi-target
			if ((multiTarget = (String[]) args.get(MMOPrismEventArgBuilder.TARGET_MULTIPLE_ARG)) != null)
				ret.white(". effected: ").gold(StringUtil.join(ChatColor.WHITE + ", " + ChatColor.GOLD, multiTarget));
			
			if (!ret.isEmpty())
				return ret.toString();
		}
		return "REPORT TO STAFF - UNKNOWN_LOG: " + data;
	}
	
}
