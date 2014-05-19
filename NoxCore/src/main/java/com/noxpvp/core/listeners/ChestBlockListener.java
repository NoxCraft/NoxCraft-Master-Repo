package com.noxpvp.core.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.events.ChestBlockedEvent;

public class ChestBlockListener implements Listener {
	public List<Block> getAdjacentChests(Block block) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (BlockFace face : sides)
			if (isChest(block.getRelative(face)))
				blocks.add(block.getRelative(face));
		
		return blocks;
	}
	
	public List<Block> getAdjacentChests(Location loc) {
		return getAdjacentChests(loc.getBlock());
	}
	
	private boolean isBlocked(Block block) {
		boolean isChest = isChest(block);
		if (isChest)
			return !(block.getRelative(BlockFace.UP).getType().isTransparent());
		else
			return !(block.getRelative(BlockFace.DOWN).getType().isTransparent());
	}
	
	private boolean isBlocked(Collection<Block> blocks) {
		for (Block b : blocks)
			if (isBlocked(b))
				return true;
		return false;
	}
	
	
	private boolean isChest(Block block) {
		return isChest(block.getType());
	}
	
	
	private boolean isChest(Material type) {
		return (type == Material.CHEST || type == Material.TRAPPED_CHEST);
	}
	
	@EventHandler(priority= EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockForm(BlockFormEvent event) {
		if (!useFormEvent)
			return;
		
		if (isBlocked(event.getBlock()))
			if (CommonUtil.callEvent(new ChestBlockedEvent(event)).isCancelled())
				event.setCancelled(true);
	}
	
	@EventHandler(priority= EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!usePlaceEvent)
			return;
		
		Block b = event.getBlock();
		if (b.getType().isTransparent())
			return;
		
		boolean blocked = true;
		blocked = isBlocked(b);
		
		Block check = (isChest(b)?b: b.getRelative(BlockFace.DOWN));
		if (!blocked && !isBlocked(getAdjacentChests(check)))
			return;
		
		ChestBlockedEvent ev = CommonUtil.callEvent(new ChestBlockedEvent(event));
		if (ev == null)
			return;

		event.setCancelled(ev.isCancelled());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) 	{
		if (!isRemovingOnInteract)
			return;
		
		if (!isChest(event.getClickedBlock()))
			return;
		
		boolean isBlocked = false;
		ItemStack i = new ItemStack(Material.DIAMOND_PICKAXE);
		Block aboveBlock = event.getClickedBlock().getRelative(BlockFace.UP);
		
		aboveBlock.breakNaturally(i);
		
		List<Block> blocks = new ArrayList<Block>();
		
		if (!aboveBlock.getType().isTransparent())
			blocks.add(aboveBlock);
		for(Block b : getAdjacentChests(event.getClickedBlock()))
			if (!b.getRelative(BlockFace.UP).getType().isTransparent())
				blocks.add(b);
		
		if (blocks.size() > 0)
			isBlocked = true;
		
		if (isBlocked)
			if (CommonUtil.callEvent(new ChestBlockedEvent(event)).isCancelled()){
				for (Block b : blocks)
					b.breakNaturally(i);
				event.setUseInteractedBlock(Result.ALLOW);
			}
		
	}
	
	public static boolean isRemovingOnInteract;
	
	public static final BlockFace[] sides = {BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
	
	public static boolean useFormEvent = false;
	
	public static boolean usePistonEvent = true;

	public static boolean usePlaceEvent = true;
	
//	@EventHandler(priority= EventPriority.HIGHEST, ignoreCancelled = true)
//	public void onPistonMoveEvent(BlockPistonEvent event0)
//	{
//		if (event0 instanceof BlockPistonExtendEvent)
//		{
//			BlockPistonExtendEvent event = (BlockPistonExtendEvent) event0;
//			List<Block> blocks = event.getBlocks();
//			BlockFace face = event.getDirection();
//			
//		} else {
//			BlockPistonRetractEvent event = (BlockPistonRetractEvent) event0;
//			Location l = event.getRetractLocation();
//			Block b = event.getBlock();
//			
//		}
//	}
	
}
