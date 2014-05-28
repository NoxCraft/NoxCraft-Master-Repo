package com.noxpvp.mmo;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.google.common.collect.MapMaker;
import com.noxpvp.core.data.Cycler;
import com.noxpvp.core.gui.rendering.IRenderer;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.core.utils.UUIDUtil;
import com.noxpvp.mmo.abilities.Ability;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
		Validate.notNull(cycler.getMMOPlayer());

		MMOPlayer p = cycler.getMMOPlayer();

		final String identity = UUIDUtil.compressUUID(p.getUUID());
		if (cyclers.containsKey(identity))
			cyclers.get(identity).add(cycler);
		else {
			List<AbilityCycler> abilities = new ArrayList<AbilityCycler>();
			abilities.add(cycler);
			cyclers.put(identity, abilities);
		}
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

	public boolean isCyclerMatch(ItemStack stack) {
		return getCycleItem().isSimilar(stack);
	}

	public boolean isPlayerMatch(Object ob) {
		if (!(ob instanceof OfflinePlayer || UUIDUtil.isUUID(ob) || ob instanceof MMOPlayer))
			return false;

		if (UUIDUtil.isUUID(ob) && getMMOPlayer() != null)
			return getMMOPlayer().getUUID().equals(UUIDUtil.toUUID(ob));

		if (getMMOPlayer() != null && ob instanceof  MMOPlayer)
			return getMMOPlayer().equals(ob);

		return false;
	}

	public static AbilityCycler getCycler(String identity, final ItemStack item) {
		List<AbilityCycler> cyclers = getCyclers(identity);
		if (cyclers.isEmpty()) return null;

		for (AbilityCycler cycler : cyclers)
			if (cycler.isCyclerMatch(item))
				return cycler;
		return null;
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
			@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
			public void onItemHeldEvent(PlayerItemHeldEvent event) {
				final Player player = event.getPlayer();
				final String identity = UUIDUtil.compressUUID(player.getUniqueId());
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
						event.setCancelled(true);
						return;
					case -1:
						cycler.previous();
						event.setCancelled(true);
						return;
					default:
						return;
				}
			}

		};

		iHeld.register();
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

	private static boolean isDataValid(Map<String, Object> data) {
		return data.containsKey("player") && data.containsKey("cycle-item") && data.containsKey("abilities") && UUIDUtil.isUUID(data.get("player"));
	}

	public static AbilityCycler valueOf(Map<String, Object> data) {
		if (!isDataValid(data))
			return null;

		UUID id = UUIDUtil.toUUID(data.get("player"));
		OfflinePlayer player = Bukkit.getOfflinePlayer(id);

		ItemStack cycleItem = null;
		if (data.get("cycle-item") instanceof ItemStack)
			cycleItem = (ItemStack) data.get("cycle-item");

		List<String> abilityNames = null;
		if (data.get("abilities") instanceof List)
			abilityNames = (List<String>) data.get("abilities");

		if (id == null || cycleItem == null || LogicUtil.nullOrEmpty(abilityNames))
			return null;

		MMOPlayer mmoPlayer = MMOPlayerManager.getInstance().getPlayer(player);

		List<Ability> abilities = new ArrayList<Ability>();

		for (Ability a : mmoPlayer.getAllAbilities())
			if (abilityNames.contains(a.getName()))
				abilities.add(a);

		if (LogicUtil.nullOrEmpty(abilities))
			return null;

		return new AbilityCycler(abilities, mmoPlayer, cycleItem);
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
