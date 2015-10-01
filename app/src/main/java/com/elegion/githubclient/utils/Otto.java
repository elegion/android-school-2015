package com.elegion.githubclient.utils;

import com.squareup.otto.Bus;

/**
 * @author Grigoriy Dzhanelidze
 */
public class Otto {
    private static final Bus BUS = new Bus();

    private Otto() {
    }

    public static void register(Object object) {
        BUS.register(object);
    }

    public static void unregister(Object object) {
        BUS.unregister(object);
    }

    public static void post(Object event) {
        BUS.post(event);
    }
}
