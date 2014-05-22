package com.noxpvp.noxchat;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.conversion.BasicConverter;

public class TargetConverter extends BasicConverter<Targetable> {

	public TargetConverter() {
		super(Targetable.class);
	}

	@Override
	protected Targetable convertSpecial(Object value, Class<?> valueType, Targetable def) {
		if (value instanceof Player)
			return PlayerManager.getInstance().getPlayer((Player) value);

		return def;
	}

	@Override
	public boolean isRegisterSupported() {
		return false;
	}
}
