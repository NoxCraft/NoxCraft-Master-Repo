package com.noxpvp.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.manager.CorePlayerManager;

public class DeathMessageListener extends NoxListener<NoxCore> {

	private CorePlayerManager pm;

	public DeathMessageListener(NoxCore core) {
		super(core);

		this.pm = CorePlayerManager.getInstance();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent event) {

		StringBuilder message = new StringBuilder(event.getDeathMessage());
		int curIndex = 0;

		NoxPlayer p;
		if ((p = pm.getPlayer(event.getEntity())) == null) return;

		String playerFullName = p.getFullName();
		String playerName = p.getName();

		int index1 = message.indexOf(playerName);
		if (index1 != -1)
			message.replace(index1, index1 + playerName.length(), playerFullName + GlobalLocale.CHAT_COLOR_GLOBAL);

		curIndex = index1 + playerName.length();
		Player player = p.getPlayer();

		if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();

			//Get damager if there is one
			Entity damager;
			if ((damager = damageEvent.getDamager()) == null || !(damager instanceof LivingEntity)) return;

			if (damager instanceof Player) {

				//Cast as player
				Player playerDamager = (Player) damager;

				//Replace player name
				NoxPlayer damagerPlayer;
				if ((damagerPlayer = pm.getPlayer(playerDamager)) == null) return;

				String damagerName = p.getName();
				String damagerFullName = damagerPlayer.getFullName();

				int index2 = message.indexOf(damagerName);
				if (index2 != -1) {
					message.replace(index2, index2 + damagerName.length(), ChatColor.RED + damagerFullName);
					curIndex = index2 + damagerFullName.length();
				}

				ItemStack itemInHand;
				if ((itemInHand = playerDamager.getEquipment().getItemInHand()) != null) {
					if (itemInHand.getItemMeta().hasDisplayName())
						message.replace(curIndex, message.length(), "using a" + itemInHand.getItemMeta().getDisplayName());
					else
						message.replace(curIndex, message.length(), "using a" + itemInHand.getType().name().toLowerCase());

				}

			} else if (damager instanceof LivingEntity) {

				//Cast as living
				LivingEntity livingDamager = (LivingEntity) damager;

				//Replace type-name
				String type = livingDamager.getType().name().toLowerCase();
				int index2 = message.toString().toLowerCase().indexOf(type);
				if (index2 != -1) {
					message.replace(index2, index2 + type.length(), ChatColor.RED + type);
					curIndex = index2 + type.length();
				}

			}

		}

	}

}
