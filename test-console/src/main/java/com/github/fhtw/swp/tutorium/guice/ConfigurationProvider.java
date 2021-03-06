package com.github.fhtw.swp.tutorium.guice;

import com.github.fhtw.swp.tutorium.MutableClassLoader;
import com.google.inject.Provider;
import org.reflections.Configuration;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Inject;
import java.net.URL;

public class ConfigurationProvider implements Provider<Configuration> {

    private final URL currentSut;
    private final MutableClassLoader mutableClassLoader;

    @Inject
    public ConfigurationProvider(@CurrentSut URL currentSut, MutableClassLoader mutableClassLoader) {
        this.currentSut = currentSut;
        this.mutableClassLoader = mutableClassLoader;
    }

    @Override
    public Configuration get() {

        makeAccessibleSut();

        final Configuration configuration = new ConfigurationBuilder()
                .setUrls(currentSut)
                .setScanners(
                        new MethodAnnotationsScanner(),
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner()
                );

        return configuration;
    }

    private void makeAccessibleSut() {
        mutableClassLoader.addUrl(currentSut);
    }
}
