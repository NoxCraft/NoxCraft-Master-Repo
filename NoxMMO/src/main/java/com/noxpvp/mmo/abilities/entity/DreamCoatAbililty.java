package com.noxpvp.mmo.abilities.entity;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutEntityEquipment;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class DreamCoatAbililty extends BaseEntityAbility{
	
	public final static String ABILITY_NAME = "Dream Coat";
	public final static String PERM_NODE = "dream-coat";
	
	@Override
	public String getDescription() {
		return "The coat of much technicolor";
	}
	
	private static WeakHashMap<LivingEntity, DreamCoatRunnable> runnables = new WeakHashMap<LivingEntity, DreamCoatAbililty.DreamCoatRunnable>();
	private static SortedMap<Long, Color> colours = new TreeMap<Long, Color>(Collections.reverseOrder());
	
	private void setupColors() {
		int[] values = new int[]{0, 32, 64, 96, 128, 160, 192, 224, 255};
		for (int r : values) {
			for (int g : values) {
				for (int b : values) {
					long brightness = (r + g + b) / 3;
					long colourScore = (brightness << 24) + (r << 8) + (g << 16) + b;
					colours.put(colourScore, Color.fromRGB(r, g, b));
				}
			}
		}
		
	}
	
	private boolean anyArmor;

	public boolean isAnyArmor() { return anyArmor; }
	public DreamCoatAbililty setAnyArmor(boolean anyArmor) { this.anyArmor = anyArmor; return this; }

	public DreamCoatAbililty(Entity e) {
		super(ABILITY_NAME, e);
		
		setupColors();
		this.setAnyArmor(false);
	}

	public boolean execute() {
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		LivingEntity a = (LivingEntity) getEntity();
		
		if (runnables.containsKey(a)){
			while (runnables.containsKey(a)) {
				runnables.get(a).safeCancel();
				runnables.remove(a);
			}			
			sendFakeArmor(a, null);
			
			return false;
		}
		
		System.out.println("starting");
		DreamCoatRunnable b = new DreamCoatRunnable(a);
		runnables.put(a, b);
		
		b.runTaskTimer(NoxMMO.getInstance(), 0, 1);
		return true;
	}
	
	public void sendFakeArmor(LivingEntity e, @Nullable Color color){
		
		CommonPacket packet;
		NMSPacketPlayOutEntityEquipment nms = new NMSPacketPlayOutEntityEquipment();
		
		int slot;
		for (ItemStack i : e.getEquipment().getArmorContents()){
			switch (i.getType()) {
			case LEATHER_BOOTS:
				slot = 1;
				break;
			case LEATHER_LEGGINGS:
				slot = 2;
				break;
			case LEATHER_CHESTPLATE:
				slot = 3;
				break;
			case LEATHER_HELMET:
				slot = 4;
				break;
			default:
				continue;
			}
			
			packet = new CommonPacket(PacketType.OUT_ENTITY_EQUIPMENT);
			
			ItemStack item = i.clone();
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
			
			if (color != null){
				meta.setColor(color);
				item.setItemMeta(meta);
			}					
			
			packet.write(nms.entityId, e.getEntityId());
			packet.write(nms.item, item);
			packet.write(nms.slot, slot);
			
			for (Entity it : e.getNearbyEntities(75, 100, 75))
				if (it instanceof Player)
					PacketUtil.sendPacket((Player) it, packet, false);
			
		}
	}
	
	private class DreamCoatRunnable extends BukkitRunnable{
		
		private LivingEntity e;
		private Cycler<Color> colors = new Cycler<Color>(colours.values());
	
		public void safeCancel(){
			try {
				this.cancel();
				return;
			} catch (IllegalStateException e) {}
		}
		
		public DreamCoatRunnable(LivingEntity e){
			this.e = e;
		}

		public void run() {

			if (e == null || !e.isValid() || e.isDead())
				safeCancel();
			
			sendFakeArmor(e, colors.next());
			
		}
		
	}

}
