package com.main;

import com.comparer.Comparer;
import com.compiler.CppCompiler;
import com.compiler.JavaCompiler;
import com.config.ConfigManager;
import com.executor.CppExecutor;
import com.executor.JavaExecutor;
import com.executor.PyExecutor;
import com.result.TestCaseInput;
import com.result.Result;
import com.usercode.UserCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        System.out.println("HI I'm ACA");
        // For Dummy args[]
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Please enter your command");
        String user_input = scanner.nextLine();
        String[] Dummy_args = user_input.split(" ");
        int startIndex = 0;
        if (Dummy_args.length > 0 && Dummy_args[0].equalsIgnoreCase("aca")) {
            startIndex = 1;
        }
        else{
            System.err.println("Error: Wrong Command");
        }


        ConfigManager configManager = new ConfigManager();
        //////////////////////////////////////////////////////forTest
//        String sourceFilePath1="userData/A.java";
//        String sourceFilePath2="userData/B.cpp";
//        String testCasePath="Settings/test_case";
//        String resultFilePath="Settings/report";
//        resultFilePath=configManager.getReportPath();
//        testCasePath=configManager.getTest_casePath();
        //////////////////////////////////////////////////////forTest


        String sourceFilePath1=null;
        String sourceFilePath2=null;
        String testCasePath=null;
        String resultFilePath=null;

        boolean isExist_T=false;
        boolean isExist_O=false;
        // 명령줄 인수 파싱
        for (int i = startIndex; i < Dummy_args.length; i++) {
            switch (Dummy_args[i]) {
                case "-t":
                    if (i + 1 < Dummy_args.length) {
                        testCasePath = Dummy_args[++i];
                        isExist_T=true;
                    } else {
                        testCasePath=configManager.getTest_casePath();
                        return;
                    }
                    break;
                case "-o":
                    if (i + 1 < Dummy_args.length) {
                        resultFilePath = Dummy_args[++i];
                        isExist_O=true;
                    } else {
                        resultFilePath=configManager.getReportPath();
                        return;
                    }
                    break;
                default:
                    if (sourceFilePath1 == null) {
                        sourceFilePath1 = Dummy_args[i];
                    } else if (sourceFilePath2 == null) {
                        sourceFilePath2 = Dummy_args[i];
                    } else {
                        System.err.println("Error: Too many arguments");
                        return;
                    }
                    break;
            }
        }
        if(!isExist_T){
            testCasePath=configManager.getTest_casePath();
        }
        if(!isExist_O){
            resultFilePath=configManager.getReportPath();
        }

        // 필수 인수가 제공되었는지 확인
        if (sourceFilePath1 == null || sourceFilePath2 == null) {
            System.err.println("Error: Missing source file arguments");
            System.out.println("Usage: aca <file1> <file2> [options]");
            System.out.println("Options:");
            System.out.println("  -t <testcasefile>  Specify the test case file");
            System.out.println("  -o <resultfile>    Save the results to a file");
            return;
        }

        String buildFilePath= configManager.getBuildFilePath();

        UserCode userCode1 = new UserCode("A",sourceFilePath1, testCasePath);
        UserCode userCode2 = new UserCode("B",sourceFilePath2, testCasePath);

        List<Result> results1;
        List<Result> results2;
        List<TestCaseInput> inputs=new ArrayList<>();
        results1=compileAndExecute(userCode1, inputs, buildFilePath);
        results2=compileAndExecute(userCode2, inputs, buildFilePath);

        Comparer comparer = new Comparer(resultFilePath);
        if(!isExist_O){
            comparer.compareToTerminal(userCode1,userCode2, inputs, results1, results2);
        }
        else {
            comparer.compareToFile(userCode1,userCode2, inputs, results1, results2);
        }
    }

    private static List<Result> compileAndExecute(UserCode userCode, List<TestCaseInput> inputs, String buildFilePath){
        //컴파일 후 실행
        UserCode.Language language;
        language=userCode.getSpecies();
        String executionPath;
        List<Result> results;
        //List<Input> inputs;
        switch (language) {
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
                throw new IllegalArgumentException("Unexpected value: " + language);
        };
        return results;
    }



}
