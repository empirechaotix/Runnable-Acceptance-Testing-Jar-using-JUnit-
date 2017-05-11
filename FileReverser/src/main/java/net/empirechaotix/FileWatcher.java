package net.empirechaotix;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileWatcher extends Thread {

    private boolean keepRunning = true;

    public FileWatcher() {

    }

    private String getFolderPathFromProperty(String propertyName) {
        String property = System.getProperty(propertyName);

        if(property != null && !property.equals("")) {
            return property;
        }

        //throw new IllegalStateException(String.format("Missing the environment variable [%s]", propertyName));
        return "";
    }


    public void run () {

        String outputFileDir = getFolderPathFromProperty("acceptancetest.output.folder");
        String inputFileDir = getFolderPathFromProperty("acceptancetest.input.folder");

        System.out.println("Checking for files in: " + inputFileDir);


        while(keepRunning) {
            try {
                //every 10 seconds
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("Checking...");


            File inputFile = new File(inputFileDir + "inputFile.txt");

            if(inputFile.exists()) {
                System.out.println("Found file...");
                FileInputStream fisTargetFile = null;
                try {
                    fisTargetFile = new FileInputStream(inputFile);


                String inputFileContent = IOUtils.toString(fisTargetFile, "UTF-8");

                String reversedString = new StringBuilder(inputFileContent).reverse().toString();

                File outputFile = new File(outputFileDir + "outputFile.txt");
                FileUtils.writeStringToFile(outputFile, reversedString, "UTF-8", false);


                inputFile.renameTo(new File("inputFile.txt.processed"));
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }


            }

            //check for file

            //if file found, open it, read it's content, reverse content, spool to output file of same name in different folder


        }
    }
}
