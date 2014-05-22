package com.noxpvp.mmo.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.packet.PacketSounds;
import com.noxpvp.mmo.NoxMMO;

public class HealListener extends NoxListener<NoxMMO> {

	public HealListener(NoxMMO plugin) {
		super(plugin);

	}

	public HealListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onHeal(EntityRegainHealthEvent event) {

		double amount = event.getAmount();
		final LivingEntity healed;

		//return unless a living entity
		if (!(event.getEntity() instanceof LivingEntity) || (healed = (LivingEntity) event.getEntity()) == null)
			return;

		{
			//figure out if the entity was healed, and if it was too much
			double damage = healed.getMaxHealth() - healed.getHealth();
			amount = damage < amount ? damage : amount;
		}

		//not actually gaining health
		if (amount <= 0)
			return;

		if (getPlugin().getMMOConfig().get("effect.heal.heart", Boolean.class, Boolean.TRUE)) {
			StaticEffects.HeartEffect(healed, amount, getPlugin());
		}

		if (getPlugin().getMMOConfig().get("effect.heal.heal-amount-particle", Boolean.class, Boolean.TRUE)) {
			StaticEffects.HealAmountParticle(healed, amount);
		}

		if (healed instanceof Player && event.getRegainReason() == RegainReason.CUSTOM)
			for (int i = 0; i < amount; i++) {
				Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {

					public void run() {
						StaticEffects.playSound((Player) healed, PacketSounds.LiquidLavaPop, 2, 3);
					}
				}, i);
			}
	}

}
