package com.noxpvp.core.gui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.packet.PacketSounds;

public abstract class CoreBox extends NoxListener<NoxCore> implements ICoreBox, Cloneable {
	
	private Map<Integer, CoreBoxItem> menuItems;
	
	public Runnable closeRunnable;
	private CorePlayerManager pm;
	private String name;
	private Inventory box;
	private CoreBox backButton;
	private Reference<Player> p;
	private final String pName;
	
	public CoreBox(Player p, String name, int size, @Nullable CoreBox backButton){
		this(p, name, size, backButton, NoxCore.getInstance());
	}
	
	public CoreBox(Player p, String name, int size) {
		this(p, name, size, null, NoxCore.getInstance());
	}
	
	public CoreBox(final Player p, String name, int size, @Nullable CoreBox backButton, NoxCore core) {
		super(core);
		
		this.pm = CorePlayerManager.getInstance();
		this.p = new WeakReference<Player>(p);
		
		this.box = Bukkit.getServer().createInventory(null, size, (this.name = name));
		this.menuItems = new HashMap<Integer, CoreBoxItem>();
		pName = p.getName();
		
		if (backButton != null) {
			this.backButton = backButton;
			
			ItemStack button = new ItemStack(Material.ARROW);
			ItemMeta meta = button.getItemMeta();
			
			meta.setDisplayName(ChatColor.GOLD + name);
			meta.setLore(Arrays.asList(ChatColor.AQUA + "<- Go Back To The \"" + ChatColor.GOLD + backButton.getName() + ChatColor.AQUA + "\" Menu"));
			
			button.setItemMeta(meta);
			
			this.box.setItem(this.box.getSize() - 1, button);
		}
		
		this.closeRunnable = new Runnable() {
			final CoreBox thisBox = CoreBox.this;	
			
			public void run() { 
				Player p;
				if ((p = getPlayer()) != null && box.getViewers().contains(p))
					p.closeInventory();
				
				NoxPlayer np = pm.getPlayer(p);
				if (np.hasCoreBox(thisBox))
					np.deleteCoreBox();
				
				thisBox.unregister();
				box.clear();
				menuItems = null;
			}
		};
		
	}
	
	public String getName() {
		return name;
	}
	
	public Inventory getBox() {
		return box;
	}
	
	public Player getPlayer() {
		return p == null? null : p.get() == null? null : p.get();
	}
	
	public boolean isValid() {
		Player p = getPlayer();
		return p != null && p.isValid() && pm.getPlayer(p).hasCoreBox(this);
	}
	
	public boolean fixReference() {
		Player player = Bukkit.getPlayer(pName);
		if (player == null)
			return false;
		
		this.p = new WeakReference<Player>(player);
		
		return true;
	}

	public void show() {
		Player p;
		if ((p = getPlayer()) == null)
			return;
		
		pm.getPlayer(p).setCoreBox(this);
		p.openInventory(box);

		register();
	}
	
	public void hide() {
		if (isValid())
			CommonUtil.nextTick(closeRunnable);
		
		return;
	}
	
	public boolean addMenuItem(int slot, CoreBoxItem item) {
		box.setItem(slot, item.getItem());
		menuItems.put(slot, item);
		
		ItemStack checkNull;
		return (checkNull = box.getItem(slot)) != null && checkNull.equals(item.getItem());
	}
	
	public boolean removeMenuItem(CoreBoxItem item) {
		return box.removeItem(item.getItem()) == null && this.menuItems.values().remove(item);
	}
	
	public void removeMenuItem(int slot) {
		box.setItem(slot, null);
		
		return;
	}
	
	public CoreBoxItem getMenuItem(CoreBoxItem item) {
		for(CoreBoxItem menuItem : menuItems.values()) {
			if(menuItem.equals(item)) {
				return menuItem;
			} else continue;
		}
		
		return null;
	}
	
	public CoreBoxItem getMenuItem(int slot) {
		try {
			return menuItems.get(slot);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onClick(InventoryClickEvent event) {
		if (!isValid()) {
			hide();
			
			return;
		}
		
		if (!event.getInventory().equals(box)) {
			return;
		}
		
		Player player = p.get();
		if (player == null)
			return;	
		
		event.setCancelled(true);
		player.updateInventory();
		
		ItemStack clickedItem = event.getCurrentItem();
		if (event.getRawSlot() < (box.getSize() - 1)) {
			if (clickedItem != null && clickedItem.getType() != Material.AIR) {
				
				CoreBoxItem item;
				if((item = getMenuItem(event.getRawSlot())) != null)
					if (item.onClick(event))
						StaticEffects.playSound(player, PacketSounds.RandomClick);
					else
						StaticEffects.PlaySound(player, player.getLocation(), PacketSounds.RandomAnvilLand, 1, +2);
			}
		} else if (backButton != null && event.getRawSlot() == (box.getSize() - 1)){
			try {
				((CoreBox) backButton.clone()).show();
				StaticEffects.playSound(player, PacketSounds.RandomClick);
			} catch (CloneNotSupportedException e) {}
			
			return;
		}
		
		this.clickHandler(event);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onClose(InventoryCloseEvent event) {
		if (!isValid()) {
			hide();
			
			return;
		}
		
		if (event.getInventory().equals(box))
			this.closeHandler(event);
	}
	
	public void clickHandler(InventoryClickEvent event) {
		
	}
	
	public void closeHandler(InventoryCloseEvent event) {
		
	}

}
