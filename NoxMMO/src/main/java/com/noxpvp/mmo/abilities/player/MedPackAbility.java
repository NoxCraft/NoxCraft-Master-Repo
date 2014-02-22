package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.utils.chat.MessageUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.listeners.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.EffectsRunnable;

public class MedPackAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Med Pack";
	public final static String PERM_NODE = "med-pack";

	List<Item> packs = new ArrayList<Item>();
	
	private BaseMMOEventHandler<PlayerPickupItemEvent> handler;
	private int health;
	private boolean isActive = false;
	
	public MedPackAbility setActive(boolean active) {
		boolean changed = this.isActive != active;
		
		MasterListener m = NoxMMO.getInstance().getMasterListener();
		
		this.isActive = active;
		
		if (active)
			m.registerHandler(handler);
		else
			m.unregisterHandler(handler);
		
		return this;
	}
	
	public boolean isActive() { return this.isActive; }
	

	public int getHealth() {return health;}

	public MedPackAbility setHealth(int health) {
		this.health = health; return this;}

	public MedPackAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<PlayerPickupItemEvent>(new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("ProjectileHitEvent").toString(), EventPriority.NORMAL, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<PlayerPickupItemEvent> getEventType() {
				return PlayerPickupItemEvent.class;
			}
			
			public String getEventName() {
				return "PlayerPickupItemEvent";
			}
			
			public void execute(PlayerPickupItemEvent event) {
				if (event.getPlayer().equals(MedPackAbility.this.getPlayer())) {
					Item eventItem = event.getItem();
					Player player = event.getPlayer();
					Player abilPlayer = MedPackAbility.this.getPlayer();
					if (MedPackAbility.this.packs.contains(eventItem))
					{
						event.setCancelled(true);
						if (player.equals(MedPackAbility.this.getPlayer()))
							return;
						
						packs.remove(eventItem);
						eventItem.remove();
						
						player.setHealth(player.getHealth() + 6.0);
						
						Location ploc = player.getLocation();
						ploc.setY(ploc.getY() + 2);
						
						EffectsRunnable hearts = new EffectsRunnable("heart", ploc, 0, 1, false, false, null);
						hearts.runTask(NoxMMO.getInstance());
						
						MessageUtil.sendLocale(NoxMMO.getInstance(), player, "ability.medpack.pick-up", player.getName(), (abilPlayer != null? abilPlayer.getName() :  MedPackAbility.this.getNoxPlayer().getName()));
						if (abilPlayer != null && abilPlayer.isOnline())
							MessageUtil.sendLocale(NoxMMO.getInstance(), abilPlayer, "ability.medpack.pick-up.other", player.getName(), MedPackAbility.this.getNoxPlayer().getName());
						
						if (packs.isEmpty())
							setActive(false);
					}
				}
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
		packs.add(drop);
		
		new EffectsRunnable("townaura", null, .1F, 10, true, true, drop).runTaskTimer(NoxMMO.getInstance(), 20, 12);
		if (!packs.isEmpty())
			setActive(true);
		
		if (isActive())
		{
			MessageUtil.sendLocale(NoxMMO.getInstance(), p, "ability.medpack.use");
			return true;
		} else
			return false;
	}

}
