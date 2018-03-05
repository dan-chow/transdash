package chow.dan.common;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

public class FileHelper {
	public static String createTmpFile(Content content) throws IOException {
		File file = new File(UUID.randomUUID().toString());
		FileUtils.writeByteArrayToFile(file, content.getData());
		return file.getAbsolutePath();
	}

	public static String createEmptyFile(String pathname) throws IOException {
		File file = new File(pathname);
		FileUtils.writeByteArrayToFile(file, new byte[0]);
		return file.getAbsolutePath();
	}

	public static String createTmpFolder() throws IOException {
		File folder = new File(UUID.randomUUID().toString());
		FileUtils.forceMkdir(folder);
		return folder.getAbsolutePath();
	}

}
