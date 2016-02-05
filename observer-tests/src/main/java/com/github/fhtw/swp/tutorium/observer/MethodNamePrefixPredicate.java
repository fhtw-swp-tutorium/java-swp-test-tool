package com.github.fhtw.swp.tutorium.observer;

import com.google.common.base.Predicate;

import java.lang.reflect.Method;

class MethodNamePrefixPredicate implements Predicate<Method> {

    private final String name;

    public static Predicate<Method> startsWith(String prefix) {
        return new MethodNamePrefixPredicate(prefix);
    }

    public MethodNamePrefixPredicate(String name) {
        this.name = name;
    }

    @Override
    public boolean apply(Method method) {
        return method.getName().toLowerCase().startsWith(name.toLowerCase());
    }
}
