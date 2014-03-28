package com.noxpvp.core.events;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class PlayerDataUnloadEvent extends NoxPlayerDataEvent {

	@Deprecated
	public PlayerDataUnloadEvent(NoxPlayerAdapter player, boolean honorCore) {
		super(player, honorCore);
	}
}
