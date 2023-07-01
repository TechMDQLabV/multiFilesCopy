package com.garguir.models;

public class MockFile {
    private String fileName;
    private String parentPath;

    public MockFile(String fileName, String parentPath) {
        this.fileName = fileName;
        this.parentPath = parentPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
