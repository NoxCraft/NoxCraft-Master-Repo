package com.noxpvp.core.utils;

import java.util.HashMap;
import java.util.Map;

public final class TimeUtils {
	private TimeUtils() {}
	
	public static class StopWatch {
		private final Map<String, Long> starts;
		private final Map<String, Long> elapses;
		private final Map<String, Long> stops;
		private boolean isNanos = false;
		
		public StopWatch(boolean nanos)
		{
			this();
			isNanos = nanos;
		}
		
		public StopWatch() {
			elapses = new HashMap<String, Long>();
			stops = new HashMap<String, Long>();
			starts = new HashMap<String, Long>();
		}
		
		private static long getStamp(boolean nanos)
		{
			return (nanos)? System.nanoTime(): System.currentTimeMillis();
		}
		
		public void start(String name)
		{
			long stamp = getStamp(isNanos());
			if (starts.containsKey(name))
				elapses.put(name, time(name));
			
			starts.put(name, stamp);
		}
		
		public long time(String name)
		{
			long mark = getStamp(isNanos());
			if (starts.containsKey(name) && !stops.containsKey(name))
				return mark - starts.get(name);
			else if (stops.containsKey(name) && elapses.containsKey(name))
				return elapses.get(name);
			else
				return 0;
		}
		
		public long stop(String name)
		{
			long mark = getStamp(isNanos());
			
			if (!starts.containsKey(name))
				return 0;
			
			long diff = mark - starts.get(name);
			stops.put(name, mark);
			starts.remove(name);
			elapses.put(name, diff);
			
			return diff;
		}
		
		public boolean isNanos() 
		{
			return isNanos;
		}
		
		private void convert(boolean nanos)
		{
			for (String key : starts.keySet())
				if (nanos)
					starts.put(key, starts.get(key) * 1000);
				else
					starts.put(key, starts.get(key) / 1000);
			
			for (String key : elapses.keySet())
				if (nanos)
					elapses.put(key, elapses.get(key) * 1000);
				else
					elapses.put(key, elapses.get(key) / 1000);
		}
		
		public boolean setUsingNanos(boolean useNanos) {
			boolean last = isNanos();
			isNanos = useNanos;

			if (last != isNanos())
				convert(isNanos());
			return last;
		}
	}
}
