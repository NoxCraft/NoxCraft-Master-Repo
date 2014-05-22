package com.noxpvp.mmo.classes.internal;

public enum ExperienceType {
	EXCAVATION, MINING, PVE, PVP, SMELTING, TAMING; //Add missing exp please. All caps.

	public static final ExperienceType[] COMBAT = new ExperienceType[]{PVE, PVP};
}
