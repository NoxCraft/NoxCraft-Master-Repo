package com.noxpvp.mmo.prism;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.core.external.prism.BaseNoxPrismEvent;
import com.noxpvp.core.utils.PrismUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

public class AbilityUsePrismEvent extends BaseNoxPrismEvent {
	
	public static void trigger(Player player, AbilityResult abrs) {
		MMOPrismEventArgBuilder b = new MMOPrismEventArgBuilder();
		
		Ability ab = abrs.getExecuter();
		b.withAbility(ab);
		
		if (ab instanceof BaseEntityAbility) {
			double damage;
			if ((damage = ((BaseEntityAbility) ab).getDamage()) > 0)
				b.withDamage(damage);
			
			List<Entity> effected;
			if ((effected = ((BaseEntityAbility) ab).getEffectedEntities()).size() > 0)
				b.withEffectEntities(effected);
		}
		
		if (ab instanceof BaseTargetedPlayerAbility) {
			b.withTarget(((BaseTargetedPlayerAbility) ab).getTarget());
		}
		
		
		PrismUtil.callCustomPrismEvent(new AbilityUsePrismEvent(player, b.build()));
	}
	
	public AbilityUsePrismEvent(Player player, String message) {
		super(NoxMMO.getInstance(), UsedAbilityActionType.name, player, message);
	}

}
