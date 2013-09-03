package net.noxcraft.noxcore.permissions;

import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.permissions.IPermissionDefault;

public class NoxcraftPermission implements IPermissionDefault {
	private final String name;
	private String node;
	private NoxcraftPermission[] children;
	private String[] parents;
	private PermissionDefault defaultPermission;
	private String description;
	
	public NoxcraftPermission(String node, String description, PermissionDefault defaults)
	{
		this(node, description, defaults, new NoxcraftPermission[0]);
	}
	
	public NoxcraftPermission(String node, String description, PermissionDefault defaults, NoxcraftPermission... children)
	{
		this.node = "noxcraft." + node;
		this.name = this.node;
		this.children = children;
		this.parents = new String[1];
		this.parents[0] = this.node.substring(0, this.node.lastIndexOf('.')) + "*";
	}
	
	public NoxcraftPermission[] getChildren()
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
