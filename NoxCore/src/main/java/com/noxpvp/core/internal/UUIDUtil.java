package com.noxpvp.core.internal;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.google.common.collect.ImmutableList;

public class UUIDUtil {
	private static final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	private ConcurrentHashMap<String, UUID> name2UUID = new ConcurrentHashMap<String, UUID>();
	
	public UUID tryGetID(String name) {
		if (name2UUID.contains(name))
			return name2UUID.get(name);
		else {
			if (Bukkit.isPrimaryThread()) {
				fetchByOnlineNames(toList(name)); //Last effort.
				if (name2UUID.contains(name))
					return name2UUID.get(name);
			}
			
			ensurePlayerUUIDByName(name);
			return null;
		}
	}
	
	/**
	 * <b>WARNING NOT THREAD SAFE THIS CALL IS BLOCKING!</b>
	 * @param name of player
	 * @return UUID of player
	 */
	public UUID getID(String name) {
		if (name2UUID.contains(name))
			return name2UUID.get(name);
		fetchByNames(toList(name));
		return name2UUID.get(name);
	}
	
	private void fetchByNames(List<String> names) {
		Map<String, UUID> call = new HashMap<String, UUID>();
		if (Bukkit.isPrimaryThread())
			call.putAll(fetchByOnlineNames(names));
			
		try {
			call.putAll(new UUIDFetcher(names).call());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		name2UUID.putAll(call);
	}
	
	private Map<String, UUID> fetchByOnlineNames(List<String> names) {
		Map<String, UUID> call = new HashMap<String, UUID>();
		for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
			String name = iterator.next();
			Player p = Bukkit.getPlayerExact(name); //Check if player is actually online. Save up on that heavy lock.
			if (p != null && p.getUniqueId() != null) {
				call.put(name, p.getUniqueId());
				iterator.remove();
			} 
		}
		return call;
	}

	private void ensurePlayerUUIDByName(String name) {
		ensurePlayerUUIDsByName(toList(name));
	}
	
	private <T> List<T> toList(T name) {
		List<T> names = new ArrayList<T>();
		names.add(name);
		return names;
	}

	public void ensurePlayerUUIDsByName(final List<String> names) {
		for (String name : names)
			name2UUID.put(name, ZERO_UUID);
		
		fetchByOnlineNames(names);
		new AsyncTask() {
			public void run() {
				fetchByNames(names);
			}
		}.start();
	}
	
	/**
	 * 
	 * @author EvilMidget
	 *
	 */
	static class UUIDFetcher implements Callable<Map<String, UUID>> {
	    private static final int MAX_SEARCH = 100;
	    private static final String PROFILE_URL = "https://api.mojang.com/profiles/page/";
	    private static final String AGENT = "minecraft";
	    private final JSONParser jsonParser = new JSONParser();
	    private final List<String> names;
	    public UUIDFetcher(List<String> names) {
	        this.names = ImmutableList.copyOf(names);
	    }
	
	    public Map<String, UUID> call() throws Exception {
	        Map<String, UUID> uuidMap = new HashMap<String, UUID>();
	        String body = buildBody(names);
	        for (int i = 1; i < MAX_SEARCH; i++) {
	            HttpURLConnection connection = createConnection(i);
	            writeBody(connection, body);
	            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
	            JSONArray array = (JSONArray) jsonObject.get("profiles");
	            Number count = (Number) jsonObject.get("size");
	            if (count.intValue() == 0) {
	                break;
	            }
	            for (Object profile : array) {
	                JSONObject jsonProfile = (JSONObject) profile;
	                String id = (String) jsonProfile.get("id");
	                String name = (String) jsonProfile.get("name");
	                UUID uuid = UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
	                uuidMap.put(name, uuid);
	            }
	        }
	        return uuidMap;
	    }
	
	    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
	        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
	        writer.write(body.getBytes());
	        writer.flush();
	        writer.close();
	    }
	
	    private static HttpURLConnection createConnection(int page) throws Exception {
	        URL url = new URL(PROFILE_URL+page);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setUseCaches(false);
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        return connection;
	    }
	    @SuppressWarnings("unchecked")
	    private static String buildBody(List<String> names) {
	        List<JSONObject> lookups = new ArrayList<JSONObject>();
	        for (String name : names) {
	            JSONObject obj = new JSONObject();
	            obj.put("name", name);
	            obj.put("agent", AGENT);
	            lookups.add(obj);
	        }
	        return JSONValue.toJSONString(lookups);
	    }
	}
}