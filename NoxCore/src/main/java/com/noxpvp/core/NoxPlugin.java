package com.noxpvp.core;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.PluginBase;
import com.noxpvp.core.permissions.NoxPermission;

public abstract class NoxPlugin extends PluginBase {

	protected void addPermissions(NoxPermission... perms)
	{
		NoxCore.getInstance().addPermissions(perms);
	}
	
	protected void addPermission(NoxPermission perm) {
		NoxCore.getInstance().addPermission(perm);
	}
	
	@Override
	public int getMinimumLibVersion() {
		return Common.VERSION;
	}
	
}
