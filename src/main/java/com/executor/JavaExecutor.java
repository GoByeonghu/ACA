package com.executor;

import java.io.File;

public class JavaExecutor extends Executor{

    public JavaExecutor() {
        super();
    }

    public boolean setCommand(String executablePath){
        File sourceFile = new File(executablePath);
        String parentDirectoryPath = sourceFile.getParent();
        String fileNameWithoutExtension = sourceFile.getName().replaceFirst("[.][^.]+$", "");


        // Prepare command for compiling
        command.add("java");
        command.add("-cp");
        command.add(parentDirectoryPath);
        command.add(fileNameWithoutExtension);
        return true;
    }


}
