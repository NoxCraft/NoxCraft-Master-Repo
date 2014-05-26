package com.noxpvp.mmo.prism;

import java.util.Map;

import org.bukkit.ChatColor;

import com.bergerkiller.bukkit.common.utils.StringUtil;

import me.botsko.prism.actions.GenericAction;

public class AbilityUseAction extends GenericAction {

	@Override
	public String getNiceName() {
		if (type.getName() == UsedAbilityActionType.name && data != null && !data.isEmpty()) {
			StringBuilder ret = new StringBuilder();
			Map<String, Object> args = MMOPrismEventArgBuilder.getMapFromBuilder(data);

			if (args.containsKey(MMOPrismEventArgBuilder.ABILITY_ARG))
				ret.append((String) args.get(MMOPrismEventArgBuilder.ABILITY_ARG));
			
			if (args.containsKey(MMOPrismEventArgBuilder.TARGET_ARG))
				ret.append(ChatColor.WHITE + " on " + ChatColor.GOLD).append(String.valueOf(args.get(MMOPrismEventArgBuilder.TARGET_ARG)));
			
			if (args.containsKey(MMOPrismEventArgBuilder.TARGET_MULTIPLE_ARG)) {
				String[] effected = (String[]) args.get(MMOPrismEventArgBuilder.TARGET_MULTIPLE_ARG);
				
				ret.append(ChatColor.WHITE + " - effect entities: " + ChatColor.GOLD).append(StringUtil.join(", ", effected));
			}
			
			return ret.toString();
		}
		return "UNKNOWN_ABILITY";
	}
	
}
