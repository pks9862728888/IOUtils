package com.fluorescence.IOUtils.shared.services;

import com.fluorescence.IOUtils.shared.enums.FileExtensionEnum;
import com.fluorescence.IOUtils.shared.exceptions.FileNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static com.fluorescence.IOUtils.shared.services.RandomUtils.generateRandomUUID;

@Service
public class FileOperationsService {

    public void throwExceptionIfFileNotExists(@NonNull String fileName) throws FileNotFoundException {
        if (!new File(fileName).exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
    }

    public String generateRandomFileName(FileExtensionEnum extensionEnum) {
        return generateRandomUUID() + extensionEnum.getExtension();
    }

    public String renameFile(@NonNull String sourceFileNameWithPath, String newFileName) {
        File destination = Paths.get(sourceFileNameWithPath).resolveSibling(newFileName).toFile();
        if (new File(sourceFileNameWithPath).renameTo(destination)) {
            return destination.getAbsolutePath();
        } else {
            return sourceFileNameWithPath;
        }
    }

    public void deleteTempFiles(List<File> tempFilesToDelete, File exclude) {
        tempFilesToDelete.stream()
                .filter(f -> !f.getName().equals(exclude.getName()))
                .forEach(this::deleteFile);
    }

    private void deleteFile(File file) {
        System.out.println("Deleting file: " + file.getName());
        if (file.delete()) {
            System.out.println("File deleted!");
        } else {
            System.out.println("Delete failed!");
        }
    }
}
