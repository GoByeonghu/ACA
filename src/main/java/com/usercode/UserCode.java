package com.usercode;

import com.result.Result;

import java.io.File;

public class UserCode {
    public enum Language {JAVA, CPP, PY}

    private Language language;

    private String nickname;
    private String sourceFilePath;


    private String testCasePath;

    public UserCode(String nickname, String sourceFilePath, String testCasePath) {
        this.nickname = nickname;
        this.sourceFilePath = sourceFilePath;
        this.testCasePath = testCasePath;
        setSpecies(sourceFilePath);
    }

    public String getTestCasePath() {
        return testCasePath;
    }

    public String getNickname() { return nickname; }
    public String getSourceFilePath() { return sourceFilePath; }
    public Language getSpecies() {
        return language;
    }

    public void setSpecies(String sourceFilePath) {
        File sourceFile = new File(sourceFilePath);
        String fileExtension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.') + 1);

        switch (fileExtension){
            case "java" :
                this.language = Language.JAVA;
                break;
            case "py" :
                this.language = Language.PY;
                break;
            case "cpp":
                this.language = Language.CPP;
                break;
            default:
                System.out.println("Wrong File");
        }
    }
}
