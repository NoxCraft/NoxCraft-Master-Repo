package com.noxpvp.mmo.abilities.entity;

import java.lang.ref.WeakReference;
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
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class DreamCoatAbililty extends BaseEntityAbility{
	
	public final static String ABILITY_NAME = "Dream Coat";
	public final static String PERM_NODE = "dream-coat";
	
	public static WeakHashMap<WeakReference<LivingEntity>, DreamCoatRunnable> runnables = new WeakHashMap<WeakReference<LivingEntity>, DreamCoatAbililty.DreamCoatRunnable>();
	
	private boolean anyArmor;

	public boolean isAnyArmor() { return anyArmor; }
	public DreamCoatAbililty setAnyArmor(boolean anyArmor) { this.anyArmor = anyArmor; return this; }

	public DreamCoatAbililty(Entity e) {
		super(ABILITY_NAME, e);
		
		this.setAnyArmor(false);
	}

	public boolean execute() {
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		LivingEntity a = (LivingEntity) getEntity();
		
		if (runnables.containsKey(a)){
			runnables.get(a).safeCancel();
			
			sendFakeArmor(a, null);
			
//			if (a instanceof Player)
//				((Player) a).sendMessage(MMOLocale.);
			
			return false;
		}
		
		DreamCoatRunnable b = new DreamCoatRunnable(a);
		runnables.put(new WeakReference<LivingEntity>((LivingEntity) a), b);
		
		b.runTaskTimer(NoxMMO.getInstance(), 0, 5);
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
		private int r = 0, g = 0, b = 0;
	
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
			
			sendFakeArmor(e, Color.fromRGB(r, g, b));
			
			if (b < 255)
				b++;
			else if (g < 255){
				g++;
				b = 0;
			} else if (r < 255){
				r++;
				g = 0;
				b = 0;
			} else {
				b = 0;
				g = 0;
				r = 0;
			}
			
		}
		
	}

}
