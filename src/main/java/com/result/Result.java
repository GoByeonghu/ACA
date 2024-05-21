package com.result;

import java.util.List;

public class Result {
    private List<String> output;
    private long executionTime; // in milliseconds
    private long memoryUsage;   // in bytes

    public Result(List<String> output, long executionTime, long memoryUsage) {
        this.output = output;
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public List<String> getOutput() {
        return output;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
