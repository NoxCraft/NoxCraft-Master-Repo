package com.noxpvp.mmo;

import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.gui.rendering.IRenderer;
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
	private static final IRenderer dummyRender = new IRenderer() {
		@Override
		public void render() {
			//NOTHING HAHA
		}
	};
//	public static final String TEMP_PCTK = "ability-cycler.active-count";

	static ConcurrentMap<String, List<AbilityCycler>> cyclers = null;
	private static NoxListener<NoxMMO> iHeld = null;
	private final ItemStack cycleItem;
	private Reference<MMOPlayer> player = null;
	private IRenderer renderer = null; //TODO: Create renderers

	public AbilityCycler(int size, OfflinePlayer player, ItemStack cycleItem) {
		this(size, MMOPlayerManager.getInstance().getPlayer(player), cycleItem);
	}

	public AbilityCycler(Collection<Ability> data, OfflinePlayer player, ItemStack cycleItem) {
		this(data, MMOPlayerManager.getInstance().getPlayer(player), cycleItem);
	}

	public AbilityCycler(int size, MMOPlayer player, ItemStack cycleItem) {
		super(size);
		this.player = new SoftReference<MMOPlayer>(player);
		this.cycleItem = cycleItem;

		register(this);
	}

	public AbilityCycler(Collection<Ability> data, MMOPlayer player, ItemStack cycleItem) {
		super(data);
		this.player = new SoftReference<MMOPlayer>(player);
		this.cycleItem = cycleItem;
		register(this);
	}

	public static void register(AbilityCycler cycler) {
		//TODO: Register globals.
	}

	static boolean isActive(String identify, ItemStack stack) {
		//FIXME: Retrieve cycler objects system.
		return false;
	}

	public static List<AbilityCycler> getCyclers(String identity) {
		if (isRegistered(identity))
			return cyclers.get(identity);
		return Collections.emptyList();
	}

	public IRenderer getRenderer() {
		if (this.renderer != null)
			return this.renderer;
		return dummyRender;
	}

	public void renderDisplay() {
		getRenderer().render();
	}

	public static AbilityCycler getCycler(String identity, final ItemStack item) {
		List<AbilityCycler> cyclers = getCyclers(identity);
		if (cyclers.isEmpty()) return null;

		return null;//FIXME: FINISH Cycler Retrieval.
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
				if (!player.isSneaking())
					return;

				final MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

				if (/*mmoPlayer.getTempData().get(TEMP_PCTK, 0) <= 0 || */!AbilityCycler.isRegistered(identity)) return; //Skip because we have no actual objects for this user.

				final ItemStack heldItem = player.getInventory().getItemInHand();

				AbilityCycler cycler = AbilityCycler.getCycler(identity, heldItem);
				if (cycler == null) return;

//				final int change = getChange(event.getPreviousSlot(), event.getNewSlot());
				switch (/*change */getChange(event.getPreviousSlot(), event.getNewSlot())) {
					case 1:
						cycler.next();
						return;
					case -1:
						cycler.previous();
						return;
					default:
						return;
				}
			}

		};
	}

	boolean isValid() {
		return player != null && player.get() != null;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();

		//Ability Storage.
		List<String> abilities = new LinkedList<String>();
		for (Ability ability : getList())
			abilities.add(ability.getName());

		//Store current state.
		ret.put("current-index", currentIndex());

		ret.put("player", getMMOPlayer().getUUID(true));

		//Store item that tells if its the cycler item.
		ret.put("cycle-item", getCycleItem());

		//Actually store the abilities.
		ret.put("abilities", abilities);

		return ret;
	}

	public static AbilityCycler valueOf(Map<String, Object> data) {
		if (!data.containsKey("player"))
			return null;
		//FIXME: FINISH ME!
		return null;
	}

	@Override
	public Ability previous() {
		Ability ret = super.previous();
		renderDisplay();
		return ret;
	}

	@Override
	public Ability next() {
		Ability ret = super.next();
		renderDisplay();
		return ret;
	}

	public MMOPlayer getMMOPlayer() {
		if (player != null) return player.get();
		return null;
	}

	public ItemStack getCycleItem() {
		return cycleItem;
	}
}
