#include "com_clark_app_MainActivity.h"
#include "jni_stdout.h"
#include "jni_utils.h"
#include "logger.h"

using clark::androids::logger;

jobject Java_com_clark_app_MainActivity_test(JNIEnv *env, jobject obj) {
    clark::jnis::jni_output out = clark::jnis::jni_output::std_out(env);
    clark::jnis::jni_output err = clark::jnis::jni_output::std_err(env);
    out << "Hello world!" << clark::jnis::endl;
    err << "Hello world!" << clark::jnis::endl;
    return 0;
}

