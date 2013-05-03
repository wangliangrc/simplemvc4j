/*
 * utils.h
 *
 *  Created on: 2013-5-3
 *      Author: guangongbo
 */

#ifndef UTILS_H_
#define UTILS_H_

#include <android/log.h>

#ifndef LOGE
#define LOGE(TAG, ...) ((void)__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__))
#endif 

#ifndef LOGD
#define LOGD(TAG, ...) ((void)__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__))
#endif 

#ifndef LOGW
#define LOGW(TAG, ...) ((void)__android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__))
#endif 

#ifndef LOGI
#define LOGI(TAG, ...) ((void)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__))
#endif 

#ifndef LOGV
#define LOGV(TAG, ...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__))
#endif 

#endif /* UTILS_H_ */
