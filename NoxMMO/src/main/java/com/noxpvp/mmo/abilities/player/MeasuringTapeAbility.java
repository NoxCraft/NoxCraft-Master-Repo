package com.noxpvp.mmo.abilities.player;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.avaje.ebeaninternal.server.cluster.Packet;
import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.utils.BlockChangeArray;
import com.noxpvp.core.utils.BlockChangeArray.BlockChange;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class MeasuringTapeAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Measuring Tape";
	public final static String PERM_NODE = "measuring-tape";
	
	private BaseMMOEventHandler<PlayerInteractEvent> handler;
	
	private Block[] blocks;
	private Block b;
	private boolean firstDone;

	public MeasuringTapeAbility(final Player player) {
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<PlayerInteractEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("PlayerInteractEvent").toString(),
				EventPriority.MONITOR, 1) {

					public boolean ignoreCancelled() {
						return false;
					}

					public void execute(PlayerInteractEvent event) {
						if (!mayExecute()){
							unRegisterHandler(this);
							return;
						}
						if (!event.getPlayer().equals(player))
							return;
						
						
						if (!firstDone){
							b = LineOfSightUtil.getTargetBlock(player, 10, (Set<Material>) null);
							if (b == null)
								return;
							
							blocks[0] = b;
							fakeBlock(b.getLocation(), Material.WOOL);
							
							firstDone = true;
							return;
						} else {
							b = LineOfSightUtil.getTargetBlock(player, 10, (Set<Material>) null);
							if (b == null)
								return;
							
							blocks[1] = b;
							fakeBlock(b.getLocation(), Material.WOOL);
							
							unRegisterHandler(this);
							Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
								
								public void run() {
									MeasuringTapeAbility.this.refreshBlocks();
								}
							}, 20 * 5);
							
							return;
						}
						
					}

					public Class<PlayerInteractEvent> getEventType() {
						return PlayerInteractEvent.class;
					}

					public String getEventName() {
						return "PlayerInteractEvent";
					}
		};
		
		this.blocks = new Block[2];
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), 20 * 120);
		return true;

	}

	@SuppressWarnings("deprecation")
	private void fakeBlock(Location loc, Material mat) {
		CommonPacket fakeBlock = new CommonPacket(PacketType.OUT_MULTI_BLOCK_CHANGE);
		BlockChangeArray change = new BlockChangeArray(1);
		
		change.getBlockChange(0).
		setRelativeX((int) loc.getX()).
		setRelativeZ((int) loc.getZ()).
		setAbsoluteY((int) loc.getY()).
		setBlockID(mat.getId());
		
		fakeBlock.write(PacketType.OUT_MULTI_BLOCK_CHANGE.chunk, new IntVector2(loc.getChunk()));
		fakeBlock.write(PacketType.OUT_MULTI_BLOCK_CHANGE.blockCount, 1);
		fakeBlock.write(PacketType.OUT_MULTI_BLOCK_CHANGE.blockData, change.toByteArray());
		
		PacketUtil.broadcastPacket(fakeBlock, false);
	}
	
	private void refreshBlocks(){
		for (Block b : blocks){
			if (b == null)
				continue;
			
			fakeBlock(b.getLocation(), b.getType());
		}
		
	}

}
