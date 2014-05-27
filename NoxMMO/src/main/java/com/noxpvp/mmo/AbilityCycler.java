package com.noxpvp.mmo;

import com.noxpvp.core.data.Cycler;
import com.noxpvp.mmo.abilities.Ability;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AbilityCycler extends Cycler<Ability> implements ConfigurationSerializable {

	private Reference<MMOPlayer> player = null;

	public AbilityCycler(int size, OfflinePlayer player) {
		this(size, MMOPlayerManager.getInstance().getPlayer(player));
	}

	public AbilityCycler(Collection<Ability> data, OfflinePlayer player) {
		this(data, MMOPlayerManager.getInstance().getPlayer(player));
	}

	public AbilityCycler(int size, MMOPlayer player) {
		super(size);
		this.player = new SoftReference<MMOPlayer>(player);
	}

	public AbilityCycler(Collection<Ability> data, MMOPlayer player) {
		super(data);
		this.player = new SoftReference<MMOPlayer>(player);
	}



	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> ret = new HashMap<String, Object>();

		ret.put("current-index", currentIndex());
//		ret.put("identifying-meta", null)

		return ret;
	}
}
