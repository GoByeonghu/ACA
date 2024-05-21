package com.comparer;

import com.executor.Executor;
import com.result.Input;
import com.result.Result;
import com.usercode.UserCode;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Comparer {
    private String resultFilePath;

    public Comparer(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    private void saveToFile(Result result, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Writing output to the file
            for (String line : result.getOutput()) {
                writer.println(line);
            }
            writer.println("Execution Time: " + result.getExecutionTime() + " ms");
            writer.println("Memory Usage: " + result.getMemoryUsage() + " bytes");
            System.out.println("Results saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error occurred while saving results to file: " + e.getMessage());
        }
    }

    public void compare(UserCode userCode1, UserCode userCode2, List<Input> inputs,
                        List<Result> results1, List<Result> results2, boolean toFile) {
        try (PrintWriter writer = toFile ? new PrintWriter(new FileWriter(resultFilePath)) : new PrintWriter(System.out)) {
            // Output header
            writer.println("--------------------------------------");
            writer.println("A = " + userCode1.getSourceFilePath());
            writer.println("B = " + userCode2.getSourceFilePath());
            writer.println("Test Case = " + userCode1.getTestCasePath());
            writer.println("--------------------------------------");
            // 반복을 위해 가장 작은 리스트의 크기 사용
            int minSize = Math.min(inputs.size(), Math.min(results1.size(), results2.size()));
            for (int i = 0; i < minSize; i++) {
                Input input = inputs.get(i);
                Result result1 = results1.get(i);
                Result result2 = results2.get(i);

                writer.println("#CASE " + (i + 1));
                writer.println("INPUT");
                for (String line : input.getInput()) {
                    writer.println(line);
                }
                writer.println("OUTPUT");

                if (result1.getOutput().equals(result2.getOutput())) {
                    writer.println(userCode1.getNickname() + "과 " + userCode2.getNickname() + "이 동일합니다.");
                    writer.println(userCode1.getNickname());
                    for (String line : result1.getOutput()) {
                        writer.println(line);
                    }
                    writer.println("Execution Time: " + result1.getExecutionTime() + " ms");
                    writer.println("Memory Usage: " + result1.getMemoryUsage() + " bytes");
                    writer.println(userCode2.getNickname());
                    for (String line : result2.getOutput()) {
                        writer.println(line);
                    }
                    writer.println("Execution Time: " + result2.getExecutionTime() + " ms");
                    writer.println("Memory Usage: " + result2.getMemoryUsage() + " bytes");
                } else {
                    writer.println("!!!결과가 다릅니다!!!");
                    writer.println("********************************");
                    writer.println(userCode1.getNickname());
                    for (String line : result1.getOutput()) {
                        writer.println(line);
                    }
                    writer.println("Execution Time: " + result1.getExecutionTime() + " ms");
                    writer.println("Memory Usage: " + result1.getMemoryUsage() + " bytes");
                    writer.println(userCode2.getNickname());
                    for (String line : result2.getOutput()) {
                        writer.println(line);
                    }
                    writer.println("Execution Time: " + result2.getExecutionTime() + " ms");
                    writer.println("Memory Usage: " + result2.getMemoryUsage() + " bytes");
                    writer.println("********************************");
                }
                writer.println();
                writer.println();
            }

            if (toFile) {
                System.out.println("Comparison results saved to " + resultFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error occurred while saving comparison results to file: " + e.getMessage());
        }
    }

    public void compareToFile(UserCode userCode1, UserCode userCode2, List<Input> inputs,
                              List<Result> results1, List<Result> results2) {
        compare(userCode1, userCode2, inputs, results1, results2, true);
    }

    public void compareToTerminal(UserCode userCode1, UserCode userCode2, List<Input> inputs,
                                  List<Result> results1, List<Result> results2) {
        compare(userCode1, userCode2, inputs, results1, results2, false);
    }
}
