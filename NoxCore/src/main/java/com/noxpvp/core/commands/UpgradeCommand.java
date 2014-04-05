package com.noxpvp.core.commands;

//import java.util.HashMap;
//import java.util.Map;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
//import com.noxpvp.core.internal.LockerCaller;

public class UpgradeCommand extends BaseCommand {
	public static final String COMMAND_NAME = "upgrade";
	public static final String PERM_NODE = "upgrade";
	
//	private final Map<CommandSender, LockerCaller> lockers = new HashMap<CommandSender, LockerCaller>();
	
	public UpgradeCommand() {
		super(COMMAND_NAME, false);
	}
	
	public String[] getFlags() {
		return new String[0];
	}

	public String[] getHelp() {
		return new MessageBuilder().blue("Currently only upgrade player data to use UID instead of playernames.").lines();
	}

	public int getMaxArguments() {
		return 0;
	}

	@Override
	public CommandResult execute(CommandContext context) throws NoPermissionException {
		String perm = StringUtil.join(".", "nox", "core", PERM_NODE);
		if (!handler.hasPermission(context.getSender(), perm))
			throw new NoPermissionException(context.getSender(), perm, "You are not allowed to upgrade the nox plugins.");
		return new CommandResult(this, true, new MessageBuilder().red("Not implemented").lines());
//		upgradeToUID(context.getSender());
	}

	@Override
	public NoxPlugin getPlugin() {
		return NoxCore.getInstance();
	}

	private void upgradeToUID(final CommandSender sender) {
//		final LockerCaller caller = new LockerCaller() {
//			
//			public void complain(SafeLocker lock, LockerCaller complainer) {
//				MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "PlayerManager has a file lock active./n The locking object is :=" + lock.getCaller().getClass().getName() + "/n The Complainer is:=" + ((complainer != null)? ((complainer == this)?"YOU": complainer.getClass().getName()): "null"));
//			}
//			
//			public void complain(SafeLocker lock) {
//				complain(lock, null);
//			}
//		};
//		
//		AsyncTask task = new AsyncTask() {
//			boolean good = false;
//			final Task syncedTask = new Task(NoxCore.getInstance()) {
//				
//				public void run() {
//					if (good)
//						MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_SUCCESS, "Upgraded to new UID config system.");
//					else
//						MessageUtil.sendLocale(sender, GlobalLocale.COMMAND_FAILED, "Failed to upgrade user data.");
//				}
//			};
//			public void run() {
//				final PlayerManager pm = PlayerManager.getInstance();
//				if (!pm.tryLock(caller)) {
//					new Task(NoxCore.getInstance()) {
//						public void run() {
//							caller.complain(pm, caller);
//						}
//					};
//					good = false;
//				} else {
//					List<File> files = pm.getAllDeprecatedPlayerFiles();
//					for (File file : files) {
//						String name = file.getName().replace(".yml", "");
//						pm.getPlayer(name);
//					}
//					good = true;
//				}
//				
//				syncedTask.start();
//			}
//		};
//				
	}
}
