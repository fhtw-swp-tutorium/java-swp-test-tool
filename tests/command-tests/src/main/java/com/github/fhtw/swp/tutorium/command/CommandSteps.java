package com.github.fhtw.swp.tutorium.command;

import com.github.fhtw.swp.tutorium.reflection.CountingInvocationHandler;
import com.github.fhtw.swp.tutorium.shared.TypeContext;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Und;
import org.hamcrest.reflection.ImplementationExistsMatcher;
import org.hamcrest.reflection.OnlyInterfaceParametersMethodMatcher;
import org.hamcrest.reflection.SubTypeFinder;
import org.junit.Assert;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CommandSteps {

    private final TypeContext typeContext;
    private final SubTypeFinder subTypeFinder;
    private final CommandDriver commandDriver;

    @Inject
    public CommandSteps(TypeContext typeContext, SubTypeFinder subTypeFinder, CommandDriver commandDriver) {
        this.typeContext = typeContext;
        this.subTypeFinder = subTypeFinder;
        this.commandDriver = commandDriver;
    }

    @Dann("^erwarte ich mir jeweils genau eine Methode$")
    public void erwarteIchMirJeweilsGenauEineMethode() throws Throwable {
        for (Class<?> type : typeContext.getTypes()) {
            final Set<Method> methodsOfType = typeContext.getMethodsOfType(type);
            Assert.assertThat(methodsOfType.size(), is(1));
        }
    }

    @Und("^jeder Parameter muss ein Interface sein$")
    public void mussJederParameterEinInterfaceSein() throws Throwable {
        for (Class<?> type : typeContext.getTypes()) {
            final Method invokeCommandMethod = typeContext.getFirstMethodOfType(type);
            Assert.assertThat(invokeCommandMethod, new OnlyInterfaceParametersMethodMatcher());
        }
    }

    @Dann("^muss es für jeden Interface Parameter eine Implementierung geben$")
    public void mussEsFuerJedenInterfaceParameterEineImplementierungGeben() throws Throwable {
        for (Class<?> invokerType : typeContext.getTypes()) {
            final Class<?> commandType = commandDriver.getCommandType(invokerType);
            Assert.assertThat(commandType, new ImplementationExistsMatcher(subTypeFinder));
        }
    }

    @Und("^ich eine Instanz des Invokers erzeuge$")
    public void ichEineInstanzDesInvokersErzeuge() throws Throwable {
        typeContext.getTypes().forEach(commandDriver::createInvokerProxyInstance);
    }

    @Und("^ich eine dynamische Instanz des Kommandos erzeuge$")
    public void ichEineDynamischeInstanzDesKommandosErzeuge() throws Throwable {
        typeContext.getTypes().forEach(commandDriver::createCommandProxyInstance);
    }

    @Und("^dieses Kommando an den Invoker übergebe$")
    public void diesesKommandoAnDenInvokerUebergebe() throws Throwable {
        for (Class<?> invokerType : typeContext.getTypes()) {
            final InvokerProxy invokerProxy = commandDriver.getInvokerProxyInstance(invokerType);
            final CommandProxy commandProxy = commandDriver.getCommandProxyInstance(invokerType);

            invokerProxy.execute(commandProxy);
        }
    }

    @Dann("^soll mindestens eine Methode des Kommandos aufgerufen werden$")
    public void sollMindestensEineMethodeDesKommandosAufgerufenWerden() throws Throwable {
        for (Class<?> invokerType : typeContext.getTypes()) {
            final CommandProxy commandProxy = commandDriver.getCommandProxyInstance(invokerType);
            final CountingInvocationHandler invocationHandler = commandProxy.getInvocationHandler();

            Assert.assertThat(invocationHandler.getInvocationCount(), is(not(0)));
        }
    }
}
