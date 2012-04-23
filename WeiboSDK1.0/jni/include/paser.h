
#ifndef PASER_H_
#define PASER_H_

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <android/log.h>
#include <errno.h>
#include <jni.h>

#include "com_sina_weibosdk_parser_JsonParser.h"
#include "../jsonlib/jansson.h"

#define LOG_TAG	"native_parser"
#define LOGE(text)	__android_log_write(ANDROID_LOG_ERROR, LOG_TAG, text)
#define LOGD(text)	__android_log_write(ANDROID_LOG_DEBUG, LOG_TAG, text)

#define PARSER_EXCEPTION_CLASS "com/sina/weibosdk/exception/WeiboParseException"

#define CB_CLASS_USERINFO 	   "com/sina/weibosdk/entity/UserInfo"
#define CB_CLASS_STATUS   	   "com/sina/weibosdk/entity/Status"
#define CB_CLASS_STATUS_LIST   "com/sina/weibosdk/entity/StatusList"
#define CB_CLASS_VISIBLE  	   "com/sina/weibosdk/entity/Visible"
#define CB_CLASS_GEO  		   "com/sina/weibosdk/entity/Geo"
#define CB_CLASS_MESSAGE 	   "com/sina/weibosdk/entity/Message"
#define CB_CLASS_MESSAGE_LIST  "com/sina/weibosdk/entity/MessageList"

/**
 * 定义解析错误Message
 */
#define PARSER_GEO_ERROR  		   "native parse geo json error !"
#define PARSER_MESSAGE_ERROR  	   "native parse message json error !"
#define PARSER_MESSAGE_LIST_ERROR  "native parse message list json error !"
#define PARSER_STATUS_ERROR  	   "native parse status json error !"
#define PARSER_STATUS_LIST_ERROR   "native parse status list json error !"
#define PARSER_USERINFO_ERROR  	   "native parse userinfo json error !"
#define PARSER_VISIBLE_ERROR  	   "native parse visible json error !"


extern jobject
parser_userinfo(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_status(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_status_list(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_geo(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_visible(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_message(JNIEnv*, jclass, json_t*, int);

extern jobject
parser_message_list(JNIEnv*, jclass, json_t*, int);

extern jclass
init_exc_class();

static void
init_fieldid(JNIEnv*, jclass own);

static void
init_methodid(JNIEnv*, jclass own);

static void
init_string_construct(JNIEnv*, jclass own);

#endif /* PASER_H_ */
