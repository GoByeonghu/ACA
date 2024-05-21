package com.executor;

import java.io.File;

public class CppExecutor extends Executor{
    public CppExecutor() {
        super();
    }

    public boolean setCommand(String executablePath) {
        // Prepare command for compiling
        command.add(executablePath);
        return true;
    }
}
