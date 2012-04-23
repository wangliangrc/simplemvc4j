#include "include/paser.h"
#include "include/utils.h"

static jfieldID  type      = NULL;
static jfieldID  list_id   = NULL;

static jmethodID constructor    = NULL;


static void
init_fieldid(JNIEnv* env, jclass own)
{
	if(NULL == type)
	{
		type = (*env) -> GetFieldID(env, own, "type", "I");
	}
	if(NULL == list_id)
	{
		list_id = (*env) -> GetFieldID(env, own, "list_id", "I");
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
parser_visible(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}

//	LOGE("parser_visible");

	if(is_java_call)
	{
		errno = 0;
	}

	jclass visible_class = NULL;
	jobject visible_obj  = NULL;

	visible_class = (*env) -> FindClass(env, CB_CLASS_VISIBLE);
	init_fieldid(env, visible_class);
	init_methodid(env, visible_class);
	visible_obj   = (*env) -> NewObject(env, visible_class, constructor);
	throw_parse_exception(env, exception, PARSER_VISIBLE_ERROR);

	int type_value 		  		= parse_integer(&root, "type");
	int list_id_value 		= parse_integer(&root, "list_id");

	(*env) -> SetIntField(env, visible_obj, type, type_value);
	throw_parse_exception(env, exception, PARSER_VISIBLE_ERROR);
	(*env) -> SetIntField(env, visible_obj, list_id, list_id_value);
	throw_parse_exception(env, exception, PARSER_VISIBLE_ERROR);

	// clean
//	LOGE("visible clean");
	if(is_java_call)
	{
		json_decref(root);
	}

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_VISIBLE_ERROR);
	}

	return visible_obj;

}

