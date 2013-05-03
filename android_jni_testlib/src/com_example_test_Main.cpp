/*
 * com_example_test_Main.cpp
 *
 *  Created on: 2013-5-3
 *      Author: guangongbo
 */

#include "com_example_test_Main.h"
#include <stdlib.h>

extern int _g_main(int argc, char **argv);

void Java_com_example_test_Main_main(JNIEnv* env, jclass clazz,
        jobjectArray args) {
    const jsize len = env->GetArrayLength(args);
    char **argv;
    argv = (char**) malloc(sizeof(char*) * len);
    if (argv == NULL) {
        return;
    }
    jstring tmp;
    for (jsize i = 0; i < len; ++i) {
        tmp = (jstring) env->GetObjectArrayElement(args, i);
        if (tmp == NULL) {
            argv[i] = NULL;
            continue;
        }
        argv[i] = (char*) env->GetStringUTFChars(tmp, NULL);
        env->DeleteLocalRef(tmp);
    }
    _g_main(len, argv);
    for (jsize i = 0; i < len; ++i) {
        tmp = (jstring) env->GetObjectArrayElement(args, i);
        if (tmp == NULL) {
            continue;
        }
        env->ReleaseStringUTFChars(tmp, argv[i]);
        env->DeleteLocalRef(tmp);
    }
    free(argv);
}
