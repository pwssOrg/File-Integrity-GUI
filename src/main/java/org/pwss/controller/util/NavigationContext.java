package org.pwss.controller.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple context for passing data between different parts of the application during navigation.
 */
public class NavigationContext {
    // A map to hold key-value pairs of data
    private final Map<String, Object> data = new HashMap<>();

    // Stores a value in the context with the specified key
    public void put(String key, Object value) {
        data.put(key, value);
    }

    // Retrieves a value from the context by key and casts it to the specified type
    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }
}
