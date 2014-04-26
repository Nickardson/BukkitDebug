package com.nickardson.bukkitdebug.script;

import org.mozilla.javascript.NativeJavaClass;

public class APIReflection {
    public Class getClass(Object o) {
        if (o instanceof NativeJavaClass) {
            return (Class) ((NativeJavaClass) o).unwrap();
        } else if (o instanceof Class) {
            return ((Class) o);
        } else {
            return o.getClass();
        }
    }
}
