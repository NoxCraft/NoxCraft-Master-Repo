package com.noxpvp.mmo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;

public class PlayerTargetListener extends NoxListener<NoxMMO>{

	PlayerManager pm;
	
	public PlayerTargetListener(NoxMMO mmo)
	{
		super(mmo);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public PlayerTargetListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent e) { //TODO: USE PACKET INSTEAD
		
		Player p;
		MMOPlayer player = pm.getPlayer(p = e.getPlayer());
		
		if (player == null) return;
		
		MessageUtil.broadcast("starting target");
		new TargetAbility(p).setRange(50).execute();//TODO make default range configized
		
	}
	
}
