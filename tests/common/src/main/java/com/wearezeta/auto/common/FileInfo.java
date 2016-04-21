package com.wearezeta.auto.common;

public class FileInfo {

    private String fileName;
    private long fileSize;
    private String mimeType;

    public FileInfo(String fileName, long fileSize, String mimeType) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
