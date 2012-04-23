#include "include/paser.h"
#include "include/utils.h"

static jfieldID  id      		   = NULL;
static jfieldID  screen_name       = NULL;
static jfieldID  gender            = NULL;
static jfieldID  profile_image_url = NULL;
static jfieldID  followers_count   = NULL;
static jfieldID  verified          = NULL;
static jfieldID  verified_type     = NULL;
static jfieldID  level             = NULL;
static jfieldID  province          = NULL;
static jfieldID  city              = NULL;
static jfieldID  location          = NULL;
static jfieldID  friends_count     = NULL;
static jfieldID  statuses_count    = NULL;
static jfieldID  favourites_count  = NULL;
static jfieldID  created_at        = NULL;
static jfieldID  status            = NULL;
static jfieldID  description       = NULL;
static jfieldID  name              = NULL;
static jfieldID  nickname          = NULL;
static jfieldID  domain            = NULL;
static jfieldID  following         = NULL;
static jfieldID  allow_all_act_msg = NULL;
static jfieldID  remark     	   = NULL;
static jfieldID  geo_enabled       = NULL;
static jfieldID  allow_all_comment = NULL;
static jfieldID  avatar_large      = NULL;
static jfieldID  verified_reason   = NULL;
static jfieldID  follow_me         = NULL;
static jfieldID  online_status     = NULL;
static jfieldID  status_id     	   = NULL;
static jfieldID  bi_followers_count= NULL;
static jfieldID  lang     		   = NULL;
static jfieldID	 distance   	   = NULL;
static jfieldID  gids     		   = NULL;

static jmethodID constructor   	   = NULL;
static jmethodID str_constructor    = NULL;
static jmethodID arraylist_add_method = NULL;

JNIEXPORT jobject JNICALL
Java_com_sina_weibosdk_parser_JsonParser_parserUserInfo
  	  (JNIEnv * env, jclass call, jstring jstr) {

	char* json = (char*)(*env) -> GetStringUTFChars(env, jstr, 0);
	if (NULL == json || strlen(json) == 0)
	{
		free_mem(json);
		return NULL;
	}

	jclass parser_exception = init_exc_class(env);

	json_t*       root    = NULL;
	json_error_t* error   = NULL;

	root = json_loads(json, 0, error);
	if(NULL == root)
	{
		(*env) -> ThrowNew(env, parser_exception, PARSER_USERINFO_ERROR);
	}

	jobject obj = parser_userinfo(env, parser_exception, root, 1);

	(*env) -> ReleaseStringUTFChars(env, jstr, json);

	throw_parse_exception(env, parser_exception, PARSER_USERINFO_ERROR);

	(*env) -> DeleteLocalRef(env, parser_exception);

	return obj;

}

static void
init_fieldid(JNIEnv* env, jclass own)
{
	if(NULL == id)
	{
		id = (*env) -> GetFieldID(env, own, "id", "Ljava/lang/String;");
	}
	if(NULL == screen_name)
	{
		screen_name = (*env) -> GetFieldID(env, own, "screen_name", "Ljava/lang/String;");
	}
	if(NULL == gender)
	{
		gender  = (*env) -> GetFieldID(env, own, "gender", "Ljava/lang/String;");
	}
	if(NULL == profile_image_url)
	{
		profile_image_url  = (*env) -> GetFieldID(env, own, "profile_image_url", "Ljava/lang/String;");
	}
	if(NULL == followers_count)
	{
		followers_count  = (*env) -> GetFieldID(env, own, "followers_count", "I");
	}
	if(NULL == verified)
	{
		verified = (*env) -> GetFieldID(env, own, "verified", "Z");
	}
	if(NULL == verified_type)
	{
		verified_type  = (*env) -> GetFieldID(env, own, "verified_type", "I");
	}
	if(NULL == level)
	{
		level  = (*env) -> GetFieldID(env, own, "level", "I");
	}
	if(NULL == province)
	{
		province  = (*env) -> GetFieldID(env, own, "province", "Ljava/lang/String;");
	}
	if(NULL == city)
	{
		city = (*env) -> GetFieldID(env, own, "city", "Ljava/lang/String;");
	}
	if(NULL == location)
	{
		location = (*env) -> GetFieldID(env, own, "location", "Ljava/lang/String;");
	}
	if(NULL == friends_count)
	{
		friends_count  = (*env) -> GetFieldID(env, own, "friends_count", "I");
	}
	if(NULL == statuses_count)
	{
		statuses_count = (*env) -> GetFieldID(env, own, "statuses_count", "I");
	}
	if(NULL == favourites_count)
	{
		favourites_count  = (*env) -> GetFieldID(env, own, "favourites_count", "I");
	}
	if(NULL == created_at)
	{
		created_at  = (*env) -> GetFieldID(env, own, "created_at", "Ljava/lang/String;");
	}
	if(NULL == status)
	{
		status  = (*env) -> GetFieldID(env, own, "status", "Lcom/sina/weibosdk/entity/Status;");
	}
	if(NULL == description)
	{
		description = (*env) -> GetFieldID(env, own, "description", "Ljava/lang/String;");
	}
	if(NULL == name)
	{
		name  = (*env) -> GetFieldID(env, own, "name", "Ljava/lang/String;");
	}
	if(NULL == nickname)
	{
		nickname  = (*env) -> GetFieldID(env, own, "nickname", "Ljava/lang/String;");
	}
	if(NULL == domain)
	{
		domain = (*env) -> GetFieldID(env, own, "domain", "Ljava/lang/String;");
	}
	if(NULL == following)
	{
		following = (*env) -> GetFieldID(env, own, "following", "Z");
	}
	if(NULL == allow_all_act_msg)
	{
		allow_all_act_msg = (*env) -> GetFieldID(env, own, "allow_all_act_msg", "Z");
	}
	if(NULL == remark)
	{
		remark = (*env) -> GetFieldID(env, own, "remark", "Ljava/lang/String;");
	}
	if(NULL == geo_enabled)
	{
		geo_enabled = (*env) -> GetFieldID(env, own, "geo_enabled", "Z");
	}
	if(NULL == allow_all_comment)
	{
		allow_all_comment = (*env) -> GetFieldID(env, own, "allow_all_comment", "Z");
	}
	if(NULL == avatar_large)
	{
		avatar_large  = (*env) -> GetFieldID(env, own, "avatar_large", "Ljava/lang/String;");
	}
	if(NULL == verified_reason)
	{
		verified_reason  = (*env) -> GetFieldID(env, own, "verified_reason", "Ljava/lang/String;");
	}
	if(NULL == follow_me)
	{
		follow_me  = (*env) -> GetFieldID(env, own, "follow_me", "Z");
	}
	if(NULL == online_status)
	{
		online_status  = (*env) -> GetFieldID(env, own, "online_status", "I");
	}
	if(NULL == status_id)
	{
		status_id = (*env) -> GetFieldID(env, own, "status_id", "Ljava/lang/String;");
	}
	if(NULL == bi_followers_count)
	{
		bi_followers_count  = (*env) -> GetFieldID(env, own, "bi_followers_count", "I");
	}
	if(NULL == lang)
	{
		lang = (*env) -> GetFieldID(env, own, "lang", "Ljava/lang/String;");
	}
	if(NULL == distance)
	{
		distance = (*env) -> GetFieldID(env, own, "distance", "I");
	}
	if(NULL == gids)
	{
		gids = (*env) -> GetFieldID(env, own, "gids", "Ljava/util/ArrayList;");
	}

}

static void
init_methodid(JNIEnv* env, jclass own)
{
	if(NULL == constructor)
	{
		constructor = (*env) -> GetMethodID(env, own, "<init>", "()V");
	}
}

static void
init_string_construct(JNIEnv* env, jclass own)
{
	if(NULL == str_constructor)
	{
		str_constructor = (*env) -> GetMethodID(env, own, "<init>", "([B)V");
	}
}

extern jobject
parser_userinfo(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}

	if(is_java_call)
	{
		errno = 0;
	}

	jclass str_class    = NULL;
	jclass userinfo_class = NULL;
	jobject userinfo_obj  = NULL;

	str_class = (*env) -> FindClass(env, "java/lang/String");
	userinfo_class = (*env) -> FindClass(env, CB_CLASS_USERINFO);

	init_fieldid(env, userinfo_class);
	init_methodid(env, userinfo_class);
	init_string_construct(env, str_class);

	userinfo_obj   = (*env) -> NewObject(env, userinfo_class, constructor);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	(*env) -> DeleteLocalRef(env, userinfo_class);

	char* id_value 		  				  = parse_text(&root, "id");
	char* screen_name_value 			  = parse_text(&root, "screen_name");
	char* gender_value 	  				  = parse_text(&root, "gender");
	char* profile_image_url_value 		  = parse_text(&root, "profile_image_url");
	int followers_count_value 			  = parse_integer(&root, "followers_count");
	unsigned char verified_value 		  = parse_boolean(&root, "verified");
	int verified_type_value 			  = parse_integer(&root, "verified_type");
	int level_value 					  = parse_integer(&root, "level");
	char* province_value 				  = parse_text(&root, "province");
	char* city_value 					  = parse_text(&root, "city");
	char* location_value 				  = parse_text(&root, "location");
	int friends_count_value 			  = parse_integer(&root, "friends_count");
	int statuses_count_value 			  = parse_integer(&root, "statuses_count");
	int favourites_count_value 			  = parse_integer(&root, "favourites_count");
	char* created_at_value 				  = parse_text(&root, "created_at");
	char* description_value 			  = parse_text(&root, "description");
	char* name_value 					  = parse_text(&root, "name");
	char* nickname_value 				  = parse_text(&root, "nickname");
	char* domain_value 					  = parse_text(&root, "domain");
	unsigned char following_value 		  = parse_boolean(&root, "following");
	unsigned char allow_all_act_msg_value = parse_boolean(&root, "allow_all_act_msg");
	char* remark_value 					  = parse_text(&root, "remark");
	unsigned char geo_enabled_value 	  = parse_boolean(&root, "geo_enabled");
	unsigned char allow_all_comment_value = parse_boolean(&root, "allow_all_comment");
	char* avatar_large_value 			  = parse_text(&root, "avatar_large");
	char* verified_reason_value   		  = parse_text(&root, "verified_reason");
	unsigned char follow_me_value 		  = parse_boolean(&root, "follow_me");
	int online_status_value      		  = parse_integer(&root, "online_status");
	char* status_id_value         		  = parse_text(&root, "status_id");
	int bi_followers_count_value 		  = parse_integer(&root, "bi_followers_count");
	char* lang_value 			 		  = parse_text(&root, "lang");
	int distance_value     	 			  = parse_integer(&root, "distance");
	char* gids_value					  = parse_text(&root, "gidstr");

	jstring j_id_value =
			(*env) -> NewStringUTF(env, id_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, id, j_id_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_id_value);

	jstring j_screen_name_value = new_string(env, str_class, str_constructor, screen_name_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, screen_name, j_screen_name_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_screen_name_value);

	jstring j_gender_value =
			(*env) -> NewStringUTF(env, gender_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, gender, j_gender_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_gender_value);

	jstring j_profile_image_url_value =
			(*env) -> NewStringUTF(env, profile_image_url_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, profile_image_url, j_profile_image_url_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_profile_image_url_value);

	(*env) -> SetIntField(env, userinfo_obj, followers_count, followers_count_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetBooleanField(env, userinfo_obj, verified, verified_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, verified_type, verified_type_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, level, level_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	jstring j_province_value = new_string(env, str_class, str_constructor, province_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, province, j_province_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_province_value);

	jstring j_city_value = new_string(env, str_class, str_constructor, city_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, city, j_city_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_city_value);

	jstring j_location_value = new_string(env, str_class, str_constructor, location_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, location, j_location_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_location_value);

	(*env) -> SetIntField(env, userinfo_obj, friends_count, friends_count_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, statuses_count, statuses_count_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, favourites_count, favourites_count_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	jstring j_created_at_value =
			(*env) -> NewStringUTF(env, created_at_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, created_at, j_created_at_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_created_at_value);


	json_t* status_value  = json_object_get(root, "status");
	jobject status_obj = NULL;
	if (status_value != NULL && is_string_empty(json_string_value(status_value)))
	{
		status_obj = parser_status(env, exception, status_value, 0);
	}
	(*env) -> SetObjectField(env, userinfo_obj, status, status_obj);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, status_obj);

	jstring j_description_value = new_string(env, str_class, str_constructor, description_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, description, j_description_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_description_value);

	jstring j_name_value = new_string(env, str_class, str_constructor, name_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, name, j_name_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_name_value);

	jstring j_nickname_value = new_string(env, str_class, str_constructor, nickname_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, nickname, j_nickname_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_nickname_value);

	jstring j_domain_value =
			(*env) -> NewStringUTF(env, domain_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, domain, j_domain_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_domain_value);

	(*env) -> SetBooleanField(env, userinfo_obj, following, following_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetBooleanField(env, userinfo_obj, allow_all_act_msg, allow_all_act_msg_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	jstring j_remark_value = new_string(env, str_class, str_constructor, remark_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, remark, j_remark_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_remark_value);

	(*env) -> SetBooleanField(env, userinfo_obj, geo_enabled, geo_enabled_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetBooleanField(env, userinfo_obj, allow_all_comment, allow_all_comment_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	jstring j_avatar_large_value =
			(*env) -> NewStringUTF(env, avatar_large_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, avatar_large, j_avatar_large_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_avatar_large_value);

	jstring j_verified_reason_value = new_string(env, str_class, str_constructor, verified_reason_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, verified_reason, j_verified_reason_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_verified_reason_value);

	jstring j_status_id_value =
			(*env) -> NewStringUTF(env, status_id_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, status_id, j_status_id_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_status_id_value);


	(*env) -> SetBooleanField(env, userinfo_obj, follow_me, follow_me_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, online_status, online_status_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetIntField(env, userinfo_obj, bi_followers_count, bi_followers_count_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);

	jstring j_lang_value = (*env) -> NewStringUTF(env, lang_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> SetObjectField(env, userinfo_obj, lang, j_lang_value);
	throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
	(*env) -> DeleteLocalRef(env, j_lang_value);

	(*env) -> SetIntField(env, userinfo_obj, distance, distance_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	/**
	 * 用户分组id,根据逗号分割字符串
	 */
	if(gids_value != NULL && strlen(gids_value) > 0)
	{
		jobject gidlist_obj = (*env) -> GetObjectField(env, userinfo_obj, gids);
		jclass arraylist_class = (*env) -> FindClass(env, "java/util/ArrayList");
		if(NULL == arraylist_add_method)
		{
			arraylist_add_method = (*env) -> GetMethodID(env, arraylist_class, "add", "(Ljava/lang/Object;)Z");
		}
		//分割
		char* gid = strtok(gids_value, ",");
		while(gid)
		{
			jstring j_gid_value = (*env) -> NewStringUTF(env, gid);
			throw_parse_exception(env, exception, PARSER_USERINFO_ERROR);
			(*env) -> CallBooleanMethod(env, gidlist_obj, arraylist_add_method, j_gid_value);
			(*env) -> DeleteLocalRef(env, j_gid_value);
			gid = strtok(NULL, ",");
		}
		(*env) -> DeleteLocalRef(env, arraylist_class);
		(*env) -> DeleteLocalRef(env, gidlist_obj);
		free_mem(gid);

	}

	// clean
	if(is_java_call)
	{
		json_decref(root);
	}

//	LOGE("userinfo clean");
	free_mem(id_value);
	free_mem(screen_name_value);
	free_mem(gender_value);
	free_mem(profile_image_url_value);
	free_mem(province_value);
	free_mem(city_value);
	free_mem(location_value);
	free_mem(created_at_value);
	free_mem(description_value);
	free_mem(name_value);
	free_mem(nickname_value);
	free_mem(domain_value);
	free_mem(remark_value);
	free_mem(avatar_large_value);
	free_mem(verified_reason_value);
	free_mem(lang_value);
	free_mem(status_id_value);
	free_mem(gids_value);

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_USERINFO_ERROR);
	}

	return userinfo_obj;

}
