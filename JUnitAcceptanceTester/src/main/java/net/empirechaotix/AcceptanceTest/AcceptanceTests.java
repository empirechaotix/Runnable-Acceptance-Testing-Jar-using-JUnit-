package net.empirechaotix.AcceptanceTest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

/**
 * Created by iainwilliams on 09/05/2017.
 */
public class AcceptanceTests {

    @Before
    public void before() {
        cleanFiles();
    }

    public void cleanFiles() {
        String outputFileDir = getFolderPathFromProperty("acceptancetest.output.folder");
        String inputFileDir = getFolderPathFromProperty("acceptancetest.input.folder");

        File inputFile = new File(inputFileDir + "inputFile.txt");
        if(inputFile.exists()) {
            inputFile.delete();
        }
        if(inputFile.exists()) {
            throw new IllegalStateException("Input file still exists");
        }

        File outputFile = new File(outputFileDir + "outputFile.txt");
        if(outputFile.exists()) {
            outputFile.delete();
        }
        if(outputFile.exists()) {
            throw new IllegalStateException("Output file still exists");
        }
    }

    @After
    public void after() {

        cleanFiles();
    }

    //submission folder
    //response folder

    private String getFolderPathFromProperty(String propertyName) {
        String property = System.getProperty(propertyName);

        if(property != null && !property.equals("")) {
            return property;
        }

        throw new IllegalStateException(String.format("Missing the environment variable [%s]", propertyName));
    }

    @Test
    public void runHappyPathTest() throws IOException {

        //setup
        String outputFileDir = getFolderPathFromProperty("acceptancetest.output.folder");
        String inputFileDir = getFolderPathFromProperty("acceptancetest.input.folder");
        generateFile(inputFileDir);


        await().atMost(5, TimeUnit.MINUTES).until(outputFileExists(outputFileDir));

        Assert.assertThat(getOutputFileContent(outputFileDir), is("0987654321"));

    }

    private Callable<Boolean> outputFileExists(final String outputFolderPath) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {

                System.out.println("Checking for file..." + outputFolderPath);
                File outputFile = new File(outputFolderPath + "outputFile.txt");

                return outputFile.exists();
            }
        };
    }



    public String getOutputFileContent(String outputFolderPath) {
        File outputFile = new File(outputFolderPath + "outputFile.txt");

        if(outputFile.exists()) {
            FileInputStream fisTargetFile = null;
            try {
                fisTargetFile = new FileInputStream(outputFile);


                String inputFileContent = IOUtils.toString(fisTargetFile, "UTF-8");

                return inputFileContent;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        return null;
    }

    public void generateFile(String inputFolderPath) throws IOException {

        File inputFile = new File(inputFolderPath + "inputFile.txt");
        FileUtils.writeStringToFile(inputFile, "1234567890", "UTF-8", false);
    }
}
