package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutEntityEquipment;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class ClassArmorDisguisePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Armor Disguise";
	public static final String PERM_NODE = "armor-disguise";

	private CommonPacket packet;

	public ClassArmorDisguisePlayerAbility(Player p, CommonPacket packet) {
		super(ABILITY_NAME, p);

		this.packet = packet;
	}

	public boolean execute() {
		NMSPacketPlayOutEntityEquipment nms = new NMSPacketPlayOutEntityEquipment();

		ItemStack stack = packet.read(nms.item);

		if (stack != null && packet.read(nms.slot) != 0) {
			String name = stack.getType().name();
			stack.setType(Material.AIR);

			if (name.contains("HELMET")) {
				stack.setType(Material.LEATHER_HELMET);

			} else if (name.contains("CHESTPLATE")) {
				stack.setType(Material.LEATHER_CHESTPLATE);

			} else if (name.contains("LEGGING")) {
				stack.setType(Material.LEATHER_LEGGINGS);

			} else if (name.contains("BOOT")) {
				stack.setType(Material.LEATHER_BOOTS);

			}

			if (stack.getType() != Material.AIR) {

				IPlayerClass clazz = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
				if (clazz == null)
					return false;

				LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();

				if (clazz.getRBGColor() != null)
					meta.setColor(clazz.getRBGColor());

				if (clazz.getLevel() == clazz.getMaxLevel())
					meta.addEnchant(Enchantment.OXYGEN, 1, true);//fake enchant for maxed tiers

				stack.setItemMeta(meta);
				packet.write(nms.item, stack);

			} else return false;
		} else return false;

		return true;

	}

}
