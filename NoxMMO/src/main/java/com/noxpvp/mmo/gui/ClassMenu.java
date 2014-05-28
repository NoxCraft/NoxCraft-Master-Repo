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

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.classes.internal.ClassTier;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;

public class ClassMenu extends CoreBox {

	public static final String MENU_NAME = "Class";
	private static final int size = 45;

	private CoreBox previousBox;
	private PlayerClass clazz;

	public ClassMenu(final Player p, PlayerClass clazz, CoreBox backButton) {
		super(p, clazz.getName() + " " + MENU_NAME, size, backButton);

		this.previousBox = backButton;
		this.clazz = clazz;

		Inventory box = getBox();

		box.setItem(0, clazz.getIdentifiableItem());

		CoreBoxRegion tiers = new CoreBoxRegion(this, new Vector(0, 0, 2), 0, 7),
				abilities = new CoreBoxRegion(this, new Vector(2, 0, 0), 1, 9);

		final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(p);

		for (Map.Entry<Integer, IClassTier> tier : clazz.getTiers()) {
			if (!(tier.getValue() instanceof ClassTier))
				continue;

			ClassTier t = (ClassTier) tier.getValue();

			boolean canuse = clazz.canUseTier(tier.getKey());
//			boolean locked = t.getLevel() >= t.getMaxLevel()? true : false;//TODO When adding exp
			boolean hasTier = mmoPlayer.getPrimaryClass().getTier() == t ||
					mmoPlayer.getSecondaryClass().getTier() == t;

			ItemStack item = t.getIdentifiableItem();
			if (hasTier)
				item.setType(Material.SKULL);

			ItemMeta meta = item.getItemMeta();
//			String name = meta.getDisplayName() + " | " + (locked? ChatColor.DARK_RED + "LOCKED" : (canuse? ChatColor.GREEN + "OPEN" : ChatColor.RED + "NOT AVAILIBLE"));//TODO When adding exp
			String name = meta.getDisplayName() + " " + (canuse ? ChatColor.GREEN + "[OPEN" : ChatColor.RED + "[NOT AVAILIBLE") + "]";
			meta.setDisplayName(name);

			item.setItemMeta(meta);

			tiers.add(new ClassMenuItem(this, item, clazz, t.getTierLevel()) {

				public boolean onClick(InventoryClickEvent click) {
					PlayerClass clazz = ClassMenu.this.getPlayerClass();

					if (!clazz.canUseClass()) {
						MessageUtil.sendLocale(p, MMOLocale.CLASS_LOCKED, clazz.getDisplayName(), "Insufficient Permissions");
						return false;
					}

					if (clazz.isPrimaryClass()) {
						mmoPlayer.setPrimaryClass(clazz);
						mmoPlayer.getPrimaryClass().setCurrentTier(getTier());
					} else {
						mmoPlayer.getSecondaryClass().setCurrentTier(getTier());
						mmoPlayer.setSecondaryClass(clazz);
					}

					StaticEffects.SmokeScreen(p.getEyeLocation(), 2);
					hide();
					return true;
				}
			});

		}

		for (Ability ab : clazz.getTier(clazz.getHighestPossibleTier()).getAbilities()) {

			ItemStack item = ab.getIdentifiableItem(clazz.getAbilities().contains(ab));

			abilities.add(new CoreBoxItem(this, item) {
				public boolean onClick(InventoryClickEvent click) {
					return true;
				}
			});

		}

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ClassMenu(getPlayer(), getPlayerClass(), this.previousBox);
	}

	public PlayerClass getPlayerClass() {
		return clazz;
	}

	public abstract class ClassMenuItem extends CoreBoxItem {

		private int tier;
		private PlayerClass clazz;

		public ClassMenuItem(ClassMenu parent, ItemStack item, PlayerClass clazz, int tier) {
			super(parent, item);

			this.clazz = clazz;
			this.tier = tier;
		}

		public PlayerClass getPlayerClass() {
			return clazz;
		}

		public int getTier() {
			return tier;
		}
	}

}
