/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.noxchat;

public interface Targetable {

	public String getName();
	public String getType();
	
	public Targetable getTarget();
	public void setTarget(Targetable targets);
	
	public boolean isMuted(Targetable target);

	public void sendTargetMessage(String message);
	public void sendTargetMessage(String... messages);
	
	public void sendMessage(String message);
	public void sendMessage(String... messages);

	public void sendTargetMessage(Targetable from, String message);
	public void sendTargetMessage(Targetable from, String... messages);
	
	public void sendMessage(Targetable from, String message);
	public void sendMessage(Targetable from, String... messages);
}
