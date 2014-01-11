package com.noxpvp.mmo.abilities.player;

import java.util.LinkedList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutEntityEquipment;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.BasePlayerClass;
import com.noxpvp.mmo.classes.PlayerClass;

public class ClassArmorDisguiseAbility extends BasePlayerAbility implements PassiveAbility{

	public final static String ABILITY_NAME = "Armor Disguise";
	public final static String PERM_NODE = "armor-disguise";
	
	private CommonPacket packet;
	
	public ClassArmorDisguiseAbility(Player p, CommonPacket packet) {
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
				
				PlayerClass clazz = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer()).getMainPlayerClass();
				if (clazz == null)
					return false;
				
				LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
				LinkedList<Integer> RBG = BasePlayerClass.getArmorColor(clazz.getId());
				
				meta.setColor(Color.fromRGB(RBG.get(0), RBG.get(1), RBG.get(2)));
				
				if (clazz.getLevel() == clazz.getLevelCap())
                	meta.addEnchant(Enchantment.OXYGEN, 1, true);//fake enchant for maxed tiers
                
                stack.setItemMeta(meta);packet.write(nms.item, stack);
                
			} else return false;
		} else return false;
		
		return true;
		
	}

}
