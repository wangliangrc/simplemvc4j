#include "include/paser.h"
#include "include/utils.h"

static jfieldID  created_at      = NULL;
static jfieldID  id      = NULL;
static jfieldID  mid        = NULL;
static jfieldID  idstr   	= NULL;
static jfieldID  text        = NULL;
static jfieldID  source          		= NULL;
static jfieldID  favorited          = NULL;
static jfieldID  truncated         = NULL;
static jfieldID  in_reply_to_status_id     = NULL;
static jfieldID  in_reply_to_user_id          = NULL;
static jfieldID  in_reply_to_screen_name   = NULL;
static jfieldID  thumbnail_pic    = NULL;
static jfieldID  bmiddle_pic   = NULL;
static jfieldID  original_pic     = NULL;
static jfieldID  geo     = NULL;
static jfieldID  user     = NULL;
static jfieldID  retweeted_status     = NULL;
static jfieldID  reposts_count     = NULL;
static jfieldID  comments_count     = NULL;
static jfieldID  mlevel     = NULL;
static jfieldID	 distance   = NULL;
static jfieldID  visible     = NULL;

static jmethodID constructor    = NULL;
static jmethodID str_constructor    = NULL;

JNIEXPORT jobject JNICALL
Java_com_sina_weibosdk_parser_JsonParser_parserStatus
  	  (JNIEnv * env, jclass call, jstring jstr) {

	char* json = (char*)(*env) -> GetStringUTFChars(env, jstr, 0);
	if (NULL == json || strlen(json) == 0)
	{
		free_mem(json);
		return NULL;
	}

	jclass parser_exception = init_exc_class(env);

	json_t*       root  = NULL;
	json_error_t* error = NULL;

	root = json_loads(json, 0, error);

	if(NULL == root)
	{
		(*env) -> ThrowNew(env, parser_exception, PARSER_STATUS_ERROR);
	}

	jobject obj = parser_status(env, parser_exception, root, 1);

	(*env) -> ReleaseStringUTFChars(env, jstr, json);

	throw_parse_exception(env, parser_exception, PARSER_STATUS_ERROR);

	(*env) -> DeleteLocalRef(env, parser_exception);

	return obj;

}

static void
init_fieldid(JNIEnv* env, jclass own)
{
	if(NULL == created_at)
	{
		created_at = (*env) -> GetFieldID(env, own, "created_at", "Ljava/lang/String;");
	}
	if(NULL == id)
	{
		id = (*env) -> GetFieldID(env, own, "id", "Ljava/lang/String;");
	}
	if(NULL == mid)
	{
		mid = (*env) -> GetFieldID(env, own, "mid", "Ljava/lang/String;");
	}
	if(NULL == idstr)
	{
		idstr = (*env) -> GetFieldID(env, own, "idstr", "Ljava/lang/String;");
	}
	if(NULL == text)
	{
		text = (*env) -> GetFieldID(env, own, "text", "Ljava/lang/String;");
	}
	if(NULL == source)
	{
		source = (*env) -> GetFieldID(env, own, "source", "Ljava/lang/String;");
	}
	if(NULL == favorited)
	{
		favorited = (*env) -> GetFieldID(env, own, "favorited", "Z");
	}
	if(NULL == truncated)
	{
		truncated = (*env) -> GetFieldID(env, own, "truncated", "Z");
	}
	if(NULL == in_reply_to_status_id)
	{
		in_reply_to_status_id = (*env) -> GetFieldID(env, own, "in_reply_to_status_id", "Ljava/lang/String;");
	}
	if(NULL == in_reply_to_user_id)
	{
		in_reply_to_user_id = (*env) -> GetFieldID(env, own, "in_reply_to_user_id", "Ljava/lang/String;");
	}
	if(NULL == in_reply_to_screen_name)
	{
		in_reply_to_screen_name = (*env) -> GetFieldID(env, own, "in_reply_to_screen_name", "Ljava/lang/String;");
	}
	if(NULL == thumbnail_pic)
	{
		thumbnail_pic = (*env) -> GetFieldID(env, own, "thumbnail_pic", "Ljava/lang/String;");
	}
	if(NULL == bmiddle_pic)
	{
		bmiddle_pic = (*env) -> GetFieldID(env, own, "bmiddle_pic", "Ljava/lang/String;");
	}
	if(NULL == original_pic)
	{
		original_pic = (*env) -> GetFieldID(env, own, "original_pic", "Ljava/lang/String;");
	}
	if(NULL == geo)
	{
		geo = (*env) -> GetFieldID(env, own, "geo", "Lcom/sina/weibosdk/entity/Geo;");
	}
	if(NULL == user)
	{
		user = (*env) -> GetFieldID(env, own, "user", "Lcom/sina/weibosdk/entity/UserInfo;");
	}
	if(NULL == retweeted_status)
	{
		retweeted_status = (*env) -> GetFieldID(env, own, "retweeted_status", "Lcom/sina/weibosdk/entity/Status;");
	}
	if(NULL == reposts_count)
	{
		reposts_count = (*env) -> GetFieldID(env, own, "reposts_count", "I");
	}
	if(NULL == comments_count)
	{
		comments_count = (*env) -> GetFieldID(env, own, "comments_count", "I");
	}
	if(NULL == mlevel)
	{
		mlevel = (*env) -> GetFieldID(env, own, "mlevel", "I");
	}
	if(NULL == distance)
	{
		distance = (*env) -> GetFieldID(env, own, "distance", "I");
	}
	if(NULL == visible)
	{
		visible = (*env) -> GetFieldID(env, own, "visible", "Lcom/sina/weibosdk/entity/Visible;");
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
parser_status(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}
//	LOGE("parser_status");
	if(is_java_call)
	{
		errno = 0;
	}

	jclass str_class    = NULL;
	jclass status_class = NULL;
	jobject status_obj  = NULL;

	str_class = (*env) -> FindClass(env, "java/lang/String");
	status_class = (*env) -> FindClass(env, CB_CLASS_STATUS);

	init_fieldid(env, status_class);
	init_methodid(env, status_class);
	init_string_construct(env, str_class);

	status_obj   = (*env) -> NewObject(env, status_class, constructor);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> DeleteLocalRef(env, status_class);

	char* created_at_value      		= parse_text(&root, "created_at");
	char* id_value      				= parse_text(&root, "id");
	char* mid_value        				= parse_text(&root, "mid");
	char* idstr_value   				= parse_text(&root, "idstr");
	char* text_value        			= parse_text(&root, "text");
	char* source_value          		= parse_text(&root, "source");
	unsigned char favorited_value       = parse_boolean(&root, "favorited");
	unsigned char truncated_value       = parse_boolean(&root, "truncated");
	char* in_reply_to_status_id_value   = parse_text(&root, "in_reply_to_status_id");
	char* in_reply_to_user_id_value     = parse_text(&root, "in_reply_to_user_id");
	char* in_reply_to_screen_name_value = parse_text(&root, "in_reply_to_screen_name");
	char* thumbnail_pic_value    		= parse_text(&root, "thumbnail_pic");
	char* bmiddle_pic_value   			= parse_text(&root, "bmiddle_pic");
	char* original_pic_value     		= parse_text(&root, "original_pic");


	json_t* geo_json_obj = json_object_get(root, "geo");
	jobject geo_obj = NULL;
	if (geo_json_obj != NULL && is_string_empty(json_string_value(geo_json_obj)))
	{
		geo_obj = parser_geo(env, exception, geo_json_obj, 0);
	}

	json_t* user_json_obj = json_object_get(root, "user");
	jobject user_obj = NULL;
	if (user_json_obj != NULL && is_string_empty(json_string_value(user_json_obj)))
	{
		user_obj = parser_userinfo(env, exception, user_json_obj, 0);
	}

	json_t* retweeted_status_json_obj = json_object_get(root, "retweeted_status");
	jobject retweeted_status_obj = NULL;
	if (retweeted_status_json_obj != NULL
			&& is_string_empty(json_string_value(retweeted_status_json_obj)))
	{
		retweeted_status_obj = parser_status(env, exception, retweeted_status_json_obj, 0);
	}


	int reposts_count_value  = parse_integer(&root, "reposts_count");
	int comments_count_value = parse_integer(&root, "comments_count");
	int mlevel_value         = parse_integer(&root, "mlevel");
	int distance_value     	 = parse_integer(&root, "distance");

	jobject visible_obj = parser_visible(env, exception,
			json_object_get(root, "visible"), 0);

	jstring j_created_at_value =
			(*env) -> NewStringUTF(env, created_at_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, created_at, j_created_at_value);
	(*env) -> DeleteLocalRef(env, j_created_at_value);

	jstring j_id_value =
			(*env) -> NewStringUTF(env, id_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, id, j_id_value);
	(*env) -> DeleteLocalRef(env, j_id_value);

	jstring j_mid_value =
			(*env) -> NewStringUTF(env, mid_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, mid, j_mid_value);
	(*env) -> DeleteLocalRef(env, j_mid_value);

	jstring j_idstr_value =
			(*env) -> NewStringUTF(env, idstr_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, idstr, j_idstr_value);
	(*env) -> DeleteLocalRef(env, j_idstr_value);

	jstring j_text_value = new_string(env, str_class, str_constructor, text_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, text, j_text_value);
	(*env) -> DeleteLocalRef(env, j_text_value);

	jstring j_source_value = new_string(env, str_class, str_constructor, source_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, source, j_source_value);
	(*env) -> DeleteLocalRef(env, j_source_value);

	(*env) -> SetBooleanField(env, status_obj, favorited, favorited_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> SetBooleanField(env, status_obj, truncated, truncated_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	jstring j_in_reply_to_status_id_value =
			(*env) -> NewStringUTF(env, in_reply_to_status_id_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, in_reply_to_status_id, j_in_reply_to_status_id_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> DeleteLocalRef(env, j_in_reply_to_status_id_value);

	jstring j_in_reply_to_user_id_value =
			(*env) -> NewStringUTF(env, in_reply_to_user_id_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, in_reply_to_user_id, j_in_reply_to_user_id_value);
	(*env) -> DeleteLocalRef(env, j_in_reply_to_user_id_value);

	jstring j_in_reply_to_screen_name_value = new_string(env, str_class, str_constructor, in_reply_to_screen_name_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, in_reply_to_screen_name, j_in_reply_to_screen_name_value);
	(*env) -> DeleteLocalRef(env, j_in_reply_to_screen_name_value);

	jstring j_thumbnail_pic_value =
			(*env) -> NewStringUTF(env, thumbnail_pic_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, thumbnail_pic, j_thumbnail_pic_value);
	(*env) -> DeleteLocalRef(env, j_thumbnail_pic_value);

	jstring j_bmiddle_pic_value =
			(*env) -> NewStringUTF(env, bmiddle_pic_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, bmiddle_pic, j_bmiddle_pic_value);
	(*env) -> DeleteLocalRef(env, j_bmiddle_pic_value);

	jstring j_original_pic_value =
			(*env) -> NewStringUTF(env, original_pic_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> SetObjectField(env, status_obj, original_pic, j_original_pic_value);
	(*env) -> DeleteLocalRef(env, j_original_pic_value);

	(*env) -> SetObjectField(env, status_obj, geo, geo_obj);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> DeleteLocalRef(env, geo_obj);

	(*env) -> SetObjectField(env, status_obj, user, user_obj);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> DeleteLocalRef(env, user_obj);

	(*env) -> SetObjectField(env, status_obj, retweeted_status, retweeted_status_obj);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> DeleteLocalRef(env, retweeted_status_obj);

	(*env) -> SetIntField(env, status_obj, reposts_count, reposts_count_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> SetIntField(env, status_obj, comments_count, comments_count_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> SetIntField(env, status_obj, mlevel, mlevel_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> SetIntField(env, status_obj, distance, distance_value);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);

	(*env) -> SetObjectField(env, status_obj, visible, visible_obj);
	throw_parse_exception(env, exception, PARSER_STATUS_ERROR);
	(*env) -> DeleteLocalRef(env, visible_obj);

//	LOGE("status clean");
	// clean
	if(is_java_call)
	{
		json_decref(root);
	}

	free_mem(created_at_value);
	free_mem(id_value);
	free_mem(mid_value);
	free_mem(idstr_value);
	free_mem(text_value);
	free_mem(source_value);
	free_mem(in_reply_to_status_id_value);
	free_mem(in_reply_to_user_id_value);
	free_mem(in_reply_to_screen_name_value);
	free_mem(thumbnail_pic_value);
	free_mem(bmiddle_pic_value);
	free_mem(original_pic_value);

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_STATUS_ERROR);
	}

	return status_obj;

}
