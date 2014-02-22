package com.noxpvp.mmo.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ClassType {
	Melee,
		Axes(Melee),
		Swords(Melee),
		Stealth(Melee),
	Ranged,
		Magic(Ranged),
			Healing(Magic),
		Archery(Ranged),
	Gathering,
		Farming(Gathering),
	Pets(null, Melee, Ranged),
	Scientific;
	
	private ClassType parent;
	private ClassType[] inheritance;
	
	private List<ClassType> children;

	private ClassType() {
		this.children = new ArrayList<ClassType>();
		this.parent = null;
		this.inheritance = new ClassType[0];
	}
	
	private ClassType(ClassType parent) {
		this.children = new ArrayList<ClassType>();
		parent.children.add(this);
		this.parent = parent;
		this.inheritance = new ClassType[0];
	}
	
	private ClassType(ClassType parent, ClassType... inherits)
	{
		this.children = new ArrayList<ClassType>();
		parent.children.add(this);
		this.parent = parent;
		this.inheritance = inherits;
	}
	
	public boolean inherits(ClassType type) {
		if (getParent() != null && getParent().equals(type))
			return true;
			
		for (ClassType t : inheritance)
			if (t.equals(type))
				return true;
		
		//Nothing matches return false
		return false;
	}
	
	public List<ClassType> getChildren() {
		return Collections.unmodifiableList(children); //Should never be edited!
	}
	
	public ClassType getRoot() {
		if (parent == null)
			return this;
		else
			return parent.getRoot();
	}
	
	public ClassType getParent() {
		return parent;
	}
}