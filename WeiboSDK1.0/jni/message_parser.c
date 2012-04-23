#include "include/paser.h"
#include "include/utils.h"

static jfieldID  created_at      = NULL;
static jfieldID  id      = NULL;
static jfieldID  idstr   	= NULL;
static jfieldID  text        = NULL;
static jfieldID  sender_id      = NULL;
static jfieldID  recipient_id      = NULL;
static jfieldID  sender_screen_name      = NULL;
static jfieldID  recipient_screen_name      = NULL;
static jfieldID  mid      = NULL;
static jfieldID  status_id      = NULL;
static jfieldID  att_ids      = NULL;
static jfieldID  geo      = NULL;
static jfieldID  sender      = NULL;
static jfieldID  recipient      = NULL;

static jmethodID constructor    = NULL;
static jmethodID str_constructor    = NULL;

static jmethodID arraylist_constructor = NULL;
static jmethodID arraylist_add_method = NULL;

JNIEXPORT jobject JNICALL
Java_com_sina_weibosdk_parser_JsonParser_parserMessage
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
		(*env) -> ThrowNew(env, parser_exception, PARSER_MESSAGE_ERROR);
	}

	jobject obj = parser_message(env, parser_exception, root, 1);

	(*env) -> ReleaseStringUTFChars(env, jstr, json);

	throw_parse_exception(env, parser_exception, PARSER_MESSAGE_ERROR);

	(*env) -> DeleteLocalRef(env, parser_exception);

	return obj;

}

static void
init_fieldid(JNIEnv* env, jclass own)
{

	if(NULL == created_at)
	{
		created_at = (*env) -> GetFieldID(env, own, "create_at", "Ljava/lang/String;");
	}
	if(NULL == id)
	{
		id = (*env) -> GetFieldID(env, own, "id", "Ljava/lang/String;");
	}
	if(NULL == idstr)
	{
		idstr = (*env) -> GetFieldID(env, own, "idstr", "Ljava/lang/String;");
	}
	if(NULL == text)
	{
		text = (*env) -> GetFieldID(env, own, "text", "Ljava/lang/String;");
	}
	if(NULL == sender_id)
	{
		sender_id = (*env) -> GetFieldID(env, own, "sender_id", "Ljava/lang/String;");
	}
	if(NULL == recipient_id)
	{
		recipient_id = (*env) -> GetFieldID(env, own, "recipient_id", "Ljava/lang/String;");
	}
	if(NULL == sender_screen_name)
	{
		sender_screen_name = (*env) -> GetFieldID(env, own, "sender_screen_name", "Ljava/lang/String;");
	}
	if(NULL == recipient_screen_name)
	{
		recipient_screen_name = (*env) -> GetFieldID(env, own, "recipient_screen_name", "Ljava/lang/String;");
	}
	if(NULL == mid)
	{
		mid = (*env) -> GetFieldID(env, own, "mid", "Ljava/lang/String;");
	}
	if(NULL == status_id)
	{
		status_id = (*env) -> GetFieldID(env, own, "status_id", "Ljava/lang/String;");
	}
	if(NULL == att_ids)
	{
		att_ids = (*env) -> GetFieldID(env, own, "att_ids", "Ljava/util/ArrayList;");
	}
	if(NULL == geo)
	{
		geo = (*env) -> GetFieldID(env, own, "geo", "Lcom/sina/weibosdk/entity/Geo;");
	}
	if(NULL == sender)
	{
		sender = (*env) -> GetFieldID(env, own, "sender", "Lcom/sina/weibosdk/entity/UserInfo;");
	}
	if(NULL == recipient)
	{
		recipient = (*env) -> GetFieldID(env, own, "recipient", "Lcom/sina/weibosdk/entity/UserInfo;");
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
parser_message(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}

//	LOGE("parser_message");

	if(is_java_call)
	{
		errno = 0;
	}

	jclass str_class     = NULL;
	jclass message_class = NULL;
	jobject message_obj  = NULL;

	str_class = (*env) -> FindClass(env, "java/lang/String");
	message_class = (*env) -> FindClass(env, CB_CLASS_MESSAGE);

	init_fieldid(env, message_class);
	init_methodid(env, message_class);
	init_string_construct(env, str_class);

	message_obj   = (*env) -> NewObject(env, message_class, constructor);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);

	(*env) -> DeleteLocalRef(env, message_class);

	char* created_at_value      		= parse_text(&root, "created_at");
	char* id_value      				= parse_text(&root, "id");
	char* mid_value        				= parse_text(&root, "mid");
	char* idstr_value   				= parse_text(&root, "idstr");
	char* text_value        			= parse_text(&root, "text");
	char* sender_screen_name_value      = parse_text(&root, "sender_screen_name");
	char* recipient_screen_name_value   = parse_text(&root, "recipient_screen_name");
	char* status_id_value  				= parse_text(&root, "status_id");
	char* sender_id_value			    = parse_text(&root, "sender_id");
	char* recipient_id_value 			= parse_text(&root, "recipient_id");


	json_t* geo_json_obj = json_object_get(root, "geo");
	jobject geo_obj = NULL;
	if (geo_json_obj != NULL && is_string_empty(json_string_value(geo_json_obj)))
	{
		geo_obj = parser_geo(env, exception, geo_json_obj, 0);
	}

	json_t* sender_json_obj = json_object_get(root, "sender");
	jobject sender_obj = NULL;
	if (sender_json_obj != NULL && is_string_empty(json_string_value(sender_json_obj)))
	{
		sender_obj = parser_userinfo(env, exception, sender_json_obj, 0);
	}

	json_t* recipient_json_obj = json_object_get(root, "recipient");
	jobject recipient_obj = NULL;
	if (recipient_json_obj != NULL && is_string_empty(json_string_value(recipient_json_obj)))
	{
		jobject recipient_obj = parser_userinfo(env, exception, recipient_json_obj, 0);
	}

	jclass    arraylist_class = NULL;
	jobject   arraylist_obj  = NULL;

	arraylist_class       = (*env) -> FindClass(env, "java/util/ArrayList");
	if(arraylist_constructor == NULL)
	{
		arraylist_constructor = (*env) -> GetMethodID(env, arraylist_class, "<init>", "()V");
	}
	arraylist_obj = (*env) -> NewObject(env, arraylist_class, arraylist_constructor);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);

	if(NULL == arraylist_add_method)
	{
		arraylist_add_method = (*env) -> GetMethodID(env, arraylist_class, "add", "(Ljava/lang/Object;)Z");
	}
	(*env) -> DeleteLocalRef(env, arraylist_class);

	json_t* attid_arr = json_object_get(root, "att_ids");

	if(attid_arr != NULL)
	{
		int size = json_array_size(attid_arr);
		if(size > 0)
		{
			int i;
			for(i = 0; i < size; i++)
			{
				json_t* ele = json_array_get(attid_arr, i);
				const char* attid_str = json_string_value(ele);

				if(NULL != attid_str)
				{
					jstring j_attid_str = (*env) -> NewStringUTF(env, attid_str);
					throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
					(*env) -> CallObjectMethod(env, arraylist_obj, arraylist_add_method, j_attid_str);
					throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
					(*env) -> DeleteLocalRef(env, j_attid_str);
				}
			}
		}
	}

	jstring j_created_at_value =
			(*env) -> NewStringUTF(env, created_at_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, created_at, j_created_at_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_created_at_value);

	jstring j_id_value =
			(*env) -> NewStringUTF(env, id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, id, j_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_id_value);

	jstring j_mid_value =
			(*env) -> NewStringUTF(env, mid_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, mid, j_mid_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_mid_value);

	jstring j_idstr_value =
			(*env) -> NewStringUTF(env, idstr_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, idstr, j_idstr_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_idstr_value);

	jstring j_text_value = new_string(env, str_class, str_constructor, text_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, text, j_text_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_text_value);

	jstring j_sender_screen_name_value = new_string(env, str_class, str_constructor, sender_screen_name_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, sender_screen_name, j_sender_screen_name_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_sender_screen_name_value);

	jstring j_recipient_screen_name_value = new_string(env, str_class, str_constructor, recipient_screen_name_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, recipient_screen_name, j_recipient_screen_name_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_recipient_screen_name_value);

	(*env) -> SetObjectField(env, message_obj, geo, geo_obj);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, geo_obj);

	(*env) -> SetObjectField(env, message_obj, sender, sender_obj);
	(*env) -> DeleteLocalRef(env, sender_obj);

	(*env) -> SetObjectField(env, message_obj, recipient, recipient_obj);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, recipient_obj);

	(*env) -> SetObjectField(env, message_obj, att_ids, arraylist_obj);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, arraylist_obj);

	jstring j_status_id_value =
			(*env) -> NewStringUTF(env, status_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, status_id, j_status_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_status_id_value);

	jstring j_sender_id_value =
			(*env) -> NewStringUTF(env, sender_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, sender_id, j_sender_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_sender_id_value);

	jstring j_recipient_id_value =
			(*env) -> NewStringUTF(env, recipient_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> SetObjectField(env, message_obj, recipient_id, j_recipient_id_value);
	throw_parse_exception(env, exception, PARSER_MESSAGE_ERROR);
	(*env) -> DeleteLocalRef(env, j_recipient_id_value);

//	LOGE("message clean");

	(*env) -> DeleteLocalRef(env, str_class);

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
	free_mem(sender_screen_name_value);
	free_mem(recipient_screen_name_value);
	free_mem(status_id_value);
	free_mem(sender_id_value);
	free_mem(recipient_id_value);

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_MESSAGE_ERROR);
	}

	return message_obj;

}
