package com.github.fhtw.swp.tutorium.cli;

import com.github.fhtw.swp.tutorium.SwpTestTool;
import com.github.fhtw.swp.tutorium.guice.CucumberInjectorSource;
import com.github.fhtw.swp.tutorium.guice.SwpTestToolModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.guice.CucumberModules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Application {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {

        final Arguments arguments = new Arguments();
        final CmdLineParser cmdLineParser = new CmdLineParser(arguments);

        LOGGER.debug("Arguments: {}", args);

        new VersionPrinter().printVersionInformation(System.out);

        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            LOGGER.error("Failed to parse arguments", e);
            printUsage(e);
            return;
        }

        final Injector injector = Guice.createInjector(
                new SwpTestToolModule(arguments.getExercise(), arguments.getJarUrl()),
                CucumberModules.SCENARIO
        );

        CucumberInjectorSource.instance = injector;

        injector.getInstance(SwpTestTool.class).run();
    }

    private static void printUsage(CmdLineException e) {

        System.err.println(e.getMessage());
        System.err.println("Help: ");
        e.getParser().printUsage(System.err);
    }
}
