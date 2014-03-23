package com.noxpvp.mmo.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.player.MedPackAbility;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;

public class PlayerInteractListener extends NoxListener<NoxMMO>{

	PlayerManager pm;
	
	public PlayerInteractListener(NoxMMO mmo)
	{
		super(mmo);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public PlayerInteractListener() {
		this(NoxMMO.getInstance());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent e) {
		
		Player p;
		MMOPlayer player = pm.getPlayer((p = e.getPlayer()));
		if (player == null) return;
		
		new TargetAbility(p).execute();//TODO make default range configized
		
		//debug===========================================
		if (p.getItemInHand().getType() != Material.STICK)
			return;
		MessageUtil.broadcast("sticked");
		
		new MedPackAbility(p).execute();
		
	}
	
}
