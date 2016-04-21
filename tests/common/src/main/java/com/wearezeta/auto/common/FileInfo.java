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

    public long getFileSize() {
        return fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }
}
