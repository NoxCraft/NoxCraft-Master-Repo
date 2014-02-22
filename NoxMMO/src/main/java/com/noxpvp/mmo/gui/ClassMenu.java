package com.noxpvp.mmo.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.IClassTier;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;

public class ClassMenu extends CoreBox{

	public final static String MENU_NAME = MMOLocale.GUI_MENU_NAME_COLOR + "Class Info";
	private final static int size = 4;
	
	private Map<Integer, ClassMenuItem> menuItems;
	private PlayerClass clazz;
	
	public ClassMenu(final Player p, PlayerClass clazz, CoreBox backButton){
		super(p, MENU_NAME, size, backButton);
		
		this.menuItems = new HashMap<Integer, ClassMenuItem>();
		this.clazz = clazz;
		
		Inventory box = getBox();
		ItemStack item;
		ItemMeta meta;
		
		box.setItem(0, clazz.getIdentifingItem());
		
		int i = 2;
		for (Map.Entry<Integer, IClassTier> tier : clazz.getTiers()) {
			IClassTier t = tier.getValue();
			
			boolean canuse = clazz.canUseTier(tier.getKey());
			boolean locked = t.getLevel() >= t.getMaxLevel()? true : false;
			
			item = new ItemStack(locked? Material.IRON_DOOR : Material.WOODEN_DOOR);
			meta = item.getItemMeta();
			
			String name = t.getDisplayName() + (locked? ChatColor.RED + "LOCKED" : (canuse? ChatColor.GREEN + "OPEN" : ChatColor.RED + "NOT AVAILIBLE"));
			
			meta.setDisplayName(name);
			meta.setLore(t.getLore());
			item.setItemMeta(meta);
			
			menuItems.put(i++, new ClassMenuItem(this, item, clazz, i) {
				
				public void onClick(InventoryClickEvent click) {
					if (click.getAction() != InventoryAction.PICKUP_ALL && click.getAction() != InventoryAction.COLLECT_TO_CURSOR)
						return;
					
					ClassMenuItem item;
					if ((item = menuItems.get(click.getSlot())) != null && item.getItem() == click.getCurrentItem()){
						MMOPlayer mmoPlayer = PlayerManager.getInstance().getPlayer(getPlayer());
						
						PlayerClass clazz = ClassMenu.this.getPlayerClass();
						
						if (clazz.isPrimaryClass()){
							mmoPlayer.setPrimaryClass(clazz);
							mmoPlayer.getPrimaryClass().setCurrentTier(getTier());
						}							
						
					}
					
				}
			});
			
		}
		
	}
	
	public PlayerClass getPlayerClass(){
		return clazz;
	}

	public void clickHandler(InventoryClickEvent event) {}

	public void closeHandler(InventoryCloseEvent event) {}
	
	public abstract class ClassMenuItem extends CoreBoxItem{

		private int tier;
		private PlayerClass clazz;

		public ClassMenuItem(ClassMenu parent, ItemStack item, PlayerClass clazz, int tier) {
			super(parent, item);
			
			this.clazz = clazz;
			this.tier = tier;
		}
		
		@Override
		public ClassMenu getParentBox(){
			return getParentBox();
		}

		public PlayerClass getPlayerClass(){
			return clazz;
		}
		
		public int getTier(){
			return tier;
		}
	}

}
