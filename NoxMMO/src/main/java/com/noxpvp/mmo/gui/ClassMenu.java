package com.noxpvp.mmo.gui;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.packet.PacketSoundEffects;
import com.noxpvp.core.utils.StaticEffects;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;

public class ClassMenu extends CoreBox {

	public final static String MENU_NAME = "Class Info";
	private final static int size = 45;
	
	private CoreBox previousBox;
	private PlayerClass clazz;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ClassMenu(getPlayer(), getPlayerClass(), this.previousBox);
	}
	
	public ClassMenu(final Player p, PlayerClass clazz, CoreBox backButton){
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);
		
		this.previousBox = backButton;
		this.clazz = clazz;
		
		Inventory box = getBox();
		
		box.setItem(0, clazz.getIdentifingItem());
		
		int i = 2;
		for (Map.Entry<Integer, IClassTier> tier : clazz.getTiers()) {
			IClassTier t = tier.getValue();
			
			boolean canuse = clazz.canUseTier(tier.getKey());
			boolean locked = t.getLevel() >= t.getMaxLevel()? true : false;
			
			ItemStack item = t.getIdentifingItem();
			item.setType(locked? Material.IRON_DOOR : Material.WOODEN_DOOR);
			
			ItemMeta meta = item.getItemMeta();
			String name = meta.getDisplayName() + " | " + (locked? ChatColor.DARK_RED + "LOCKED" : (canuse? ChatColor.GREEN + "OPEN" : ChatColor.RED + "NOT AVAILIBLE"));
			meta.setDisplayName(name);
			
			item.setItemMeta(meta);
			
			box.setItem(i, item);
			addMenuItem(new ClassMenuItem(this, item, clazz, i) {
				
				public void onClick(InventoryClickEvent click) {
					MMOPlayer mmoPlayer = PlayerManager.getInstance().getPlayer(getPlayer());
						
					PlayerClass clazz = ClassMenu.this.getPlayerClass();
						
					if (clazz.isPrimaryClass() && clazz.canUseClass()){//TODO finish secondarys and only play blaze if can't switch
						mmoPlayer.setPrimaryClass(clazz);
						mmoPlayer.getPrimaryClass().setCurrentTier(getTier());
						
						StaticEffects.PlaySound(p, p.getLocation(), PacketSoundEffects.MobBlazeDeath, (float) 100, 100);
					}
					
				}
			});
			
			i++;
			
		}
		
		i = 20;
		for (Ability ab : clazz.getAbilities()) {
			if (i > 26)
				i = 29;
			else if (i > 35)
				i = 38;
			else if (i > 44)
				throw new UnsupportedOperationException("Too many abilities for the " + getBox().getTitle() + " menu");
			
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(ab.getDisplayName());
			meta.setLore(ab.getLore(ChatColor.GOLD));
			
			item.setItemMeta(meta);
			
			box.setItem(i++, item);
			
		}
		
	}
	
	public PlayerClass getPlayerClass(){
		return clazz;
	}
	
	public abstract class ClassMenuItem extends CoreBoxItem{

		private int tier;
		private PlayerClass clazz;

		public ClassMenuItem(ClassMenu parent, ItemStack item, PlayerClass clazz, int tier) {
			super(parent, item);
			
			this.clazz = clazz;
			this.tier = tier;
		}
		
		public PlayerClass getPlayerClass(){
			return clazz;
		}
		
		public int getTier(){
			return tier;
		}
	}

}
