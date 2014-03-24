package com.noxpvp.core.json;

import java.util.Stack;

import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class TrackedJSONStringer extends JSONStringer {

	private enum ACTION {
		ARRAY, OBJECT;
	}

	private Stack<ACTION> actions = new Stack<TrackedJSONStringer.ACTION>();

	public TrackedJSONStringer() {
		super();
	}
	
	public boolean isCurrentlyArray() {
		if (!actions.isEmpty())
			return actions.peek() == ACTION.ARRAY;
		return false;
	}
	
	public boolean isCurrentlyObject() {
		if (!actions.isEmpty())
			return actions.peek() == ACTION.OBJECT;
		return false;
	}

	@Override
	public JSONWriter array() throws JSONException {
		JSONWriter ret = super.array();
		actions.add(ACTION.ARRAY);
		return ret;
	}

	@Override
	public JSONWriter object() throws JSONException {
		JSONWriter ret = super.object();
		actions.add(ACTION.OBJECT);
		return ret;
	}
	
	@Override
	public JSONWriter endArray() throws JSONException {
		JSONWriter ret = super.endArray();
		if (!actions.isEmpty())
			if (actions.peek() == ACTION.ARRAY)
				actions.pop();
		
		return ret;
	}
	
	@Override
	public JSONWriter endObject() throws JSONException {
		JSONWriter ret = super.endObject();
		if (!actions.isEmpty())
			if (actions.peek() == ACTION.OBJECT)
				actions.pop();
		
		return ret;
	}

	public JSONWriter close() throws JSONException {
		while (!actions.isEmpty())
		{
			ACTION a = actions.pop();
			if (a.equals(ACTION.ARRAY))
				endArray();
			else if (a.equals(ACTION.OBJECT))
				endObject();
		}

		return this;
	}
}