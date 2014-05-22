package com.noxpvp.noxchat;

import java.util.logging.Level;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.utils.StaticCleaner;
import com.palmergames.bukkit.towny.Towny;

public class NoxChat extends NoxPlugin {
	private static NoxChat instance;
	Towny towny;
	private PermissionHandler permHandler;

	public static NoxChat getInstance() {
		return instance;
	}

	private static void setInstance(NoxChat instance) {
		NoxChat.instance = instance;
	}

	@Override
	public NoxCore getCore() {
		return NoxCore.getInstance();
	}

	@Override
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return new Class[0];
	}

	@Override
	public void disable() {
		String[] internalClasses = new String[]{};
		Class<?>[] publicClasses = new Class[]{
				PlayerManager.class
		};

		new StaticCleaner(this, getClassLoader(), internalClasses, publicClasses).resetAll();
	}

	@Override
	public void enable() {
		if (instance != null) {
			log(Level.SEVERE, "This plugin already has an instance running!! Disabling second run.");
			setEnabled(false);
			return;
		}
		setInstance(this);

		Conversion.register(new TargetConverter());

		permHandler = new PermissionHandler(this);

		towny = getCore().getTowny();
	}
}
