package com.noxpvp.mmo.util;

import java.util.Set;
import java.util.Map.Entry;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.utils.NoxMessageBuilder;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class NoxMMOMessageBuilder extends NoxMessageBuilder {

	public NoxMMOMessageBuilder(NoxPlugin plugin) {
		super(plugin);
	}
	
	public NoxMMOMessageBuilder(NoxPlugin plugin, boolean withHeader) {
		super(plugin, withHeader);
	}
	
	public NoxMMOMessageBuilder withClassInfo(PlayerClass clazz) {
		
		gold("Name: ").append(clazz.getDisplayName()).newLine();
		gold("Tiers: ");
		
		Set<Entry<Integer, IClassTier>> tiers = clazz.getTiers();
		for (Entry<Integer, IClassTier> tier : tiers) {
			append(tier.getValue().getDisplayName());
			
			if (tiers.size() > tier.getKey())
				append(", ");
		}
		
		return this;
	}

}
