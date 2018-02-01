package com.gobenefit.util;

import static com.gobenefit.util.AppConstant.IMG_PARENT_FOLDER_NAME;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;

public class AmazonS3Utils {

	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

	private static Pattern pattern = Pattern.compile(IMAGE_PATTERN);

	private static Map<String, Set<String>> uploadedImageMap = new HashMap<String, Set<String>>();

	public static void listBucketsContent(AmazonS3 conn) {
		// get list of buckets
		List<Bucket> buckets = conn.listBuckets();
		for (Bucket bucket : buckets) {
			// print information of each bucket
			listBucketContent(conn, bucket);
		}
	}

	public static void listBucketsContent(AmazonS3 conn, String bucketName) {
		// get list of buckets
		List<Bucket> buckets = conn.listBuckets();
		for (Bucket bucket : buckets) {
			if (bucket.getName().equalsIgnoreCase(bucketName))
				// print information of bucket
				listBucketContent(conn, bucket);
		}
	}

	public static void listBucketContent(AmazonS3 conn, Bucket bucket) {
		System.out.println(bucket.getName() + "\t" + StringUtils.fromDate(bucket.getCreationDate()));
		// listing contents of each bucket
		ObjectListing objects = conn.listObjects(bucket.getName());
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				System.out.println(objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t"
						+ StringUtils.fromDate(objectSummary.getLastModified()));
			}
			objects = conn.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
	}

	public static void createBucket(AmazonS3 conn, String bucketName) {
		if (!conn.doesBucketExist(bucketName)) {
			conn.createBucket(bucketName, Region.US_Standard);
		}
	}

	public static void addObjectToBucketFromFile(AmazonS3 conn, String bucketName, String keyName, File inputFile)
			throws IOException {
		conn.putObject(bucketName, keyName, inputFile);
	}

	public static void addObjectToBucketFromStream(AmazonS3 conn, String bucketName, String keyName,
			InputStream inputStream, Map<String, String> metadataMap) throws IOException {
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			if (metadataMap != null) {
				for (Iterator<String> iterator = metadataMap.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next();
					String value = metadataMap.get(key);
					metadata.addUserMetadata(key, value);
				}
			}
			metadata.setContentLength(inputStream.available());
			if (isImageFile(keyName))
				metadata.setContentType("image/jpeg");
			PutObjectRequest por = new PutObjectRequest(bucketName, keyName, inputStream, metadata);
			por.setCannedAcl(CannedAccessControlList.PublicRead);
			conn.putObject(por);
			putInUploadedImageMap(keyName);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	public static void downloadObjectFromBucketToFile(AmazonS3 conn, String bucketName, String keyName,
			String localFileLocation, Map<String, String> metadataMap) throws Exception {
		ObjectMetadata metadata = conn.getObject(new GetObjectRequest(bucketName, keyName),
				new File(localFileLocation + File.separatorChar + keyName));
		if (metadataMap != null) {
			Map<String, String> userMetadataMap = metadata.getUserMetadata();
			if (userMetadataMap != null && !userMetadataMap.isEmpty()) {
				metadataMap.putAll(userMetadataMap);
			}
		}
	}

	public static InputStream downloadObjectFromBucketToStream(AmazonS3 conn, String bucketName, String keyName,
			Map<String, String> metadataMap) throws Exception {
		S3Object object = conn.getObject(new GetObjectRequest(bucketName, keyName));
		if (metadataMap != null) {
			Map<String, String> userMetadataMap = object.getObjectMetadata().getUserMetadata();
			if (userMetadataMap != null && !userMetadataMap.isEmpty()) {
				metadataMap.putAll(userMetadataMap);
			}
		}
		return object.getObjectContent();
	}

	public static void downloadObjectFromBucketToOutputStream(AmazonS3 conn, String bucketName, String keyName,
			OutputStream dataStream, Map<String, String> metadataMap) throws Exception {
		InputStream downloadedStream = null;
		try {
			downloadedStream = AmazonS3Utils.downloadObjectFromBucketToStream(conn, bucketName, keyName, metadataMap);
			int bytesRead = -1;
			byte[] byteBuffer = new byte[4096];
			while ((bytesRead = downloadedStream.read(byteBuffer)) != -1) {
				dataStream.write(byteBuffer, 0, bytesRead);
			}
			dataStream.flush();
		} finally {
			if (downloadedStream != null) {
				downloadedStream.close();
			}
		}
	}

	public static void deleteObject(AmazonS3 conn, String bucketName, String key) {
		conn.deleteObject(bucketName, key);
		String type = extractCompanyTypeFromPath(key);
		if (org.apache.commons.lang.StringUtils.isNotBlank(type) && uploadedImageMap.containsKey(type))
			uploadedImageMap.remove(extractCompanyTypeFromPath(key));
	}

	public static void deleteBucket(AmazonS3 conn, String bucketName) {
		conn.deleteBucket(bucketName);
	}

	public static void closeClientConnection(AmazonS3 conn) {
		if (conn != null && conn instanceof AmazonWebServiceClient) {
			((AmazonWebServiceClient) conn).shutdown();
		}
	}

	public static String getUniqueKey(String transactionPID, String connectionId, String fileName) {
		String md5Hex = DigestUtils.md5Hex(transactionPID);
		// get first 4 characters of md5Hex
		String md5HexSubStr = md5Hex.substring(0, 4);
		return md5HexSubStr + "-" + connectionId + "/" + transactionPID + "/" + fileName;
	}

	public static boolean isImageFile(String fileName) {
		Matcher matcher = pattern.matcher(fileName);
		return matcher.matches();
	}

	public static boolean isFileExist(AmazonS3 conn, String bucketName, String path) {
		String type = extractCompanyTypeFromPath(path);
		return (org.apache.commons.lang.StringUtils.isBlank(type) || !uploadedImageMap.containsKey(type)) ? false
				: uploadedImageMap.get(type).contains(path);
	}

	/**
	 * @param conn
	 * @param bucketName
	 */
	public static void populateUploadedImageMap(AmazonS3 conn, String bucketName) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
				.withPrefix(IMG_PARENT_FOLDER_NAME + "/" + "IMG_CHILD_FOLDER_NAME");
		ObjectListing objects = conn.listObjects(listObjectsRequest);
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				putInUploadedImageMap(objectSummary.getKey());
			}
			objects = conn.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
	}

	private static String extractCompanyTypeFromPath(String filePath) {
		String[] filePathArray = (filePath != null) ? filePath.split("/") : new String[0];
		return (filePathArray.length > 1) ? filePathArray[1] : "";
	}

	private static void putInUploadedImageMap(String filePath) {
		String type = extractCompanyTypeFromPath(filePath);
		Set<String> imagesListOfCompany = null;
		if (uploadedImageMap.containsKey(type)) {
			uploadedImageMap.get(type).add(filePath);
		} else {
			imagesListOfCompany = new HashSet<String>();
			imagesListOfCompany.add(filePath);
			uploadedImageMap.put(type, imagesListOfCompany);
		}
	}

}
