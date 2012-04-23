#include "include/paser.h"
#include "include/utils.h"

static jfieldID  statusList      	  = NULL;
static jfieldID  hasvisible           = NULL;
static jfieldID  previous_cursor      = NULL;
static jfieldID  next_cursor   	      = NULL;
static jfieldID  total_number         = NULL;

static jmethodID arraylist_add_method = NULL;
static jmethodID constructor          = NULL;

JNIEXPORT jobject JNICALL
Java_com_sina_weibosdk_parser_JsonParser_parserStatusList
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
	if(root == NULL)
	{
		(*env) -> ThrowNew(env, parser_exception, PARSER_STATUS_LIST_ERROR);
	}

	jobject obj = parser_status_list(env, parser_exception, root, 1);

	(*env) -> ReleaseStringUTFChars(env, jstr, json);

	throw_parse_exception(env, parser_exception, PARSER_STATUS_LIST_ERROR);

	(*env) -> DeleteLocalRef(env, parser_exception);

	return obj;

}

static void
init_fieldid(JNIEnv* env, jclass own)
{
	if(NULL == statusList)
	{
		statusList      = (*env) -> GetFieldID(env, own, "statusList", "Ljava/util/ArrayList;");
	}
	if(NULL == hasvisible)
	{
		hasvisible      = (*env) -> GetFieldID(env, own, "hasvisible", "Z");
	}
	if(NULL == previous_cursor)
	{
		previous_cursor = (*env) -> GetFieldID(env, own, "previous_cursor", "Ljava/lang/String;");
	}
	if(NULL == next_cursor)
	{
		next_cursor     = (*env) -> GetFieldID(env, own, "next_cursor", "Ljava/lang/String;");
	}
	if(NULL == total_number)
	{
		total_number    = (*env) -> GetFieldID(env, own, "total_number", "I");
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

extern jobject
parser_status_list(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}
	if(is_java_call)
	{
		errno = 0;
	}

	jclass status_list_class = NULL;
	jobject status_list_obj  = NULL;

	jclass arraylist_class = NULL;
	jobject arraylist_obj  = NULL;

	status_list_class = (*env) -> FindClass(env, CB_CLASS_STATUS_LIST);
	arraylist_class  = (*env) -> FindClass(env, "java/util/ArrayList");
	init_fieldid(env, status_list_class);
	init_methodid(env, status_list_class);

	status_list_obj = (*env) -> NewObject(env, status_list_class, constructor);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);

	(*env) -> DeleteLocalRef(env, status_list_class);

	arraylist_obj = (*env) -> GetObjectField(env, status_list_obj, statusList);

	if(NULL == arraylist_add_method)
	{
		arraylist_add_method = (*env) -> GetMethodID(env, arraylist_class, "add", "(Ljava/lang/Object;)Z");
	}
	(*env) -> DeleteLocalRef(env, arraylist_class);

	json_t* status = json_object_get(root, "statuses");

	if(status == NULL)
	{
		(*env) -> ThrowNew(env, exception, PARSER_STATUS_LIST_ERROR);
	}

	if(status != NULL)
	{
		int size = json_array_size(status);
		if(size > 0)
		{
			int i;
			for(i = 0; i < size; i++)
			{
				json_t* ele = json_array_get(status, i);
				jobject status_obj = parser_status(env, exception, ele, 0);
				if(NULL != status_obj)
				{
					(*env) -> CallBooleanMethod(env, arraylist_obj, arraylist_add_method, status_obj);
					throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);
					(*env) -> DeleteLocalRef(env, status_obj);
				}
			}
		}
	}
	(*env) -> DeleteLocalRef(env, arraylist_obj);

	unsigned char hasvisible_value  = parse_boolean(&root, "hasvisible");
	char* previous_cursor_value     = parse_text(&root, "previous_cursor");
	char* next_cursor_value         = parse_text(&root, "next_cursor");
	int total_number_value          = parse_integer(&root, "total_number");

	(*env) -> SetBooleanField(env, status_list_obj, hasvisible, hasvisible_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);

	jstring j_previous_cursor_value =
			(*env) -> NewStringUTF(env, previous_cursor_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);
	(*env) -> SetObjectField(env, status_list_obj, previous_cursor, j_previous_cursor_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);
	(*env) -> DeleteLocalRef(env, j_previous_cursor_value);

	jstring j_next_cursor_value =
				(*env) -> NewStringUTF(env, next_cursor_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);
	(*env) -> SetObjectField(env, status_list_obj, next_cursor, j_next_cursor_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);
	(*env) -> DeleteLocalRef(env, j_next_cursor_value);

	(*env) -> SetIntField(env, status_list_obj, total_number, total_number_value);
	throw_parse_exception(env, exception, PARSER_STATUS_LIST_ERROR);

	// clean
	if(is_java_call)
	{
		json_decref(root);
	}
	free_mem(previous_cursor_value);
	free_mem(next_cursor_value);

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_STATUS_LIST_ERROR);
	}

	return status_list_obj;

}
