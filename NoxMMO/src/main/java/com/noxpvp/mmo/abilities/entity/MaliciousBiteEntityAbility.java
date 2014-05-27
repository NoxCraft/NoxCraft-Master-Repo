package com.noxpvp.mmo.abilities.entity;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class MaliciousBiteEntityAbility extends BaseEntityAbility implements IPassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String ABILITY_NAME = "Malicious bite";
	public static final String PERM_NODE = "malicious-bite";

	public MaliciousBiteEntityAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult(this, false);

		if (!(getEntity() instanceof Tameable)) return new AbilityResult(this, false);

		AnimalTamer a = ((Tameable) getEntity()).getOwner();

		if (a == null || !(a instanceof Player)) return new AbilityResult(this, false);

		Player o = (Player) a;

		IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(o).getPrimaryClass();

		return new AbilityResult(this, RandomUtils.nextFloat() < (pClass.getCurrentTierLevel() * pClass.getLevel()) / 1000);
	}

	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

}
