package com.noxpvp.core.logging;

import java.io.File;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.TimeUtils;

public class PVELog extends NoxListener<NoxCore>{

	private static final String PVELog = "PVELOG";
	
	public PVELog(NoxCore core) {
		super(core);
		
	}
	
	/**
	 * Gets the player PVELog file.
	 * <b>INTERNAL METHOD</b> Best not to use this!
	 * @param uuid The players unique id
	 * @return the players log file
	 */
	private FileConfiguration getPlayerPVELog(UUID uuid){
		return new FileConfiguration(getPlugin(), PVELog + File.separatorChar + uuid.toString() + ".yml");
	}
	
	public void log(EntityDamageEvent event) {
		Player p;
		
		if (!(event.getEntity() instanceof Player) || (p = (Player) event.getEntity()) == null)
			return;
		
		StringBuilder log = new StringBuilder();
		
		String time, player, damager, damage, loc;
		UUID uuid;
		
		uuid = p.getUniqueId();
		time = TimeUtils.getReadableMillisTime(System.currentTimeMillis());
		player = p.getName();
		
		
		{
			EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) ((event instanceof EntityDamageByEntityEvent)? event : null);
			damager = event2 == null? event.getCause().name() : (event2.getDamager() instanceof Player)? ((Player) event2.getDamager()).getName() : event2.getDamager().getType().name();
		}
		
		damage = event.getDamage() + " / " + p.getHealth();
		
		{
			Location pLoc = p.getLocation();
			loc = "X= " + (float) pLoc.getX() + ", Y= " + (float) pLoc.getY() + ", Z= " + (float) pLoc.getZ();
		}

		log.append(player + " hit for " + damage + " by " + damager + ". At " + loc);
		
		getPlayerPVELog(uuid).set(time, log.toString());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent e){
		if (!(e.getEntity() instanceof Player))
			return;
		
		log(e);
		
	}
	
}
