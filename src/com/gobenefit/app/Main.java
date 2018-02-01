package com.gobenefit.app;

import static com.gobenefit.util.ApplicationUtils.setupLookupCodes;

import java.io.File;
import java.io.IOException;

import com.gobenefit.config.PathConfig;
import com.gobenefit.config.PropertiesConstant;

public class Main {

	public Main(String workingDirPath) throws IOException {
		PathConfig.setWorkingDirectory(new File(workingDirPath).getAbsolutePath());
		System.setProperty(PropertiesConstant.RESOURCE_DIRECTORY_PATH, workingDirPath);
	}

	public void boot() throws Exception {
		setupLookupCodes();
	}

	public void shutdown() throws Exception {
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
