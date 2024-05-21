package com.compiler;

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



}
