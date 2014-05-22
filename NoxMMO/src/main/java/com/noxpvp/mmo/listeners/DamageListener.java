package com.noxpvp.mmo.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.gui.corebar.LivingEntityTracker;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.mmo.NoxMMO;

public class DamageListener extends NoxListener<NoxMMO> {

	CorePlayerManager pm;

	public DamageListener(NoxMMO mmo) {
		super(mmo);

		this.pm = CorePlayerManager.getInstance();
	}

	public DamageListener() {
		this(NoxMMO.getInstance());
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event) {

//		Entity e = event.getEntity();

		LivingEntity livingDamaged = DamageUtil.getDamagedEntity(event),
				livingAttacker = DamageUtil.getAttackingLivingEntity(event);

		Player playerDamaged = DamageUtil.getDamagedPlayer(event),
				playerAttacker = DamageUtil.getAttackingPlayer(event);

		if (playerAttacker != null) {

			if (livingDamaged != null) {
				com.noxpvp.mmo.MMOPlayerManager.getInstance().getPlayer(playerAttacker).setTarget(livingDamaged);

				if (event.getDamage() > 0) {
					if (getPlugin().getMMOConfig().get("effect.damage.blood", Boolean.class, Boolean.TRUE)) {
						StaticEffects.BloodEffect(livingDamaged, getPlugin());
					}
					if (getPlugin().getMMOConfig().get("effect.damage.damage-particle", Boolean.class, Boolean.TRUE)) {
						StaticEffects.DamageAmountParticle(livingDamaged, event.getDamage());
					}
				}

				if (playerDamaged != null) {

					NoxPlayer noxPlayerDamaged = pm.getPlayer(playerDamaged.getName());

					if (noxPlayerDamaged != null)
						new LivingEntityTracker(playerAttacker, livingDamaged, noxPlayerDamaged.getFullName(), 300);


				} else {
					new LivingEntityTracker(playerAttacker, livingDamaged, livingDamaged.getType().name(), 300);
				}

			}
		}

	}

}
