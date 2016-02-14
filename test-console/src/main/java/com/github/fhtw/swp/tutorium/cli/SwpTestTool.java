package com.github.fhtw.swp.tutorium.cli;

import com.github.fhtw.swp.tutorium.ExerciseTestRunner;
import com.github.fhtw.swp.tutorium.MutableClassLoader;
import com.github.fhtw.swp.tutorium.common.SwpTestContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import static java.lang.ClassLoader.getSystemClassLoader;

public class SwpTestTool {

    private static final Arguments arguments = new Arguments();
    private static final VersionPrinter versionPrinter = new VersionPrinter();
    private static final CmdLineParser cmdLineParser = new CmdLineParser(arguments);
    private static final ExerciseTestRunner exerciseTestRunner = new ExerciseTestRunner();
    private static final MutableClassLoader mutableClassLoader = new MutableClassLoader(getSystemClassLoader());

    public static void main(String[] args) {

        versionPrinter.printCurrentVersion(System.out);

        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            printUsage(e);
            return;
        }

        SwpTestContext.setJarUrl(arguments.getJarUrl());
        mutableClassLoader.addUrl(arguments.getJarUrl());

        final int numberOfFailedTests = exerciseTestRunner.runExerciseTests(arguments.getExercise());

        System.exit(numberOfFailedTests);
    }

    private static void printUsage(CmdLineException e) {

        System.err.println(e.getMessage());
        System.err.println("Help: ");
        e.getParser().printUsage(System.err);
    }
}
