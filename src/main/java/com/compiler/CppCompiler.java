package com.compiler;
import com.config.ConfigManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CppCompiler extends Compiler{

    public CppCompiler(){
        super();
        setCompilerPath(configManager.getCppPath());
    }

    public boolean setCommand(String sourceFilePath, String outputDirectoryPath){
        File sourceFile = new File(sourceFilePath);
        String fileNameWithoutExtension = sourceFile.getName().replaceFirst("[.][^.]+$", "");

        // Prepare command for compiling
        if("null".equals(getCompilerPath())){
            command.add("g++");
        }
        else{
            command.add(getCompilerPath());
        }
        command.add("-o");
        command.add(outputDirectoryPath + "/"+fileNameWithoutExtension); // Ensure this is a directory path
        command.add(sourceFilePath);

        return true;
    }

    public boolean isCompilerAvailable() {
        try {
            Process process = new ProcessBuilder("g++", "--version").start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
