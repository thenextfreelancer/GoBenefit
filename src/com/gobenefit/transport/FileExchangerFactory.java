package com.gobenefit.transport;

import static com.gobenefit.util.AppConstant.AMAZONS3;
import static com.gobenefit.util.AppConstant.LOCAL;

import com.gobenefit.spring.BeanFactory;
import com.gobenefit.transport.local.LocalFileExchanger;

public class FileExchangerFactory {

	public static IntermediateFileExchanger getIntermediateFileExchanger(String type) throws Exception {
		IntermediateFileExchanger fileExchanger = null;
		switch (type) {
		case AMAZONS3: {
			fileExchanger = (IntermediateFileExchanger) BeanFactory.getWebContextBean("amazonS3FileExchanger");
			break;
		}
		case LOCAL: {
			fileExchanger = LocalFileExchanger.getInstance();
			break;
		}
		default:
			fileExchanger = LocalFileExchanger.getInstance();
		}
		return fileExchanger;
	}

}
