package com.noxpvp.mmo.abilities.player;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.packet.NoxPacketUtil;
import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class MeasuringTapePlayerAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Measuring Tape";
	public final static String PERM_NODE = "measuring-tape";
	
	private BaseMMOEventHandler<PlayerInteractEvent> handler;
	
	private Block[] blocks;
	private Block b;
	private boolean firstDone;

	public MeasuringTapePlayerAbility(final Player player) {
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<PlayerInteractEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("PlayerInteractEvent").toString(),
				EventPriority.MONITOR, 1) {

					public boolean ignoreCancelled() {
						return false;
					}

					public void execute(PlayerInteractEvent event) {
						if (!mayExecute()){
							unregisterHandler(this);
							return;
						}
						if (!event.getPlayer().equals(player))
							return;
						
						
						if (!firstDone){
							b = LineOfSightUtil.getTargetBlock(player, 10, (Set<Material>) null);
							if (b == null)
								return;
							
							blocks[0] = b;
							NoxPacketUtil.fakeBlock(10, Material.WOOL, b.getLocation());
							
							firstDone = true;
							return;
						} else {
							b = LineOfSightUtil.getTargetBlock(player, 10, (Set<Material>) null);
							if (b == null)
								return;
							
							blocks[1] = b;
							NoxPacketUtil.fakeBlock(10, Material.WOOL, b.getLocation());
							
							unregisterHandler(this);
							
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

}
