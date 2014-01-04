package com.noxpvp.mmo.listeners;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.CoreBar;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class DamageListener extends GenericNoxListener<EntityDamageEvent>{

	PlayerManager pm;
	
	public DamageListener(NoxMMO mmo)
	{
		super(mmo, EntityDamageEvent.class);
		
		this.pm = NoxCore.getInstance().getPlayerManager();
	}
	
	public DamageListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	private void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) return;
		
		LivingEntity eEntity = (LivingEntity) e.getEntity();
		
		Location loc = eEntity.getLocation();
		loc.setY(loc.getY() + 1.8);
		
		new EffectsRunnable(Arrays.asList("blockdust_152_0"), loc, .1F, 50, false, false, null).runTask(NoxMMO.getInstance());
			
		EntityDamageByEntityEvent pe = (EntityDamageByEntityEvent)
				(eEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent? eEntity.getLastDamageCause() : null);
		
		if (pe != null && (pe.getDamager() instanceof Player)) {
			//damaged by player
			Player eDamager = (Player) pe.getDamager();
			ChatColor color = ChatColor.GREEN;//TODO Make as a locale variable
			
			//is a player getting damaged
			if (eEntity instanceof Player) {
				
				NoxPlayer eDamagerPlayer = pm.getPlayer(eDamager.getName()),
						eEntityPlayer = pm.getPlayer((Player) eEntity);
				
				if (eDamagerPlayer == null || eEntityPlayer == null)
					return;
				
				CoreBar bar = pm.getCoreBar(eDamager.getName());
				bar.newLivingTracker(eEntity, eEntityPlayer.getFullName(), color);
			} else {//still make bar, but with the entity type name instead
				
				CoreBar bar = pm.getCoreBar(eDamager.getName());
				bar.newLivingTracker(eEntity, eEntity.getType().name(), color);
				
			}
			
		}
		
	}
	
}
