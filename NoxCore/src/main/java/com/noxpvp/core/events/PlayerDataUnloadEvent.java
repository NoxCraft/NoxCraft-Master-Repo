package com.noxpvp.core.events;

import com.noxpvp.core.data.NoxPlayerAdapter;

public class PlayerDataUnloadEvent extends NoxPlayerDataEvent {

	public PlayerDataUnloadEvent(NoxPlayerAdapter player, boolean honorCore) {
		super(player, honorCore);
	}
}
