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

public class MainCLI {

    public static void main(String[] args){

        System.out.println("HI I'm ACA");
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

        // 명령줄 인수 파싱
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t":
                    if (i + 1 < args.length) {
                        testCasePath = args[++i];
                    } else {
                        testCasePath=configManager.getTest_casePath();
                        return;
                    }
                    break;
                case "-o":
                    if (i + 1 < args.length) {
                        resultFilePath = args[++i];
                    } else {
                        resultFilePath=configManager.getReportPath();
                        return;
                    }
                    break;
                default:
                    if (sourceFilePath1 == null) {
                        sourceFilePath1 = args[i];
                    } else if (sourceFilePath2 == null) {
                        sourceFilePath2 = args[i];
                    } else {
                        System.err.println("Error: Too many arguments");
                        return;
                    }
                    break;
            }
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
        comparer.compareToFile(userCode1,userCode2, inputs, results1, results2);
        comparer.compareToTerminal(userCode1,userCode2, inputs, results1, results2);

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
