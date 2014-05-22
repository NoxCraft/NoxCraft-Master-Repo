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
