package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class RagePlayerAbility extends BaseRangedPlayerAbility implements IPVPAbility {

	public static final String ABILITY_NAME = "Rage";
	public static final String PERM_NODE = "Rage";
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;

	/**
	 * @param player The user of the ability instance
	 */
	public RagePlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range);
		
		setCD(75);

		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}

			public void execute(EntityDamageByEntityEvent event) {
				if (!event.getDamager().equals(RagePlayerAbility.this.getPlayer()))
					return;

				double damage = event.getDamage();
				double range = getRange();
				
				Player attacker = (Player) event.getDamager();

				for (Entity it : getPlayer().getNearbyEntities(range, range, range)) {
					if (!(it instanceof Damageable) || it.equals(attacker)) continue;

					((Damageable) it).damage(damage - (damage / 4), attacker);
					StaticEffects.SkullBreak((LivingEntity) it);
				}
			}
		};

	}

	public RagePlayerAbility(Player player) {
		this(player, 5);
	}

	@Override
	public String getDescription() {
		return "Your axe becomes ingulfed with your own rage! Dealing 75% damage to all enemys surrounding anything you damage";
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();

		int length = (20 * (pClass.getTotalLevel())) / 16;

		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), length);

		return new AbilityResult(this, true);
	}

}
