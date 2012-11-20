/*
 * coms_Vm.cpp
 *
 *  Created on: 2012-11-20
 *      Author: guangongbo
 */

#include "coms_Vm.h"
#include <stdlib.h>

void Java_coms_Vm_fatalError(JNIEnv* env, jclass clazz, jstring str) {
    const char * msg;
    if (str == NULL) {
        msg = "";
    } else {
        msg = env->GetStringUTFChars(str, NULL);
    }
    env->FatalError(msg);
    if (str != NULL) {
        env->ReleaseStringUTFChars(str, msg);
    }
}

jint Java_coms_Vm_getVersion(JNIEnv* env, jclass clazz) {
    return env->GetVersion();
}

jlong Java_coms_Vm_fromReflectedField(JNIEnv* env, jclass clazz, jobject obj) {
    if (obj == NULL) {
        return -1;
    }
    return (jlong) env->FromReflectedField(obj);
}

jlong Java_coms_Vm_fromReflectedMethod(JNIEnv* env, jclass clazz, jobject obj) {
    if (obj == NULL) {
        return -1;
    }
    return (jlong) env->FromReflectedMethod(obj);
}

jobject Java_coms_Vm_toReflectedField(JNIEnv* env, jclass clazz,
        jclass target_clazz, jlong id, jboolean is_static) {
    if (target_clazz == NULL || id == 0) {
        return NULL;
    }

    return env->ToReflectedField(target_clazz, (jfieldID) id, is_static);
}

jobject Java_coms_Vm_toReflectedMethod(JNIEnv* env, jclass clazz,
        jclass target_clazz, jlong id, jboolean is_static) {
    if (target_clazz == NULL || id == 0) {
        return NULL;
    }

    return env->ToReflectedMethod(target_clazz, (jmethodID) id, is_static);
}

jlong Java_coms_Vm_getMethodId(JNIEnv* env, jclass clazz, jclass target_clazz,
        jstring name, jstring sig) {
    if (target_clazz == NULL || name == NULL || sig == NULL) {
        return -1;
    }

    const char * _name = env->GetStringUTFChars(name, NULL);
    const char * _sig = env->GetStringUTFChars(sig, NULL);
    const jlong res = (jlong) env->GetMethodID(target_clazz, _name, _sig);
    env->ReleaseStringUTFChars(name, _name);
    env->ReleaseStringUTFChars(sig, _sig);
    return res;
}

jlong Java_coms_Vm_getFieldId(JNIEnv* env, jclass clazz, jclass target_clazz,
        jstring name, jstring sig) {
    if (target_clazz == NULL || name == NULL || sig == NULL) {
        return -1;
    }

    const char * _name = env->GetStringUTFChars(name, NULL);
    const char * _sig = env->GetStringUTFChars(sig, NULL);
    const jlong res = (jlong) env->GetFieldID(target_clazz, _name, _sig);
    env->ReleaseStringUTFChars(name, _name);
    env->ReleaseStringUTFChars(sig, _sig);
    return res;
}

jlong Java_coms_Vm_getStaticMethodId(JNIEnv* env, jclass clazz,
        jclass target_clazz, jstring name, jstring sig) {
    if (target_clazz == NULL || name == NULL || sig == NULL) {
        return -1;
    }

    const char * _name = env->GetStringUTFChars(name, NULL);
    const char * _sig = env->GetStringUTFChars(sig, NULL);
    const jlong res = (jlong) env->GetStaticMethodID(target_clazz, _name, _sig);
    env->ReleaseStringUTFChars(name, _name);
    env->ReleaseStringUTFChars(sig, _sig);
    return res;
}

jlong Java_coms_Vm_getStaticFieldId(JNIEnv* env, jclass clazz,
        jclass target_clazz, jstring name, jstring sig) {
    if (target_clazz == NULL || name == NULL || sig == NULL) {
        return -1;
    }

    const char * _name = env->GetStringUTFChars(name, NULL);
    const char * _sig = env->GetStringUTFChars(sig, NULL);
    const jlong res = (jlong) env->GetStaticFieldID(target_clazz, _name, _sig);
    env->ReleaseStringUTFChars(name, _name);
    env->ReleaseStringUTFChars(sig, _sig);
    return res;
}
