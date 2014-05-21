package com.noxpvp.mmo.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.noxpvp.core.internal.IHeated;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.events.EntityAbilityExecutedEvent;
import com.noxpvp.mmo.events.EntityAbilityPreExcuteEvent;
import com.noxpvp.mmo.events.PlayerAbilityExecutedEvent;
import com.noxpvp.mmo.events.PlayerAbilityPreExecuteEvent;

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
	public void onPlayerAbilityPreExecute(PlayerAbilityPreExecuteEvent event) {
		return;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityAbilityExecuted(EntityAbilityExecutedEvent event) {
		return;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerAbilityExecuted(PlayerAbilityExecutedEvent event) {
		Player p = event.getPlayer();
		MMOPlayer mp = pm.getPlayer(p);
		BaseEntityAbility ab = event.getAbility();
		
		if (ab instanceof IHeated)
			mp.addCoolDown(ab.getName(), ((IHeated) ab).getCoolDownTime(), true);
		
		return;
	}
	
}
