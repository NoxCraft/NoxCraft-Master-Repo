package com.noxpvp.mmo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoTool;

public class BlockListener extends NoxListener<NoxMMO>{

	public BlockListener(NoxMMO mmo) {
		super(mmo);
	}
	
	public BlockListener() {
		this(NoxMMO.getInstance());
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		if (VaultAdapter.permission.has(e.getPlayer(), NoxMMO.PERM_NODE + ".ability." + AutoTool.PERM_NODE)) {
			new AutoTool(e.getPlayer()).execute(e);
		}
	}
	
	
//	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
//	public void onPlace(BlockPlaceEvent e) {
//		
//	}
	
}
