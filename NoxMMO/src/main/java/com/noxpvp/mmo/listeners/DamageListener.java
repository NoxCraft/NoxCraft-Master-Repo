package com.noxpvp.mmo.listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.DamageUtil;
import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class DamageListener extends NoxListener<NoxMMO>{

	PlayerManager pm;
	
	public DamageListener(NoxMMO mmo)
	{
		super(mmo);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public DamageListener() {
		this(NoxMMO.getInstance());
	}
	
	

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = false)
	public void onDamage(EntityDamageEvent event) {	
		
		Entity e = event.getEntity();
		
		LivingEntity livingDamaged = DamageUtil.getDamagedEntity(event),
				livingAttacker = DamageUtil.getAttackingEntity(event);
		
		Player playerDamaged = DamageUtil.getDamagedPlayer(event),
				playerAttacker = DamageUtil.getAttackingPlayer(event);
		
		
		if (playerAttacker != null) {
			
			if (livingDamaged != null) {
				com.noxpvp.mmo.PlayerManager.getInstance().getPlayer(playerAttacker).setTarget(livingDamaged);
				
				Location dLoc = e.getLocation();
				dLoc.setY(dLoc.getY() + 1.8);
				new EffectsRunnable(Arrays.asList("blockdust_152_0"), false, dLoc, .12F, 25, 1, null).runTask(NoxMMO.getInstance());
				
				ChatColor color = ChatColor.GREEN;//TODO Make as a locale variable
				CoreBar bar = pm.getCoreBar(playerAttacker.getName());
				
				if (playerDamaged != null) {
					
					NoxPlayer noxPlayerDamaged = pm.getPlayer(playerDamaged.getName());
					
					if (noxPlayerDamaged != null)
						bar.newLivingTracker(livingDamaged, noxPlayerDamaged.getFullName(), color);
						
					
				} else {
					bar.newLivingTracker(livingDamaged, livingDamaged.getType().name(), color);
				}
				
			}
		}
		
	}
	
}
