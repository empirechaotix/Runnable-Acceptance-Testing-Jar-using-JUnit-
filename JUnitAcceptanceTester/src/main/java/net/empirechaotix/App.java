package net.empirechaotix;

import net.empirechaotix.AcceptanceTest.AcceptanceTests;
import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        JUnitCore junit = new JUnitCore();

        junit.addListener( new XMLTestListener.JUnitResultFormatterAsRunListener(new XMLJUnitResultFormatter()) {
            @Override
            public void testStarted(Description description) throws Exception {

                File testReport = new File("/Users/iainwilliams/Projects/Java/JUnitIntegration/target","TEST.xml");
                System.out.println("Report: " + testReport.getAbsolutePath() );
                formatter.setOutput(new FileOutputStream(testReport));
                super.testStarted(description);
            }
        });

        junit.addListener(new TextListener(System.out));
        junit.run(
                AcceptanceTests.class
        );
    }
}
