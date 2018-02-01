package com.gobenefit.util;

public interface AppConstant {

	String LOOKUP_QUERY_PARAM = "lookup";

	String TOTAL_COUNT = "totalCount";

	String CUSTOM_ENTITY_ALIAS = "customEntityAlias";

	String GENERATED_ALIAS_0 = "generatedAlias0";

	String AMAZONS3 = "AMAZONS3";

	String LOCAL = "LOCAL";

	String IMG_PARENT_FOLDER_NAME = "images";

	String IMG_USER_FOLDER_NAME = "users";

	String IMG_USER_PROFILE = "userProfile.jpg";

	String IMG_APARTMENT_FOLDER_NAME = "apartments";

	String DEFUALT_APARTMENT_IMAGE = "defaultApartment.jpg";

	String DEFAULT_USER_IMAGE = "defaultUserImage.jpg";

	String SEPARATOR = "/";

	String LOOKUP_CODE_REFERENCE = " NVARCHAR(16) NOT NULL REFERENCES LOOKUP_CODE(CODE)";

	String LOOKUP_CODE_REFERENCE_NULLABLE = " NVARCHAR(16) REFERENCES LOOKUP_CODE(CODE)";

	String APP_ADMIN_ROLE = "APP_ADMIN";

	String RWA_ADMIN_ROLE = "RWA_ADMIN";

	String STANDARD_USER_ROLE = "STANDARD";

	String ALL_AUTHETICATED_USER_ROLE = "ALL";

	String SERVICE_PROVIDER_USER_ROLE = "SERVICE_PROVIDER";

	String APARTMENT_ID = "apartmentId";

	String TOWER_ID = "towerId";

	String USER_ID = "userId";

	String ID = "id";
	
	String FLAT_ID = "flatId";
	
	String FLAT = "flat";
	
	String USER = "user";

	String APARTMENT = "apartment";

	String AUTHENTICATION_SCHEME = "Bearer";

	String PAGINATION_PAGE_NO = "pageNo";

	String PAGINATION_RECORD_LIMIT = "limit";
	
	String STATUS = "status";

	Long DEFAULT_FLAT_ID = -1L;
}
