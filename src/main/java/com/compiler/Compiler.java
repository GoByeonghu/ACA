package com.compiler;

import com.config.ConfigManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Compiler {
    private String compilerPath;
    protected ConfigManager configManager;
    protected List<String> command;

    public Compiler(){
        configManager = new ConfigManager();
        command = new ArrayList<>();
    }

    public abstract boolean setCommand(String sourceFilePath, String outputDirectoryPath);

    public String getCompilerPath() {
        return compilerPath;
    }

    public void setCompilerPath(String compilerPath) {
        this.compilerPath = compilerPath;
    }

    public String compile(String sourceFilePath, String outputDirectoryPath){
        // Ensure the output directory exists
        File outputDirectory = new File(outputDirectoryPath);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        File sourceFile = new File(sourceFilePath);
        String fileNameWithoutExtension = sourceFile.getName().replaceFirst("[.][^.]+$", "");

        setCommand(sourceFilePath, outputDirectoryPath);
        //System.out.println("Compilation command: " + command);

        // Create ProcessBuilder instance
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(System.getProperty("user.dir"))); // Set working directory to current directory

        processBuilder.redirectErrorStream(true); // Redirect error stream to output stream

        try {
            // Start compilation process
            Process process = processBuilder.start();

            // Capture and print the output and error messages
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Check if compilation was successful
            if (exitCode == 0) {
                System.out.println("Compilation successful.");
            } else {
                System.err.println("Compilation failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during compilation: " + e.getMessage());
            e.printStackTrace();
        }

        return outputDirectoryPath+"/"+fileNameWithoutExtension;
    }

}
