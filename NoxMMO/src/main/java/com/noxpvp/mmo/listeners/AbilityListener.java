package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.SilentAbility;
import com.noxpvp.mmo.events.EntityAbilityExecutedEvent;
import com.noxpvp.mmo.events.EntityAbilityPreExcuteEvent;
import com.noxpvp.mmo.events.EntityTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.events.EntityTargetedAbilityPreExecuteEvent;
import com.noxpvp.mmo.events.PlayerAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerAbilityPreExecuteEvent;
import com.noxpvp.mmo.events.PlayerTargetedAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerTargetedAbilityPreExecuteEvent;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.prism.AbilityUsePrismEvent;

public class AbilityListener extends NoxListener<NoxMMO> {

	MMOPlayerManager pm;

	public AbilityListener() {
		this(NoxMMO.getInstance());
	}

	public AbilityListener(NoxMMO plugin) {
		super(plugin);

		this.pm = MMOPlayerManager.getInstance();
	}

	//TODO finish

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityAbilityPreExecute(EntityAbilityPreExcuteEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityAbilityExecuted(EntityAbilityExecutedEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerAbilityPreExecute(PlayerAbilityPreExecuteEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerAbilityExecuted(PlayerAbilityExecutedEvent event) {
		Player p = event.getPlayer();
		MMOPlayer mp = pm.getPlayer(p);
		BaseEntityAbility ab = event.getAbility();
		AbilityResult result = event.getResult();
		
		boolean hasMessages = result.getMessages().length != 0;
		boolean silent = (ab instanceof SilentAbility);
		boolean hasCD = ab.getCD() > 0;
		
		if (result.getResult()) {
			AbilityUsePrismEvent.trigger(p, result);
			
			if (hasCD)
				mp.addCoolDown(ab.getName(), ab.getCD(), !silent);
		}
		
		if (result.getResult() && !silent)
			if (!hasMessages)
				MessageUtil.sendLocale(p, MMOLocale.ABIL_USE, ab.getName());
			else
				p.sendMessage(MessageUtil.parseColor(StringUtil.join(" ", result.getMessages())));

	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityTargetedAbilityPreExecute(EntityTargetedAbilityPreExecuteEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityTargetedAbilityExecuted(EntityTargetedAbilityExecutedEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTargetedAbilityPreExecute(PlayerTargetedAbilityPreExecuteEvent event) {
		return;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTargetedAbilityExecuted(PlayerTargetedAbilityExecutedEvent event) {
		Player p = event.getPlayer();
		MMOPlayer mp = pm.getPlayer(p);
		BaseTargetedPlayerAbility ab = event.getAbility();
		AbilityResult result = event.getResult();
		
		boolean hasMessages = result.getMessages().length != 0;
		boolean silent = (ab instanceof SilentAbility);
		boolean hasCD = ab.getCD() > 0;
		
		if (result.getResult()) {
			AbilityUsePrismEvent.trigger(p, result);
			
			if (hasCD)
				mp.addCoolDown(ab.getName(), ab.getCD(), !silent);
		}

		String target = ab.getTarget() instanceof Player ?
				pm.getPlayer((Player) ab.getTarget()).getFullName() :
				ab.getTarget().getType().name().toLowerCase();

		if (result.getResult() && !silent) {
			if (hasMessages) {
				p.sendMessage(MessageUtil.parseColor(StringUtil.join(" ", result.getMessages())));
			} else if (ab.getDamage() > 0) {
				MessageUtil.sendLocale(p, MMOLocale.ABIL_USE_TARGET_DAMAGED, ab.getName(), target, String.format("%.2f", ab.getDamage()));
					
				if (ab.getTarget() instanceof Player)
					MessageUtil.sendLocale((Player) ab.getTarget(), MMOLocale.ABIL_HIT_ATTACKER_DAMAGED, mp.getFullName(), ab.getName(), String.format("%.2f", ab.getDamage()));
			} else {
				MessageUtil.sendLocale(p, MMOLocale.ABIL_USE_TARGET, ab.getName(), target);
				
				if (ab.getTarget() instanceof Player)
					MessageUtil.sendLocale((Player) ab.getTarget(), MMOLocale.ABIL_HIT_ATTACKER, mp.getFullName(), ab.getName());
			}
		}
		
		return;
	}

}
