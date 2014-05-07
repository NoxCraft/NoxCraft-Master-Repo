package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

/**
 * @author NoxPVP
 *
 */
public class SuperBreakerAbility extends BasePlayerAbility {
	
	public static final String PERM_NODE = "super-breaker";
	public static final String ABILITY_NAME = "Super Breaker";
	
	private BaseMMOEventHandler<BlockDamageEvent> instaBreakHandler;

	public SuperBreakerAbility(final Player player){
		super(ABILITY_NAME, player);
		
		this.instaBreakHandler = new BaseMMOEventHandler<BlockDamageEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("BlockDamageEvent").toString(),
				EventPriority.LOW, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<BlockDamageEvent> getEventType() {
				return BlockDamageEvent.class;
			}
			
			public String getEventName() {
				return "BlockDamageEvent";
			}
			
			public void execute(BlockDamageEvent event) {
				if (!event.getPlayer().equals(player))
					return;
				
				if (!mayExecute() || !player.isValid()){
					unRegisterHandler(this);
					return;
				}
				
				Material type = event.getBlock().getType();
				if (!isInstaBreakable(type))
					return;
				
				event.setInstaBreak(true);
				
			}
		};
	}
	
	protected boolean isInstaBreakable(Material type) {
		switch (type) {
		case STONE:
		case COBBLESTONE:
		case SANDSTONE:
		case OBSIDIAN:
		case ENDER_STONE:
		case GLOWSTONE:
		case MOSSY_COBBLESTONE:
		case NETHER_BRICK:
		case NETHERRACK:
			return true;

		default:
			return false;
		}
	}

	public SuperBreakerAbility(NoxPlayerAdapter adapt){
		this(adapt.getNoxPlayer().getPlayer());
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		IPlayerClass pClass = PlayerManager.getInstance().getPlayer(p).getPrimaryClass();
		
		int length = (20 * pClass.getTotalLevel()) / 16;
		
		registerHandler(instaBreakHandler);
		new UnregisterMMOHandlerRunnable(instaBreakHandler).runTaskLater(NoxMMO.getInstance(), length);
		
		return true;
	}

}
