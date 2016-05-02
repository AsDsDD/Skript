/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011-2016 Peter Güttinger and contributors
 * 
 */

package ch.njol.skript.timings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.njol.skript.lang.Trigger;
import ch.njol.skript.localization.Language;

/**
 * Creates timing reports.
 */
public class TimingReporter {
	
	public static String getReport() {
		Map<Object,Timing> timings = Timings.timings;
		Map<String,Long> triggers = new HashMap<String,Long>();
		Map<Object,Long> events = new HashMap<Object,Long>();
		
		for (Entry<Object,Timing> entry : timings.entrySet()) {
			Object key = entry.getKey();
			Timing val = entry.getValue();
			
			for (Entry<Trigger,Long> trigger : val.getTriggerTimes().entrySet()) {
				String name = trigger.getKey().getName();
				long tt = 0L;
				if (triggers.containsKey(name))
					tt = triggers.get(name);
				tt += trigger.getValue();
				triggers.put(name, tt);
			}
			
			long evtTime = 0L;
			if (events.containsKey(key))
				evtTime = events.get(key);
			evtTime += val.getEventTime();
		}
		
		long length = Timings.disableTime - Timings.enableTime;
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Language.get("timings.start"), length / 1000000000));
		
		sb.append(Language.get("timings.triggers"));
		for (Entry<String,Long> trigger : triggers.entrySet()) {
			float percent = trigger.getValue() / length * 100;
			sb.append(trigger.getKey() + ": " + (trigger.getValue() / 1000000) + "ms (" + percent + "%)");
		}
		
		sb.append(Language.get("timings.events"));
		for (Entry<Object,Long> event : events.entrySet()) {
			float percent = event.getValue() / length * 100;
			sb.append(event.getKey() + ": " + (event.getValue() / 1000000) + "ms (" + percent + "%)");
		}
		
		return sb.toString();
	}
}
