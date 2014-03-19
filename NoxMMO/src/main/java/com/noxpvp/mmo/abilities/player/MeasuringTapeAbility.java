package com.noxpvp.mmo.abilities.player;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
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
						return true;
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
							fakeBlock(b.getLocation(), Material.WOOL, 14);
							
							firstDone = true;
						} else {
							b = LineOfSightUtil.getTargetBlock(player, 10, (Set<Material>) null);
							if (b == null)
								return;
							
							blocks[1] = b;
							fakeBlock(b.getLocation(), Material.WOOL, 14);
							
							unRegisterHandler(this);
							Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
								
								public void run() {
									MeasuringTapeAbility.this.refreshBlocks();
								}
							}, 20 * 5);
							
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
		
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), 20 * 120);
		return true;

	}

	private void fakeBlock(Location loc, Material mat, int data) {
		CommonPacket commonFakeBlock = new CommonPacket(35, true);
		
		try {
			commonFakeBlock.write(0, loc.getX());//need to test and use *32?
			commonFakeBlock.write(1, loc.getY());
			commonFakeBlock.write(2, loc.getZ());
			commonFakeBlock.write(3, mat.getId());
			commonFakeBlock.write(4, data);
			
			PacketUtil.sendPacket(getPlayer(), commonFakeBlock);
			
		} catch (IllegalArgumentException e) {e.printStackTrace();}
	}
	
	private void refreshBlocks(){
		for (Block b : blocks){
			if (b == null)
				continue;
			
			fakeBlock(b.getLocation(), b.getType(), b.getData());
		}
		
	}

}
