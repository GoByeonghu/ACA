package com.executor;

import java.io.File;

public class PyExecutor extends Executor{

    public PyExecutor() {
        super();
    }

    public boolean setCommand(String executablePath){
        command.add("python3");
        command.add(executablePath);
        return true;
    }
}
