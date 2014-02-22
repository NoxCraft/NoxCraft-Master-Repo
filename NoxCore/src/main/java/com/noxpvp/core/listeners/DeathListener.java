package com.noxpvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.PlayerManager;

public class DeathListener extends NoxListener<NoxCore> {
	private PlayerManager pm;
	
	public DeathListener()
	{
		this(NoxCore.getInstance());
	}
	
	public DeathListener(NoxCore core)
	{
		super(core);
		this.pm = core.getPlayerManager();
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent e)
	{
		NoxPlayer player = pm.getPlayer(e.getEntity());
		if (player == null)
			return;
		
		player.setLastDeath(e);
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent e)
	{
		Player p = null;
		NoxPlayer np = null;
		EntityDamageEvent ede =  e.getEntity().getLastDamageCause();
		if (ede instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) ede;
			if (edbe.getDamager() instanceof Projectile) {
				if (((Projectile)edbe.getDamager()).getShooter() instanceof Player) {
					p = (Player)((Projectile)edbe.getDamager()).getShooter();
					if ((np = pm.getPlayer(p)) != null)
						np.setLastKill(e.getEntity());
				}
			}
		}
	}
}
