package com.noxpvp.mmo.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.player.AutoToolAbilities.AutoTool;

public class BlockListener extends GenericNoxListener<BlockEvent>{

	private PlayerManager pm;

	public BlockListener(NoxMMO mmo) {
		super(mmo, BlockEvent.class);
		
		pm = mmo.getPlayerManager();
	}
	
	public BlockListener() {
		this(NoxMMO.getInstance());
	}

	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		if (VaultAdapter.permission.has(e.getPlayer(), NoxMMO.PERM_NODE + ".ability." + AutoTool.PERM_NODE)) {
			new AutoTool(e.getPlayer(), e).execute();
		}
	}
	
	
//	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
//	public void onPlace(BlockPlaceEvent e) {
//		
//	}
	
}