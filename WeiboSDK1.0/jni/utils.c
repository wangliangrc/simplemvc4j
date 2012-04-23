#include "include/utils.h"

extern char*
parse_text(json_t** element, char* key)
{
	json_t* node = NULL;
	node = json_object_get(*element, key);

	if(NULL != node)
	{
		if(json_is_string(node))
		{
			const char* text = json_string_value(node);
			char* ret_text = (char*)malloc((strlen(text) + 1) * sizeof(char));
			strcpy(ret_text, text);
			return ret_text;
		}
		else if(json_is_integer(node))
		{
			long long value = json_integer_value(node);
			return to_string(value);
		}
		else if(json_is_boolean(node))
		{
			char* bool_text = (char*)malloc(sizeof(char) * 6);
			memset(bool_text, '\0', 6);
			if(json_is_true(node))
			{
				strcpy(bool_text, "true");
				return bool_text;
			}
			else
			{
				strcpy(bool_text, "false");
				return bool_text;
			}
		}
		else if(json_is_real(node))
		{
			double value = json_real_value(node);
			return to_string(value);
		}
	}
	return NULL;
}

extern long long
parse_integer(json_t** element, char* key)
{
	json_t*   node = NULL;
	long long number;

	node = json_object_get(*element, key);
	if(NULL != node)
	{
		if(json_is_integer(node))
		{
			number = json_integer_value(node);
		}
	}
	return number;
}

extern unsigned char
parse_boolean(json_t** element, char* key)
{
	json_t*   node = NULL;
	unsigned char bool = 0;
	node = json_object_get(*element, key);
	if(NULL != node)
	{
		if(json_is_integer(node))
		{
//			LOGE("json_is_integer");
			bool = json_integer_value(node);
			return bool;
		}
		else if(json_is_boolean(node))
		{
//			LOGE("json_is_boolean");
			if(json_is_true(node))
			{
//				LOGE("json_is_boolean : true");
				return 1;
			}
			else
			{
//				LOGE("json_is_boolean : false");
				return 0;
			}
		}
		else if(json_is_string(node))
		{
//			LOGE("json_is_string");
			const char* temp = json_string_value(node);
			if(strcmp(temp, "true"))
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	return bool;
}

extern double
parse_real(json_t** element, char* key)
{
	json_t*   node = NULL;
	double real;

	node = json_object_get(*element, key);
	if(NULL != node)
	{
		if(json_is_integer(node))
		{
			real = json_integer_value(node);
			return real;
		}
		else if(json_is_real(node))
		{
			real = json_real_value(node);
			return real;
		}
	}
	return real;
}

extern char*
to_string(long long digit)
{

	int str_len = MAX_NUMBER_LEN;
	char* s = (char*)malloc(sizeof(char) * str_len);

	long long sign;
	int i = 0, j = 0;

	if((sign = digit) < 0)
	{
		digit = - digit;
	}

	do{
		if(i >= str_len - 1)
		{
			str_len = (int)(str_len * INC_SPEED + 1);
			s = realloc(s, str_len);
		}
		s[i++] = digit % 10 + '0';

	} while ((digit /= 10) > 0);

	if(sign < 0)
	{
		s[i++]='-';
	}

	s[i]='\0';

	char* temp = (char*)malloc(sizeof(char) * i);
	memset(temp, '\0', i);
	for(j = 0; j < i ; j++)
	{
		temp[j] = s[i - j - 1];
	}
	memcpy(s, temp, i);
	free_mem(temp);

	return s;
}

extern void
free_mem(void* p)
{
	if(NULL != p)
	{
		free(p);
	}
}

extern void
throw_parse_exception(JNIEnv * env, jclass exec, const char* err_msg)
{
	if((*env) -> ExceptionCheck(env))
	{
		(*env) -> ExceptionClear(env);
		(*env) -> ThrowNew(env, exec, err_msg);
	}
}

extern jstring
new_string(JNIEnv * env, jclass str_cls, jmethodID construct, const char* str)
{
	if(str == NULL || construct == NULL)
	{
		return NULL;
	}

	int len;
	len = strlen(str);
	if(len == 0)
	{
		return NULL;
	}

	jstring ret = NULL;
	jbyteArray bytes = NULL;

	bytes = (*env) -> NewByteArray(env, len);
	if(bytes != NULL)
	{
		(*env) -> SetByteArrayRegion(env, bytes, 0, len, str);
		ret = (*env) -> NewObject(env, str_cls, construct, bytes);
	}

	(*env) -> DeleteLocalRef(env, bytes);

	return ret;
}

extern int
is_string_empty(const char* str)
{
	if (NULL == str)
	{
		return 1;
	}
	else if (strlen(str) == 0)
	{
		return 1;
	}
	else
	{
		return 0;
	}
}
