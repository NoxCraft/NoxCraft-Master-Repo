package com.noxpvp.mmo.listeners;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoArmor;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoSword;
import com.noxpvp.mmo.abilities.player.BackStabAbility;
import com.noxpvp.mmo.abilities.player.CriticalHitAbility;
import com.noxpvp.mmo.abilities.player.JoustAbility;
import com.noxpvp.mmo.abilities.player.RazerClawsAbility;
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

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	private void onDamage(EntityDamageEvent event) {		
		Entity e = event.getEntity();
		
		LivingEntity livingDamaged = (LivingEntity) ((e instanceof LivingEntity)? e : null),
				livingAttacker = null;
		Player playerDamaged = (Player) ((e instanceof Player)? e : null),
				playerAttacker = null;
		
		EntityDamageByEntityEvent pe = (EntityDamageByEntityEvent)
				(e.getLastDamageCause() instanceof EntityDamageByEntityEvent? e.getLastDamageCause() : null);
		
		if (pe != null) {
			livingAttacker = (LivingEntity) ((pe.getDamager() instanceof LivingEntity)? pe.getDamager() : null);
			playerAttacker = (Player) ((pe.getDamager() instanceof Player)? pe.getDamager() : null);
			
		}
		
		if (playerAttacker != null) {//damaged by player
			
			/*
			 * auto tools - sword
			 */
			if (playerAttacker.getItemInHand().getType() == Material.GOLD_SWORD && 
					VaultAdapter.permission.has(playerAttacker, NoxMMO.PERM_NODE + ".ability." + AutoSword.PERM_NODE)) {
				new AutoSword(playerAttacker).execute();
				
			}
			
			/*
			 * backstab
			 */
			if (VaultAdapter.permission.has(playerAttacker, NoxMMO.PERM_NODE + ".ability." + BackStabAbility.PERM_NODE)){
				String itemName = playerAttacker.getItemInHand().getType().name();
				
				if (itemName.contains("SWORD") || itemName.contains("AXE"))
					new BackStabAbility(playerAttacker);
			}
			
			/*
			 * critical hit
			 */
			if (VaultAdapter.permission.has(playerAttacker, NoxMMO.PERM_NODE + ".ability." + CriticalHitAbility.PERM_NODE)){
				String itemName = playerAttacker.getItemInHand().getType().name();
				
				if (itemName.contains("SWORD") || itemName.contains("AXE"))
					new CriticalHitAbility(playerAttacker);
			}
			
			/*
			 * player / living entity bars
			 */
			if (livingDamaged != null) {
				Location dLoc = e.getLocation();
				dLoc.setY(dLoc.getY() + 1.8);
				new EffectsRunnable(Arrays.asList("blockdust_152_0"), false, dLoc, .12F, 25, 1, null).runTask(NoxMMO.getInstance());
				
				/*
				 * joust passive ability
				 */
				if (VaultAdapter.permission.has(playerAttacker, NoxMMO.PERM_NODE + ".ability." + JoustAbility.PERM_NODE)) {
					new JoustAbility(playerAttacker, pe).execute();
				}
				
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
			
		
		if (playerDamaged != null) {//is a player getting damaged
			
			/*
			 * auto armor - helmet
			 */
			if (event.getCause() == DamageCause.DROWNING) {
				if (VaultAdapter.permission.has(playerDamaged, NoxMMO.PERM_NODE + ".ability." + AutoArmor.PERM_NODE)) {
					new AutoArmor(playerDamaged).execute();
				}
			}
			
		}
		
		if (livingDamaged != null) {
			switch (livingAttacker.getType()) {
			case WOLF:
				Wolf it = (Wolf) livingAttacker;
				AnimalTamer owner = it.getOwner() != null? it.getOwner() : null;
				
				if (owner != null && owner instanceof Player) {
					Player tamer = (Player) owner;
					
					/*
					 * razer claws ability
					 */
					if (VaultAdapter.permission.has(tamer, NoxMMO.PERM_NODE + ".ability." + RazerClawsAbility.PERM_NODE)) {
						new RazerClawsAbility(tamer, it).execute();
					}
					
				}
				
				break;
				
			default:
				break;
			}
		
		}
		
	}
	
}
