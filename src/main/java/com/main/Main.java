package com.main;

import com.comparer.Comparer;
import com.compiler.CppCompiler;
import com.compiler.JavaCompiler;
import com.config.ConfigManager;
import com.executor.CppExecutor;
import com.executor.JavaExecutor;
import com.executor.PyExecutor;
import com.result.Input;
import com.result.Result;
import com.usercode.UserCode;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){

        System.out.println("HI I'm ACA");

        ConfigManager configManager = new ConfigManager();

        //////////////////////////////////////////////////////userInput
        String sourceFilePath1="userData/A.java";
        String sourceFilePath2="userData/B.cpp";
        String testCasePath="Settings/test_case";
        String resultFilePath="Settings/report";
        //////////////////////////////////////////////////////userInput
        resultFilePath=configManager.getReportPath();
        testCasePath=configManager.getTest_casePath();
        String buildFilePath= configManager.getBuildFilePath();

        UserCode userCode1 = new UserCode("A",sourceFilePath1, testCasePath);
        UserCode userCode2 = new UserCode("B",sourceFilePath2, testCasePath);

        List<Result> results1;
        List<Result> results2;
        List<Input> inputs=new ArrayList<>();
        results1=compileAndExecute(userCode1, inputs, buildFilePath);
        results2=compileAndExecute(userCode2, inputs, buildFilePath);

        Comparer comparer = new Comparer(resultFilePath);
        comparer.compareToFile(userCode1,userCode2, inputs, results1, results2);
        comparer.compareToTerminal(userCode1,userCode2, inputs, results1, results2);

    }

    private static List<Result> compileAndExecute(UserCode userCode, List<Input> inputs, String buildFilePath){
        //컴파일 후 실행
        UserCode.SPECIES species;
        species=userCode.getSpecies();
        String executionPath;
        List<Result> results;
        //List<Input> inputs;
        switch (species) {
            case CPP:
                CppCompiler cppCompiler = new CppCompiler();
                executionPath=cppCompiler.compile(userCode.getSourceFilePath(), buildFilePath);
                CppExecutor cppExecutor = new CppExecutor();
                inputs.addAll(cppExecutor.setInput(userCode.getTestCasePath()));
                results=cppExecutor.execute(executionPath,inputs);
                break;
            case JAVA:
                JavaCompiler javaCompiler = new JavaCompiler();
                executionPath=javaCompiler.compile(userCode.getSourceFilePath(), buildFilePath);
                JavaExecutor javaExecutor = new JavaExecutor();
                inputs.addAll(javaExecutor.setInput(userCode.getTestCasePath()));
                results=javaExecutor.execute(executionPath,inputs);
                break;
            case PY:
                PyExecutor pyExecutor = new PyExecutor();
                inputs.addAll(pyExecutor.setInput(userCode.getTestCasePath()));
                results=pyExecutor.execute(userCode.getSourceFilePath(),inputs);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + species);
        };
        return results;
    }



}
