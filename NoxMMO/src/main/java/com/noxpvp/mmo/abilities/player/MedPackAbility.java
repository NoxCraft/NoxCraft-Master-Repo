package com.noxpvp.mmo.abilities.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class MedPackAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Med Pack";
	public final static String PERM_NODE = "med-pack";
	public static Map<Item, String> packs = new HashMap<Item, String>();
	
	private void EventExecute(PlayerPickupItemEvent event){
		Player player = getPlayer();
		Item item = event.getItem();
		
		if (MedPackAbility.packs.containsKey(item)) {
			event.setCancelled(true);				
			if (MedPackAbility.packs.get(item) != player.getName()){
				item.remove();
				
				player.setHealth(player.getHealth() + 6.0);
				
				Location ploc = player.getLocation();
				ploc.setY(ploc.getY() + 2);
				
				EffectsRunnable hearts = new EffectsRunnable("heart", ploc, 0, 1);
				hearts.runTask(NoxMMO.getInstance());
			} else return;
		}
	}
	
	private BaseMMOEventHandler<PlayerPickupItemEvent> handler;
	private int health;

	public int getHealth() {return health;}

	public MedPackAbility setHealth(int health) {
		this.health = health; return this;}

	public MedPackAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<PlayerPickupItemEvent>(new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("ProjectileHitEvent").toString(), EventPriority.NORMAL, 1) {
			
			@Override
			public boolean ignoreCancelled() {
				return true;
			}
			
			@Override
			public Class<PlayerPickupItemEvent> getEventType() {
				return PlayerPickupItemEvent.class;
			}
			
			@Override
			public String getEventName() {
				return "PlayerPickupItemEvent";
			}
			
			@Override
			public void execute(PlayerPickupItemEvent event) {
				if (event.getPlayer().equals(MedPackAbility.this.getPlayer()))
					MedPackAbility.this.EventExecute(event);
			}
		};
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		ItemStack pack = new ItemStack(Material.EMERALD, 1);
		pack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		
		Item drop = p.getWorld().dropItem(p.getLocation(), pack);
		drop.setPickupDelay(30);
		
		MedPackAbility.packs.put(drop, p.getName());
		
		return false;
	}

}
