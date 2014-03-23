package com.noxpvp.mmo.abilities.player;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxPacketListener;
import com.noxpvp.core.utils.EffectsRunnable;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public class MedPackAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Med Pack";
	public final static String PERM_NODE = "med-pack";
	
	private BaseMMOEventHandler<PlayerPickupItemEvent> handler;
	private BaseMMOEventHandler<InventoryPickupItemEvent> inventoryPickupHandler;
	private NoxPacketListener camoflouge;
	
	private int health;
	private boolean isActive;
	private Item pack;
	
	public MedPackAbility setActive(boolean active){
		this.isActive = active;
		
		if (active) {
			registerHandler(handler);
			registerHandler(inventoryPickupHandler);
			camoflouge.register();
		} else {
			unRegisterHandler(handler);
			unRegisterHandler(inventoryPickupHandler);
			camoflouge.unRegister();
		}
		return this;
	}
	
	public boolean isActive() { return this.isActive; }

	public int getHealth() { return health; }

	public MedPackAbility setHealth(int health) { this.health = health; return this; }

	public MedPackAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.handler = new BaseMMOEventHandler<PlayerPickupItemEvent>(new StringBuilder().append(ABILITY_NAME).append(player.getName()).append("PlayerPickupItemEvent").toString(), EventPriority.NORMAL, 1) {
			
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
				if (!isValid()) {
					setActive(false);
					return;
				}
				
				Item eventItem = event.getItem();
				if (pack != eventItem)
					return;
				
				event.setCancelled(true);
				
				Player abilPlayer = getPlayer();
				if (event.getPlayer() == abilPlayer)
					return;
				
				eventItem.remove();
				Player player = event.getPlayer();
				
				player.setHealth(player.getHealth() + getHealth());
				
				EffectsRunnable hearts = new EffectsRunnable(Arrays.asList("heart"), false, player.getEyeLocation(), 0, 3, 2, null);
				hearts.runTask(NoxMMO.getInstance());
				
				MessageUtil.sendLocale(NoxMMO.getInstance(), player, "ability.medpack.pick-up", player.getName(), (abilPlayer != null? abilPlayer.getName() :  MedPackAbility.this.getNoxPlayer().getName()));
				if (abilPlayer != null && abilPlayer.isOnline())
					MessageUtil.sendLocale(NoxMMO.getInstance(), abilPlayer, "ability.medpack.pick-up.other", player.getName(), MedPackAbility.this.getNoxPlayer().getName());

			}
		};
		
		this.inventoryPickupHandler = new BaseMMOEventHandler<InventoryPickupItemEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("InventoryPickupItemEvent").toString(),
				EventPriority.HIGH, 1) {
			
			public boolean ignoreCancelled() {
				return true;
			}
			
			public Class<InventoryPickupItemEvent> getEventType() {
				return InventoryPickupItemEvent.class;
			}
			
			public String getEventName() {
				return "InventoryPickupItemEvent";
			}
			
			public void execute(InventoryPickupItemEvent event) {
				if (event.getItem() == pack)
					event.setCancelled(true);
				
				if (!isValid())
					setActive(false);
				
				return;
			}
		};
		
		this.camoflouge = new NoxPacketListener(PacketType.OUT_ENTITY_METADATA) {
			
			public void onPacketSend(PacketSendEvent arg0) {
				CommonPacket packet = arg0.getPacket();
				
				if (packet.read(PacketType.OUT_ENTITY_METADATA.entityId) != pack.getEntityId())
					return;
				
				try {
					
					DataWatcher dw = new DataWatcher(packet.read(PacketType.OUT_ENTITY_METADATA.watchedObjects));
					System.out.println(dw.get(0));
					
					ItemStack stack = new ItemStack((ItemStack) dw.get(0));
					stack.setType(Material.EMERALD);
					
					dw.set(10, stack);
					
				} catch (Exception e) { e.printStackTrace(); }
			}
			
			public void onPacketReceive(PacketReceiveEvent arg0) { return; }
			
			@Override
			public NoxPlugin getPlugin() {
				return NoxMMO.getInstance();
			}
		};
		
		this.health = 8;
		this.isActive = false;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		ItemStack medPack = new ItemStack(Material.DIRT, 1);
		ItemMeta meta = medPack.getItemMeta();
		
		meta.setDisplayName(ChatColor.RED + "DO_NOT_USE");
		meta.setLore(null);
		
		medPack.setItemMeta(meta);
		
		this.pack = p.getWorld().dropItem(p.getLocation(), medPack);
		
		new EffectsRunnable(Arrays.asList("happyVillager"), true, null, 1F, 10, 0, this.pack).runTaskTimer(NoxMMO.getInstance(), 0, 10);
		setActive(true);
		MessageUtil.sendLocale(NoxMMO.getInstance(), p, "ability.medpack.use");

		return true;
	}

}
