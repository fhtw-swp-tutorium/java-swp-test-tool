package com.github.fhtw.swp.tutorium.e2eTest;

import com.github.fhtw.swp.tutorium.Pattern;
import com.github.fhtw.swp.tutorium.cli.Application;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import static com.github.fhtw.swp.tutorium.e2eTest.JUnitResultMatcher.*;
import static org.hamcrest.Matchers.is;

public class CompositesE2ETest {

    private SwpTestToolProxy swpTestTool;

    @Before
    public void setUp() throws Exception {
        swpTestTool = SwpTestToolProxy.createInstance();
    }

    @Test
    public void testAccurateComposites() throws Exception {

        final URL patternImplementation = CompositesE2ETest.class.getResource("/accurate-composites-0.0.1-SNAPSHOT.jar");

        swpTestTool.run(Pattern.COMPOSITE, patternImplementation);

        final File junitResultFile = Paths.get(".", Application.JUNIT_RESULTS_FOLDER, "MyCompositeFigure.xml").toFile();

        final JUnitResult junitResult = JUnitResult.fromFile(junitResultFile);

        Assert.assertThat(junitResult, numberOfTests(is(8)));
        Assert.assertThat(junitResult, numberOfSkippedTests(is(0)));
        Assert.assertThat(junitResult, numberOfFailedTests(is(0)));
    }
}
