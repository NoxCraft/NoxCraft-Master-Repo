package com.noxpvp.mmo.prism;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.core.external.prism.NoxPrismEventArgBuilder;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.Ability;

public class MMOPrismEventArgBuilder extends NoxPrismEventArgBuilder {
	public static final String ABILITY_ARG = "ability";
	public static final String TARGET_ARG = "target";
	public static final String TARGET_MULTIPLE_ARG = "target-multiple";
	public static final String DAMAGE_ARG = "damage";
	public static final String HEAL_ARG = "heal";
	
	public void withAbility(Ability ab) {
		withArg(ABILITY_ARG, ab.getDisplayName(ChatColor.GOLD));
	}
	
	public void withTarget(LivingEntity target) {
		String tName;
		
		if (target instanceof Player)
			tName = MMOPlayerManager.getInstance().getPlayer((Player) target).getFullName();
		else tName = target.getType().name().toLowerCase();
		
		withArg(TARGET_ARG, tName);
	}

	public void withDamage(double damage) {
		withArg(DAMAGE_ARG, String.format("%.2f", damage));
	}
	
	public void withEffectEntities(List<Entity> ents) {
		List<String> additions = new ArrayList<String>();
		
		for (Entity e : ents) {
			String name;
			
			if (e instanceof Player)
				name = MMOPlayerManager.getInstance().getPlayer((Player) e).getFullName();
			else name = e.getType().name().toLowerCase();
			
			if (name != null && !name.isEmpty())
				additions.add(name);
		}
		
		if (additions.size() > 0)
			withList(TARGET_MULTIPLE_ARG, additions);
	}

}
