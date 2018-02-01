package com.gobenefit.transport.amazons3;

import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.gobenefit.transport.IntermediateFileExchanger;
import com.gobenefit.util.AmazonS3Utils;

@Component
@Scope("prototype")
public class AmazonS3FileExchanger implements IntermediateFileExchanger {

	@Autowired
	AmazonS3 conn;

	@Value("${app.amazon.server.s3.bucket.name}")
	private String bucketName;

	@Override
	public void putFileFromStream(String fileName, InputStream dataStream) throws Exception {
		try {
			AmazonS3Utils.addObjectToBucketFromStream(conn, bucketName, fileName, dataStream, null);
		} finally {
			AmazonS3Utils.closeClientConnection(conn);
		}
	}

	@Override
	public void deleteFile(String fileName) throws Exception {
		try {
			AmazonS3Utils.deleteObject(conn, bucketName, fileName);
		} finally {
			AmazonS3Utils.closeClientConnection(conn);
		}
	}

	@Override
	public boolean exist(String fileName) throws Exception {
		try {
			return AmazonS3Utils.isFileExist(conn, bucketName, fileName);
		} finally {
			AmazonS3Utils.closeClientConnection(conn);
		}
	}

	@PostConstruct
	public void initialize() {
		try {
			AmazonS3Utils.createBucket(conn, bucketName);
			AmazonS3Utils.populateUploadedImageMap(conn, bucketName);
		} catch (Exception e) {
			// ignore this exception if application is running in locally
			e.printStackTrace();
		} finally {
			AmazonS3Utils.closeClientConnection(conn);
		}

	}

	@Override
	public void getFileToStream(String fileName, OutputStream dataStream) throws Exception {

	}

}
