package com.hiveag.geepy.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;


public class GestionArchivos {
	private static final long MEGABYTE = 1024L * 1024L;
	 //private static String UPLOADED_FOLDER = "/home/ubuntu/images/";
	private static String UPLOADED_FOLDER = "C:\\Users\\Ivan\\Desktop\\geepy";
	
	
	public static String getUPLOADED_FOLDER() {
		return UPLOADED_FOLDER;
	}

	public static boolean validateSize(long bytes) {
		Long megas = bytes / MEGABYTE;

		if (megas < 4)
			return true;
		return false;

	}

	public static void saveUploadedFiles(String nameFile, MultipartFile file) throws IOException {
		byte[] bytes = file.getBytes();
		Path path = Paths.get(UPLOADED_FOLDER + nameFile);
		Files.write(path, bytes);
	}

	public static void deleteFile(String nameFile) throws IOException {
		Path fileToDeletePath = Paths.get(UPLOADED_FOLDER + nameFile);
		Files.delete(fileToDeletePath);

	}

	public static String getExtensionOfFile(String fileName) {
		String fileExtension = "";
		// If fileName do not contain "." or starts with "." then it is not a valid file
		if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		return fileExtension;
	}

}
