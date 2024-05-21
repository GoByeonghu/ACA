package com.config;

import java.io.File;///
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private final String CONFIG_FILE_PATH = "settings/config";
    private Properties properties;

    private String test_casePath;
    private String reportPath;
    private String buildFilePath;

    private String cPath;
    private String cppPath;
    private String pyPath;
    private String javaPath;

    public String getBuildFilePath() {
        return buildFilePath;
    }

    public void setBuildFilePath(String buildFilePath) {
        this.buildFilePath = buildFilePath;
    }

    public String getTest_casePath() {
        return test_casePath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public String getcPath() {
        return cPath;
    }

    public String getCppPath() {
        return cppPath;
    }

    public String getPyPath() {
        return pyPath;
    }

    public String getJavaPath() {
        return javaPath;
    }

    public ConfigManager() {
        properties = new Properties();
        readProperties(CONFIG_FILE_PATH);
        SetField();
    }

    public ConfigManager(String configFilePath) {
        properties = new Properties();
        readProperties(configFilePath);
        SetField();
    }

    private void SetField(){
        this.reportPath = properties.getProperty("report");
        this.test_casePath = properties.getProperty("test_case");
        this.buildFilePath = properties.getProperty("build");

        this.cPath = properties.getProperty("c");
        this.cppPath = properties.getProperty("cpp");
        this.pyPath = properties.getProperty("py");
        this.javaPath = properties.getProperty("java");

    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    private void readProperties(String configFilePath) {
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProperties(String configFilePath) {
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

