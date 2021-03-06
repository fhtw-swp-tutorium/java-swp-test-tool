package com.github.fhtw.swp.tutorium.observer;

import com.github.fhtw.swp.tutorium.reflection.GenericInvocationCountingProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubjectProxy {

    private final Object subjectInstance;
    private final Method registerMethod;
    private final Method unregisterMethod;
    private final Method updateMethod;

    public SubjectProxy(Object subjectInstance, Method registerMethod, Method unregisterMethod, Method updateMethod) {
        this.subjectInstance = subjectInstance;
        this.registerMethod = registerMethod;
        this.unregisterMethod = unregisterMethod;
        this.updateMethod = updateMethod;
    }

    public void register(GenericInvocationCountingProxy genericInvocationCountingProxy) {
        invoke(registerMethod, subjectInstance, genericInvocationCountingProxy.getInstance());
    }

    public void unregister(GenericInvocationCountingProxy genericInvocationCountingProxy) {
        invoke(unregisterMethod, subjectInstance, genericInvocationCountingProxy.getInstance());
    }

    public void update() {
        invoke(updateMethod, subjectInstance);
    }

    private void invoke(Method method, Object instance, Object... arguments) {
        try {
            method.invoke(instance, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}
