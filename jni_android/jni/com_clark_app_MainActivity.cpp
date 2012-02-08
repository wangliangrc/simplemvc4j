#include "com_clark_app_MainActivity.h"
#include "jni_stdout.h"
#include "jni_utils.h"
#include "logger.h"

using clark::androids::logger;

namespace {
    const char *TAG = "com_clark_app_MainActivity";
}

jobject Java_com_clark_app_MainActivity_test(JNIEnv *env, jobject obj) {
    logger::open();
    logger::d(TAG, "debug!!!");
    logger::close();
    logger::e(TAG, "error!!!");
    return 0;
}

