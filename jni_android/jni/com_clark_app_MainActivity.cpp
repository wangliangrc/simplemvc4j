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

    clark::jnis::jni_output out = clark::jnis::jni_output::std_out(env);
    int i = 0;
    jobject temp = 0;
    while (true) {
        ++i;
        out << i << clark::jnis::endl;
        temp = clark::jnis::newObject(env);
        env->DeleteLocalRef(temp);
    }
    return 0;
}

