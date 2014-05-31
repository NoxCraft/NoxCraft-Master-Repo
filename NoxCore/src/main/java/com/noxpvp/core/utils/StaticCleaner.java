
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

package com.noxpvp.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.reflection.FieldUtils;


/**
 * This is a modified version. Obtained through ProtocolLibraries code. <br>
 * Exact version used is in the link below <br>
 * A copy may be obtained through github @ <a href="https://github.com/aadnk/ProtocolLib/blob/c137640feea9063e22b788c8d8643400cf145ba6/ProtocolLib/src/main/java/com/comphenix/protocol/CleanupStaticMembers.java">https://github.com/aadnk/ProtocolLib/</a
 *  <br><br>
 * Used to fix ClassLoader leaks that may lead to filling up the permanent generation.
 * 
 * @author Kristian
 */
public class StaticCleaner {
	private ModuleLogger log; 
	private ClassLoader loader;
	private String[] internalClasses;
	private Class<?>[] publicClasses;
	
	public StaticCleaner(NoxPlugin plugin, ClassLoader loader, String[] internals, Class<?>[] classes) {
		log = plugin.getModuleLogger("Static Cleaner");
		this.loader = loader;
		this.internalClasses = internals;
		this.publicClasses = classes;
	}
	
	public void resetAll() {
		resetClasses(publicClasses);
		resetClasses(getClasses(loader, internalClasses));
	}
	
	private void resetClasses(Class<?>[] classes) {
		// Reset each class one by one
		for (Class<?> clazz : classes) {
			resetClass(clazz);
		}
	}
	
	private void resetClass(Class<?> clazz) {
		for (Field field : clazz.getFields()) {
			Class<?> type = field.getType();
			
			// Only check static non-primitive fields. We also skip strings.
			if (Modifier.isStatic(field.getModifiers()) && 
					!type.isPrimitive() && !type.equals(String.class)) {
				try {
					setFinalStatic(field, null);
				} catch (IllegalAccessException e) {
					StringBuilder sb = new StringBuilder("Illegal Access exception has occured... ").append(field.getName()).append(" had the error with the following message.").append(e.getMessage());
					log.warning(sb.toString());
					e.printStackTrace();
				}
			}
		}
	}

	// HACK! HAACK!
	private static void setFinalStatic(Field field, Object newValue) throws IllegalAccessException {
		int modifier = field.getModifiers();
		boolean isFinal = Modifier.isFinal(modifier);
		
		Field modifiersField = isFinal ? FieldUtils.getField(Field.class, "modifiers", true) : null;

		// We have to remove the final field first
		if (isFinal) {
			FieldUtils.writeField(modifiersField, field, modifier & ~Modifier.FINAL, true);
		}
			
		Class<?> type = field.getType();
		if (type.isInstance(AsyncTask.class))
		{
			AsyncTask t = (AsyncTask)field.get(null);  
			if (t != null && t.isRunning())
				t.stop(); //STOP TASK OR IT WILL RUN FOREVER!
		}
		
		// Now we can safely modify the field
		FieldUtils.writeStaticField(field, newValue, true);
		
		// Revert modifier
		if (isFinal) {
			FieldUtils.writeField(modifiersField, field, modifier, true);
		}
	}
	
	private Class<?>[] getClasses(ClassLoader loader, String[] names) {
		List<Class<?>> output = new ArrayList<Class<?>>();
		
		for (String name : names) {
			try {
				output.add(loader.loadClass(name));
			} catch (ClassNotFoundException e) {
				// Warn the user
				log.warning(new StringBuilder().append("ClassNotFoundException occured while looking for class \"").append(name).append("\"").append("Plugin may be out of date!").toString());
			}
		}
		
		return output.toArray(new Class<?>[0]);
	}
}
