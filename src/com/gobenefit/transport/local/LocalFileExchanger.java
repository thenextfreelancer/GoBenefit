package com.gobenefit.transport.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import com.gobenefit.transport.IntermediateFileExchanger;

public class LocalFileExchanger implements IntermediateFileExchanger {

	private static IntermediateFileExchanger fileExchanger = new LocalFileExchanger();

	private LocalFileExchanger() {
	}

	@Override
	public void getFileToStream(String fileName, OutputStream dataStream) throws Exception {
		fileName = getServerPath() + File.separator + fileName;
		try (InputStream fileStream = new FileInputStream(fileName)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			// read from is to buffer
			while ((bytesRead = fileStream.read(buffer)) != -1) {
				dataStream.write(buffer, 0, bytesRead);
			}
		}
	}

	@Override
	public void putFileFromStream(String filePath, InputStream dataStream) throws Exception {
		try {
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
			String folderPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
			folderPath = getServerPath() + File.separator + folderPath;
			File parentDir = new File(folderPath);
			if (!parentDir.exists())
				parentDir.mkdirs();
			File file = new File(parentDir, fileName);
			file.createNewFile();
			try (FileOutputStream fos = new FileOutputStream(file)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				// read from is to buffer
				while ((bytesRead = dataStream.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			}
		} finally {
			if (dataStream != null)
				try {
					dataStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public void deleteFile(String fileName) throws Exception {
		fileName = getServerPath() + File.separator + fileName;
		File file = new File(fileName);
		if (file.exists())
			file.delete();
	}

	private static String getServerPath() {
		String serverPath = null;
		try {
			serverPath = LocalFileExchanger.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return (serverPath == null) ? "" : serverPath.substring(0, serverPath.indexOf("WEB-INF"));
	}

	@Override
	public boolean exist(String fileName) throws Exception {
		return new File(getServerPath() + File.separator + fileName).exists();
	}

	/**
	 * Return the object of singleton class
	 * 
	 * @return
	 */
	public static IntermediateFileExchanger getInstance() {
		return fileExchanger;
	}

}
