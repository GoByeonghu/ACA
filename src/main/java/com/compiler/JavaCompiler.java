package com.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JavaCompiler extends Compiler {

    public JavaCompiler(){
        super();
        setCompilerPath(configManager.getJavaPath());
    }

    public boolean setCommand(String sourceFilePath, String outputDirectoryPath){
        // Prepare command for compiling
        if("null".equals(getCompilerPath()) || getCompilerPath()==null){
            command.add("javac");
        }
        else{
            command.add(getCompilerPath());
        }
        command.add("-d");
        command.add(outputDirectoryPath); // Ensure this is a directory path
        command.add(sourceFilePath);

        return true;
    }

    public boolean isCompilerAvailable() {
        try {
            Process process = new ProcessBuilder("javac", "-version").start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }



}
