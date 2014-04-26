package com.nickardson.bukkitdebug.script;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;

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

        Object conv = Context.jsToJava(o, Object.class);
        if (conv instanceof NativeJavaObject) {
            conv = ((NativeJavaObject) conv).unwrap();
        }

        JavaScriptEngine engine = BukkitDebug.getPlugin(BukkitDebug.class).engine;
        engine.enter();
        try {
            return Context.toString(conv);
        } finally {
            engine.exit();
        }
    }

    public static interface Instance {
        public String stringify(Object o);
    }
}
