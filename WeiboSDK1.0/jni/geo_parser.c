
#include "include/paser.h"
#include "include/utils.h"

static jfieldID  type           = NULL;
static jfieldID  coordinates    = NULL;

static jmethodID constructor    = NULL;

JNIEXPORT jobject JNICALL
Java_com_sina_weibosdk_parser_JsonParser_parserGeo
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
		(*env) -> ThrowNew(env, parser_exception, PARSER_GEO_ERROR);
	}

	jobject obj = parser_geo(env, parser_exception, root, 1);

	(*env) -> ReleaseStringUTFChars(env, jstr, json);

	throw_parse_exception(env, parser_exception, PARSER_GEO_ERROR);

	(*env) -> DeleteLocalRef(env, parser_exception);

	return obj;

}

extern jclass
init_exc_class(JNIEnv* env)
{
	return (*env) -> FindClass(env, PARSER_EXCEPTION_CLASS);
}

static void
init_fieldid(JNIEnv* env, jclass own)
{
	if(NULL == type)
	{
		type = (*env) -> GetFieldID(env, own, "type", "Ljava/lang/String;");
	}
	if(NULL == coordinates)
	{
		coordinates = (*env) -> GetFieldID(env, own, "coordinates", "[D");
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
parser_geo(JNIEnv* env, jclass exception, json_t* root, int is_java_call)
{
	if(NULL == root)
	{
		return NULL;
	}

	if(is_java_call)
	{
		errno = 0;
	}

	jclass geo_class    = NULL;
	jobject geo_obj     = NULL;

	geo_class = (*env) -> FindClass(env, CB_CLASS_GEO);
	init_fieldid(env, geo_class);
	init_methodid(env, geo_class);
	geo_obj   = (*env) -> NewObject(env, geo_class, constructor);
	throw_parse_exception(env, exception, PARSER_GEO_ERROR);

	(*env) -> DeleteLocalRef(env, geo_class);

	char* type_value = parse_text(&root, "type");
	jdoubleArray coordinates_value = NULL;

	json_t* array = json_object_get(root, "coordinates");
	if(array != NULL)
	{
		int size = json_array_size(array);
		if(size > 0)
		{
			jdouble buf[size];
			coordinates_value = (*env) -> NewDoubleArray(env, size);
			throw_parse_exception(env, exception, PARSER_GEO_ERROR);
			int i;
			for(i = 0; i < size; i++)
			{
				json_t* ele = json_array_get(array, i);
				buf[i] = json_real_value(ele);
			}
			(*env) -> SetDoubleArrayRegion(env, coordinates_value, 0, size, buf);
			throw_parse_exception(env, exception, PARSER_GEO_ERROR);
		}
	}

	if(coordinates_value != NULL)
	{
		(*env) -> DeleteLocalRef(env, coordinates_value);
	}

	jstring j_type_value = (*env) -> NewStringUTF(env, type_value);
	throw_parse_exception(env, exception, PARSER_GEO_ERROR);
	(*env) -> SetObjectField(env, geo_obj, type, j_type_value);
	throw_parse_exception(env, exception, PARSER_GEO_ERROR);
	(*env) -> DeleteLocalRef(env, j_type_value);

	(*env) -> SetObjectField(env, geo_obj, coordinates,
			coordinates_value);
	throw_parse_exception(env, exception, PARSER_GEO_ERROR);

	// clean
//	LOGE("geo clean");
	if(is_java_call)
	{
		json_decref(root);
	}
	free_mem(type_value);

	if(errno != 0)
	{
		(*env) -> ThrowNew(env, exception, PARSER_GEO_ERROR);
	}

	return geo_obj;

}
