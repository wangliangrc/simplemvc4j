#ifndef UTIL_H_
#define UTIL_H_

#include <stdio.h>
#include <stdlib.h>
#include "paser.h"

// 定义Digit数值的最大长度
#define MAX_NUMBER_LEN	20
// 定义Real数值的最大长度
#define MAX_REAL_LEN 50
#define INC_SPEED 1.5

extern char*
parse_text(json_t**, char*);

extern unsigned char
parse_boolean(json_t**, char*);

extern long long
parse_integer(json_t**, char*);

extern double
parse_real(json_t**, char*);

extern void
free_mem(void*);

extern char*
to_string(long long);

// 抛异常工具
extern void
throw_parse_exception(JNIEnv * env, jclass exec, const char* err_msg);

extern jstring
new_string(JNIEnv *, jclass, jmethodID, const char*);

extern int
is_string_empty(const char*);

#endif /* UTIL_H_ */
