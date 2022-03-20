package com.fluorescence.IOUtils;

import com.fluorescence.IOUtils.pdfmerger.services.PDFMergerService;
import com.fluorescence.IOUtils.shared.enums.FileExtensionEnum;
import com.fluorescence.IOUtils.shared.services.FileOperationsService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class IoUtilsApplication implements CommandLineRunner {

	@Autowired
	protected PDFMergerService pdfMergerService;

	@Autowired
	protected FileOperationsService fileOperationsService;

	public static void main(String[] args) {
		SpringApplication.run(IoUtilsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<File> pdfs = getFileNamesToMerge(Paths.get("inputs", "pdfmerger"));
		if (pdfs.size() < 2) {
			System.out.println("At least two pdf are required for merging.");
			System.exit(-1);
		}
		String finalMergedPdf = null;
		List<File> tempFilesToDelete = new ArrayList<>();
		// Merging PDF and collecting tempFiles to delete
		for (int i = 1; i < pdfs.size(); i++) {
			if (i == 1) {
				finalMergedPdf = pdfMergerService.mergePdfs(pdfs.get(0).toString(), pdfs.get(1).toString());
			} else {
				finalMergedPdf = pdfMergerService.mergePdfs(finalMergedPdf, pdfs.get(i).toString());
			}
			tempFilesToDelete.add(new File(finalMergedPdf));
		}

		// Deleting intermediate temporary files
		fileOperationsService.deleteTempFiles(tempFilesToDelete, new File(finalMergedPdf));

		// Printing out merged pdf file location
		System.out.println("Final Merged pdf: " + fileOperationsService.renameFile(finalMergedPdf, "MergedNotes.pdf"));
		System.exit(0);
	}

	private List<File> getFileNamesToMerge(@NonNull Path path) {
		return Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
				.filter(f -> f.getName().endsWith(FileExtensionEnum.PDF.getExtension()))
				.sorted(Comparator.comparing(File::getName))
				.collect(Collectors.toList());
	}
}
