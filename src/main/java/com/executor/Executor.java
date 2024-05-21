package com.executor;

import com.config.ConfigManager;
import com.result.Input;
import com.result.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Executor {
    protected ConfigManager configManager;
    protected List<String> command;

    public Executor() {
        configManager = new ConfigManager();
        command = new ArrayList<>();
    }

    public abstract boolean setCommand(String executablePath);

    public List<Input> setInput(String inputPath) {
        List<Input> inputs = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            List<String> inputLines = new ArrayList<>();
            boolean isCase = false;

            while ((line = fileReader.readLine()) != null) {
                if (line.trim().equals("#case")) {
                    if (isCase) {
                        inputs.add(new Input(new ArrayList<>(inputLines)));
                        inputLines.clear();
                    }
                    isCase = true;
                } else if (line.trim().equals("#end")) {
                    if (isCase) {
                        inputs.add(new Input(new ArrayList<>(inputLines)));
                        inputLines.clear();
                        isCase = false;
                    }
                } else if (isCase) {
                    inputLines.add(line);
                }
            }

            if (isCase) {
                inputs.add(new Input(new ArrayList<>(inputLines)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputs;
    }

    public List<Result> execute(String executablePath, List<Input> inputs) {
        List<Result> results = new ArrayList<>();

        for (Input input : inputs) {
            results.add(executeSingleCase(executablePath, input.getInput()));
        }

        return results;
    }

    private Result executeSingleCase(String executablePath, List<String> inputLines) {
        setCommand(executablePath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(System.getProperty("user.dir")));
        processBuilder.redirectErrorStream(true);

        List<String> output = new ArrayList<>();
        long executionTime = 0;
        long memoryUsage = 0;

        try {
            long startTime = System.currentTimeMillis();
            Process process = processBuilder.start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                for (String input : inputLines) {
                    writer.write(input);
                    writer.newLine();
                }
                writer.flush();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }

            int exitCode = process.waitFor();
            executionTime = System.currentTimeMillis() - startTime;
            memoryUsage = getMemoryUsage(process);

            if (exitCode == 0) {
                //System.out.println("Successfully completed execution");
            } else {
                System.err.println("Execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during Execution: " + e.getMessage());
            e.printStackTrace();
        }

        return new Result(output, executionTime, memoryUsage);
    }

    private long getMemoryUsage(Process process) {
        long memoryUsage = 0;

        try {
            long pid = process.pid();
            ProcessBuilder processBuilder = new ProcessBuilder("ps", "-p", Long.toString(pid), "-o", "rss=");
            Process psProcess = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()))) {
                String line;
                if ((line = reader.readLine()) != null) {
                    memoryUsage = Long.parseLong(line.trim()) * 1024; // Convert to bytes
                }
            }

            psProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return memoryUsage;
    }
}

//package com.executor;
//
//import com.config.ConfigManager;
//import com.result.Result;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class Executor {
//    protected ConfigManager configManager;
//    protected List<String> command;
//
//    public Executor() {
//        configManager = new ConfigManager();
//        command = new ArrayList<>();
//    }
//
//    public abstract boolean setCommand(String executablePath);
//
//    public Result execute(String executablePath) {
//
//        setCommand(executablePath);
//        //System.out.println("Compilation command: " + command);
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.directory(new File(System.getProperty("user.dir"))); // Set working directory to current directory
//        processBuilder.redirectErrorStream(true); // Redirect error stream to output stream
//
//        List<String> output = new ArrayList<>();
//        long executionTime = 0;
//        long memoryUsage = 0;
//
//        try {
//            // Start compilation process
//            long startTime = System.currentTimeMillis();
//            Process process = processBuilder.start();
//
//            // Capture and print the output and error messages
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    output.add(line);
//                    //System.out.println(line);
//                }
//            }
//
//            // Wait for the process to finish
//            int exitCode = process.waitFor();
//
//            // Calculate execution time
//            executionTime = System.currentTimeMillis() - startTime;
//
//            // Get memory usage (not implemented in this example)
//            memoryUsage = getMemoryUsage(process);
//
//            // Check if compilation was successful
//            if (exitCode == 0) {
//                System.out.println("Successfully completed execution");
//            } else {
//                System.err.println("Execution failed with exit code: " + exitCode);
//            }
//
//
//        } catch (IOException | InterruptedException e) {
//            System.err.println("Error during Execution: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return new Result(output, executionTime, memoryUsage);
//    }
//
//    //////////////////////////////////////
//    private long getMemoryUsage(Process process) {
//        long memoryUsage = 0;
//
//        try {
//            // Get the PID of the process
//            long pid = process.pid();
//
//            // Run ps command to get memory usage
//            ProcessBuilder processBuilder = new ProcessBuilder("ps", "-p", Long.toString(pid), "-o", "rss="); //| awk '{print $1*1024}'");
//            Process psProcess = processBuilder.start();
//
//            // Read the output of ps command
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(psProcess.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println("내용: " + line);
//                    // Parse the output and extract memory usage
//                    memoryUsage = Long.parseLong(line.trim()) * 1024; // Convert to bytes
//                }
//            }
//
//            // Wait for the ps process to finish
//            psProcess.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return memoryUsage;
//    }
//    ///////////////////////////////////////
//}
