package com.executor;

import com.config.ConfigManager;
import com.memorymonitor.MemoryMonitor;
import com.result.TestCaseInput;
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

    public List<TestCaseInput> setInput(String inputPath) {
        List<TestCaseInput> inputs = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            List<String> inputLines = new ArrayList<>();
            boolean isCase = false;

            while ((line = fileReader.readLine()) != null) {
                if (line.trim().equals("#case")) {
                    if (isCase) {
                        inputs.add(new TestCaseInput(new ArrayList<>(inputLines)));
                        inputLines.clear();
                    }
                    isCase = true;
                } else if (line.trim().equals("#end")) {
                    if (isCase) {
                        inputs.add(new TestCaseInput(new ArrayList<>(inputLines)));
                        inputLines.clear();
                        isCase = false;
                    }
                } else if (isCase) {
                    inputLines.add(line);
                }
            }

            if (isCase) {
                inputs.add(new TestCaseInput(new ArrayList<>(inputLines)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputs;
    }

    public List<Result> execute(String executablePath, List<TestCaseInput> inputs) {
        List<Result> results = new ArrayList<>();

        for (TestCaseInput input : inputs) {
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

            // 메모리 모니터 시작
            MemoryMonitor memoryMonitor = new MemoryMonitor(process.pid());
            Thread memoryMonitorThread = new Thread(memoryMonitor);
            memoryMonitorThread.start();

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

            // 메모리 모니터 종료
            memoryMonitor.stop();
            memoryMonitorThread.join();
            memoryUsage = memoryMonitor.getMaxMemoryUsage();

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



}
