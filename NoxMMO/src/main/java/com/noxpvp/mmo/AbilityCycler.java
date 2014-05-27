package com.noxpvp.mmo;

import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.abilities.Ability;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class AbilityCycler extends Cycler<Ability> implements ConfigurationSerializable {

	public static final String TEMP_PCTK = "ability-cycler.active-count";

	static ConcurrentMap<String, List<AbilityCycler>> cyclers = null;

	private final ItemStack identity;
	private Reference<MMOPlayer> player = null;

	private static NoxListener<NoxMMO> iHeld = null;

	public AbilityCycler(int size, OfflinePlayer player, ItemStack identity) {
		this(size, MMOPlayerManager.getInstance().getPlayer(player), identity);
	}

	public AbilityCycler(Collection<Ability> data, OfflinePlayer player, ItemStack identity) {
		this(data, MMOPlayerManager.getInstance().getPlayer(player), identity);
	}

	public AbilityCycler(int size, MMOPlayer player, ItemStack identity) {
		super(size);
		this.player = new SoftReference<MMOPlayer>(player);
		this.identity = identity;

		register(this);
	}

	public AbilityCycler(Collection<Ability> data, MMOPlayer player, ItemStack identity) {
		super(data);
		this.player = new SoftReference<MMOPlayer>(player);
		this.identity = identity;
		register(this);
	}

	boolean isValid() {
		return player != null && player.get() != null;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();

		ret.put("current-index", currentIndex());
//		ret.put("identifying-meta", null)

		return ret;
	}


	public static void register(AbilityCycler cycler) {
		//TODO: Register globals.
	}

	static boolean isActive(String identify, ItemStack stack) {
		//FIXME: Add some sort of listener key system.
		return false;
	}

	public static List<AbilityCycler> getCyclers(String identity) {
		if (isRegistered(identity))
			return cyclers.get(identity);
		return Collections.emptyList();
	}

	public static AbilityCycler getCycler(String identity, ItemStack item) {
		List<AbilityCycler> cyclers = getCyclers(identity);
		if (cyclers.isEmpty()) return null;


	}

	static boolean isRegistered(String identity) {
		return cyclers != null && cyclers.containsKey(identity);
	}

	//Helpers...
	private static int getChange(int prev, int next) {
		if (next > prev)
			return 1;
		else if (next < prev)
			return -1;
		else
			return 0;
	}

	//INITIALIZE
	public static void init() {
		cyclers = new MapMaker().concurrencyLevel(2).makeMap();

		iHeld = new NoxListener<NoxMMO>(NoxMMO.getInstance()) {
			public void onItemHeldEvent(PlayerItemHeldEvent event) {
				final Player player = event.getPlayer();
				final String identity = player.getUniqueId().toString();

				final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

				if (mmoPlayer.getTempData().get(TEMP_PCTK, 0) <= 0 || !AbilityCycler.isRegistered(identity)) return; //Skip because we have no actual objects for this user.


				final ItemStack heldItem = player.getInventory().getItemInHand();

				AbilityCycler cycler;
				cycler = AbilityCycler.getCycler(heldItme);

				final int change = getChange(event.getPreviousSlot(), event.getNewSlot());


			}

		};
	}
}
