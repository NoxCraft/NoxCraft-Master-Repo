package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class CriticalHitPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, IPVPAbility {

	public static final String PERM_NODE = "critical-hit";
	public static final String ABILITY_NAME = "Critical Hit";
	private MMOPlayerManager pm;

	public CriticalHitPlayerAbility(Player p) {
		super(ABILITY_NAME, p);

		this.pm = MMOPlayerManager.getInstance();
	}

	@Override
	public String getDescription() {
		return "A random chance to land a critical hit, causing nausia and increased damage on the target";
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player playerAttacker = (Player) ((event.getDamager() instanceof Player) ? event.getDamager() : null);


		if (playerAttacker == null || !playerAttacker.equals(getPlayer()))
			return new AbilityResult(this, false);
		
		String itemName = playerAttacker.getItemInHand().getType().name().toUpperCase();
		if (!itemName.contains("SWORD") && !itemName.contains("AXE"))
			return new AbilityResult(this, false);

		MMOPlayer player = pm.getPlayer(getPlayer());

		if (player == null)
			return new AbilityResult(this, false);

		IPlayerClass clazz = player.getPrimaryClass();

		double damage = (clazz.getLevel() + clazz.getTotalLevel()) / 75;
		if ((Math.random() * 100) > (damage * 45))
			return new AbilityResult(this, false);

		event.setDamage(damage);

		if (event.getEntity() instanceof LivingEntity)
			((LivingEntity) event.getEntity()).addPotionEffect(
					new PotionEffect(PotionEffectType.CONFUSION, 40, 2, false));

		return new AbilityResult(this, true);
	}

	/**
	 * Always Returns True Due To Being Passive!
	 */
	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

}
