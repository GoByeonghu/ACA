package com.memorymonitor;

//package com.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MemoryMonitor implements Runnable {
    private final long pid;
    private volatile boolean running = true;
    private long maxMemoryUsage = 0;
    private final List<Long> memoryLog = new ArrayList<>();

    public MemoryMonitor(long pid) {
        this.pid = pid;
    }

    public void stop() {
        running = false;
    }

    public long getMaxMemoryUsage() {
        return maxMemoryUsage;
    }

    public List<Long> getMemoryLog() {
        return memoryLog;
    }

    @Override
    public void run() {
        while (running) {
            long memoryUsage = getMemoryUsage(pid);
            memoryLog.add(memoryUsage);

            if (memoryUsage > maxMemoryUsage) {
                maxMemoryUsage = memoryUsage;
            }

            try {
                Thread.sleep(1); // 0.001초 간격으로 측정
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private long getMemoryUsage(long pid) {
        long memoryUsage = 0;

        try {
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

