package com.nickardson.bukkitdebug.script;

import java.util.HashMap;
import java.util.Map;

/**
 * Turns objects into strings the client can observe.
 */
public class Stringifier {

    private Map<Class, Instance> map;

    public Stringifier() {
        map = new HashMap<Class, Instance>();
    }

    public Instance put(Class key, Instance value) {
        return map.put(key, value);
    }

    public String stringify(Object o) {
        Instance i = map.get(o.getClass());
        if (i != null) {
            return i.stringify(o);
        }
        return o.toString();
    }

    public static interface Instance {
        public String stringify(Object o);
    }
}
