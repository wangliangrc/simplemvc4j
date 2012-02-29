package com.clark.ifk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class IFKFactory {

    private static Map<Executor, IFK> INSTANCES = new HashMap<Executor, IFK>();
    private static final IFK DEFAULT = new IFKJavaImpl(null);

    public static synchronized IFK getInstance(Executor executor) {
        if (executor == null) {
            return DEFAULT;
        }

        if (INSTANCES.containsKey(executor)) {
            return INSTANCES.get(executor);
        } else {
            IFK framework = new IFKJavaImpl(executor);
            INSTANCES.put(executor, framework);
            return framework;
        }
    }

    public static final IFK getInstance() {
        return getInstance(null);
    }
}
