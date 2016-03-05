package com.topolyai.avest.android;

import java.util.HashSet;
import java.util.Set;

public class AVestCofigure {

    private Set<String> packages = new HashSet<>();

    private static AVestCofigure inst;


    public static void registerPackage(String p) {
        get().packages.add(p);
    }

    static AVestCofigure get() {
        if (inst == null) {
            inst = new AVestCofigure();
        }
        return inst;
    }

    boolean registeredPacakge(String p) {
        for (String pack : packages) {
            if (p.startsWith(pack)) {
                return true;
            }
        }
        return false;
    }

}
