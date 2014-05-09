package com.noxpvp.mmo.gui;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.core.packet.PacketSoundEffects;
import com.noxpvp.core.utils.StaticEffects;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.classes.internal.ClassTier;
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
		
		box.setItem(0, clazz.getIdentifibleItem());
		
		CoreBoxRegion tiers = new CoreBoxRegion(this, new Vector(0, 0, 2), 0, 7),
				abilities = new CoreBoxRegion(this, new Vector(2, 0, 0), 1, 9);
		
		for (Map.Entry<Integer, IClassTier> tier : clazz.getTiers()) {
			if (!(tier.getValue() instanceof ClassTier))
				continue;
			
			ClassTier t = (ClassTier) tier.getValue();
			
			boolean canuse = clazz.canUseTier(tier.getKey());
			boolean locked = t.getLevel() >= t.getMaxLevel()? true : false;
			
			ItemStack item = t.getIdentifibleItem();
			item.setType(locked? Material.IRON_DOOR : Material.WOODEN_DOOR);
			
			ItemMeta meta = item.getItemMeta();
			String name = meta.getDisplayName() + " | " + (locked? ChatColor.DARK_RED + "LOCKED" : (canuse? ChatColor.GREEN + "OPEN" : ChatColor.RED + "NOT AVAILIBLE"));
			meta.setDisplayName(name);
			
			item.setItemMeta(meta);
			
			tiers.add(new ClassMenuItem(this, item, clazz, t.getTierLevel()) {
				
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
			
		}
		
		for (Ability ab : clazz.getAbilities()) {
			
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta meta = item.getItemMeta();
			
			meta.setDisplayName(ab.getDisplayName(clazz.getColor()));
			meta.setLore(ab.getLore(ChatColor.GOLD));
			
			item.setItemMeta(meta);
			
			abilities.add(new CoreBoxItem(this, item) {
				public void onClick(InventoryClickEvent click) { return; }
			});
			
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
