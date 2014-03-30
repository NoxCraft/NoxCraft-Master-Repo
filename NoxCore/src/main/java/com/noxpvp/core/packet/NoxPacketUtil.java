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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.dsh105.holoapi.util.TagIdGenerator;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.BlockChangeArray;

public class NoxPacketUtil extends com.bergerkiller.bukkit.common.utils.PacketUtil {
	
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
	
	/**
	 * <b>This is a very glitchy method, do NOT use it unless you know the exact implications</b>
	 * 
	 * @param e
	 * @param type
	 * @param data
	 * @param field
	 */
	public static void fakeEntityType(Entity e, ObjectType type, Object data, int field) {
		try {
			CommonPacket packet = new CommonPacket(PacketType.OUT_ENTITY_SPAWN_LIVING);
			
			DataWatcher dw = new DataWatcher();
			dw.set(0, (byte) 0);
			dw.set(12, 0);
			dw.set(10, data);
			
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityId, e.getEntityId());
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityType, (int) type.getByteId());
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.motX, (int) e.getLocation().getX() * 32);
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.motY, (int) e.getLocation().getY() * 32);
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.motZ, (int) e.getLocation().getZ() * 32);
			packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.dataWatcher, dw);
			
			broadcastPacket(packet, true);
			
		} catch (Exception ex) {}
	}

	/**
	 * <b>Warning: this is a method which should not be used unless you know the exact implications</b>
	 * 
	 * @param a
	 * @param item
	 */
	public static void disguiseArrow(Arrow a, ItemStack item) {
		try {
			CommonPacket packet = new CommonPacket(PacketType.OUT_ENTITY_SPAWN);
			PacketContainer packetMeta = new PacketContainer(com.comphenix.protocol.PacketType.Play.Server.ENTITY_METADATA);
			
			Location aLoc = a.getLocation();
			double x = aLoc.getX(), y = aLoc.getY(), z = aLoc.getZ();
			
			packet.write(PacketType.OUT_ENTITY_SPAWN.entityId, a.getEntityId());
			packet.write(PacketType.OUT_ENTITY_SPAWN.entityType, 2);
			packet.write(PacketType.OUT_ENTITY_SPAWN.x, (int) Math.floor(x * 32));
			packet.write(PacketType.OUT_ENTITY_SPAWN.y, (int) Math.floor(y * 32));
			packet.write(PacketType.OUT_ENTITY_SPAWN.z, (int) Math.floor(z * 32));
			
			packetMeta.getIntegers().write(0, a.getEntityId());
			
			WrappedDataWatcher watcher = new WrappedDataWatcher();
			watcher.setObject(0, (byte) 0);
			watcher.setObject(10, item);
			watcher.setObject(12, 0);
			
			packetMeta.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
			
			PacketUtil.broadcastPacket(packet, false);
			ProtocolLibrary.getProtocolManager().broadcastServerPacket(packetMeta);
			
		} catch (Exception ex) {}
		
	}

}
