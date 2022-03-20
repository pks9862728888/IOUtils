package com.fluorescence.IOUtils.shared.enums;

public enum FileExtensionEnum {
    PDF(".pdf");

    private final String extension;

    FileExtensionEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
