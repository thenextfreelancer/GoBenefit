package com.gobenefit.transport;

import java.io.InputStream;
import java.io.OutputStream;

public interface IntermediateFileExchanger {

	void getFileToStream(String fileName, OutputStream dataStream) throws Exception;

	void putFileFromStream(String fileName, InputStream dataStream) throws Exception;

	void deleteFile(String fileName) throws Exception;

	boolean exist(String fileName) throws Exception;

}
