package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;

public class PlayerTargetListener extends GenericNoxListener<PlayerInteractEvent>{

	PlayerManager pm;
	
	public PlayerTargetListener(NoxMMO mmo)
	{
		super(mmo, PlayerInteractEvent.class);
		
		this.pm = NoxMMO.getInstance().getPlayerManager();
	}
	
	public PlayerTargetListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) {
		
		Player p;
		MMOPlayer player = pm.getMMOPlayer(p = e.getPlayer());
		
		if (player == null) return;
		
		new TargetAbility(p).setRange(30).execute();//TODO make default range configized
		
	}
}
