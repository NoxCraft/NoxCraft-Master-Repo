package com.noxpvp.noxchat.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.noxpvp.noxchat.Targetable;

public abstract class BaseChannel implements Targetable {
	
	private Targetable target = null;
	
	private List<Targetable> listeners = Collections.synchronizedList(new ArrayList<Targetable>());
	
	public final void addListener(Targetable target) {
		synchronized (listeners) {
			listeners.add(target);
		}
	}
	
	public final void addListeners(Collection<Targetable> listeners) {
		synchronized (this.listeners) {
			this.listeners.addAll(listeners);
		}
	}
	
	public final void clearListeners() {
		synchronized (listeners) {
			listeners.clear();
		}
	}
	
	public final Targetable[] getListeners() {
		synchronized (listeners) {
			return listeners.toArray(new Targetable[listeners.size()]);
		}
	}
	
	public boolean canHear(Targetable target) {
		return canHear(this, target);
	}
	
	public boolean canHear(Targetable from, Targetable target) {
		if (from instanceof BaseChannel) {
			BaseChannel channel = (BaseChannel) from;
			return channel.isListener(target) && !channel.isMuted(target);
		} else {
			return from.isMuted(target);
		}
	}
	
	public Targetable getTarget() {
		return target;
	}
	
	public String getType() {
		return "Channel-" + getChannelType();
	}
	
	public final boolean isListener(Targetable target) {
		synchronized (listeners) {
			return listeners.contains(target);
		}
	}
	
	public final boolean removeListener(Targetable target) {
		synchronized (listeners) {
			return listeners.remove(target);
		}
	}
	
	public final boolean removeListeners(Collection<Targetable> listeners) {
		synchronized (this.listeners) {
			return this.listeners.removeAll(listeners);
		}
	}

	public void sendMessage(String message) {
		for (Targetable listener : getListeners())
			listener.sendMessage(message);
	}

	public void sendMessage(String... messages) {
		for (Targetable listener : getListeners())
			listener.sendMessage(messages);
	}
	
	public void sendMessage(Targetable from, String message) {
		if (!isMuted(from))
			for (Targetable listener : getListeners())
				listener.sendMessage(message);
	}

	public void sendMessage(Targetable from, String... messages) {
		if (!isMuted(from))
			for (Targetable listener : getListeners())
				listener.sendMessage(messages);
	}

	public void sendTargetMessage(String message) {
		if (getTarget() != null)
			getTarget().sendMessage(message);
	}

	public void sendTargetMessage(String... messages) {
		if (getTarget() != null)
			getTarget().sendMessage(messages);
	}
	
	public void sendTargetMessage(Targetable from, String message) {
		if (getTarget() != null)
			sendTargetMessage(message);
	}
	
	public void sendTargetMessage(Targetable from, String... messages) {
		if (getTarget() != null)
			sendTargetMessage(messages);
	}

	public final void setListeners(Collection<Targetable> listeners) {
		clearListeners();
		addListeners(listeners);
	}

	public void setTarget(Targetable target) {
		this.target = target;
	}

	protected abstract String getChannelType();

}
