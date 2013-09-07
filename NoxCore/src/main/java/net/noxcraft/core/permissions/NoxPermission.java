package net.noxcraft.core.permissions;

import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.permissions.IPermissionDefault;

public class NoxPermission implements IPermissionDefault {
	private final String name;
	private String node;
	private NoxPermission[] children;
	private String[] parents;
	private PermissionDefault defaultPermission;
	private String description;
	
	public NoxPermission(String node, String description, PermissionDefault defaults)
	{
		this(node, description, defaults, new NoxPermission[0]);
	}
	
	public NoxPermission(String node, String description, PermissionDefault defaults, NoxPermission... children)
	{
		this.node = "nox." + node;
		this.name = this.node;
		this.children = children;
		this.parents = new String[1];
		this.parents[0] = this.node.substring(0, this.node.lastIndexOf('.')) + "*";
	}
	
	public NoxPermission[] getChildren()
	{
		return children;
	}
	
	public String[] getParentNodes()
	{
		return parents;
	}
	
	public PermissionDefault getDefault() {
		return defaultPermission;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getName() {
		return name;
	}
}
