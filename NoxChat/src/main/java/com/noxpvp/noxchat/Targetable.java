package com.noxpvp.noxchat;

public interface Targetable {

	public Targetable getTarget();
	public void setTarget(Targetable targets);
	
	public void sendTargetMessage(String message);
	public void sendTargetMessage(String... messages);
	
	public void sendMessage(String message);
	public void sendMessage(String... messages);
}
