package com.noxpvp.core.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.dsh105.holoapi.util.TagIdGenerator;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.BlockChangeArray;

public class PacketUtil extends com.bergerkiller.bukkit.common.utils.PacketUtil {
	
	private static List<Integer> MagicEntityId = new ArrayList<Integer>();
	private static int SHARED_IDS = Short.MAX_VALUE;
	
	public static int getNewEntityId(int amount) {
		if (NoxCore.getInstance().isHoloAPIActive())
			return TagIdGenerator.nextId(amount);
		
		int id = ++SHARED_IDS;
		SHARED_IDS =+ amount * 2;
		
		return (id);
	}
	
	public static void returnMagicEntityId(int id) {
		if (MagicEntityId.contains(id))
			MagicEntityId.remove(id);
	}
	
	public static void FakeBlock(int seconds, Material type, Location loc) {
		Map<Material, Location> single = new HashMap<Material, Location>();
		single.put(type, loc);
		
		FakeBlocks(seconds, single);	
	}
	
	public static void FakeBlocks(int seconds, Map<Material, Location> changes) {
		try {
			
			CommonPacket block = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
			BlockChangeArray blockChanges = new BlockChangeArray(changes.size());
			
			int i = 0;
			Iterator<Entry<Material, Location>> iter = changes.entrySet().iterator();
			Map.Entry<Material, Location> pair = null;
			while (iter.hasNext())
			{
				pair = iter.next();
				
				blockChanges.getBlockChange(i).
				setLocation(pair.getValue()).
				setBlockID(pair.getKey().getId());
				
				i++;
			}
			if (pair == null)
				return;
			
			block.write(PacketType.OUT_MULTI_BLOCK_CHANGE.chunk, new IntVector2(pair.getValue().getChunk()));
			block.write(PacketType.OUT_MULTI_BLOCK_CHANGE.blockCount, changes.size());
			block.write(PacketType.OUT_MULTI_BLOCK_CHANGE.blockData, blockChanges.toByteArray());
			
			broadcastPacket(block, false);
			
			final CommonPacket update = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
			Map<Material, Location> refresh = new HashMap<Material, Location>();
			BlockChangeArray blockChangesUpdate = new BlockChangeArray(refresh.size());
			
			i = 0;
			iter = changes.entrySet().iterator();
			
			while (iter.hasNext())
			{
				pair = iter.next();
				
				blockChangesUpdate.getBlockChange(i).
				setLocation(pair.getValue()).
				setBlockID(pair.getValue().getBlock().getTypeId());
			
				i++;
			}
			
			Bukkit.getScheduler().runTaskLater(NoxCore.getInstance(), new Runnable() {
				
				public void run() {
					broadcastPacket(update, true);
				}
			}, 20 * seconds);
			
		} catch (Exception e) {e.printStackTrace();}
	}

}
