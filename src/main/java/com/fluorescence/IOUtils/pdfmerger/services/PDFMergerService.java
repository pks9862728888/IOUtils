package com.fluorescence.IOUtils.pdfmerger.services;

import com.fluorescence.IOUtils.pdfmerger.exceptions.PDFOperationException;
import com.fluorescence.IOUtils.shared.exceptions.FileNotFoundException;
import com.fluorescence.IOUtils.shared.services.FileOperationsService;
import lombok.NonNull;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static com.fluorescence.IOUtils.shared.enums.FileExtensionEnum.PDF;

@Service
public class PDFMergerService {

    @Autowired
    private FileOperationsService fileOperationsService;

    public String mergePdfs(@NonNull String pdf1, @NonNull String pdf2)
            throws FileNotFoundException, PDFOperationException {
        // Throw exception if pdf does not exist
        fileOperationsService.throwExceptionIfFileNotExists(pdf1);
        fileOperationsService.throwExceptionIfFileNotExists(pdf2);

        // Input files
        File pdf1File = new File(pdf1);
        File pdf2File = new File(pdf2);

        // Destination file
        File destinationFile = Paths.get("results", "pdfmerger",
                fileOperationsService.generateRandomFileName(PDF)).toFile();

        // Merging PDF
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        try {
            pdfMerger.addSource(pdf1File);
            pdfMerger.addSource(pdf2File);
            pdfMerger.setDestinationFileName(destinationFile.getAbsolutePath());
            System.out.printf("Merging pdf: %s and %s%n", pdf1File.getName(), pdf2File.getName());
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly(), CompressParameters.DEFAULT_COMPRESSION);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PDFOperationException(e.getMessage());
        }

        return destinationFile.toString();
    }
}
