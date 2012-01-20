/*
 * JNIHelper.cpp
 *
 *  Created on: 2012-1-9
 *      Author: clark
 */

#include "JNIHelper.h"
#include "global.h"
#include <cstdlib>
#include <cstring>
using namespace std;

static char internal_name[256];

#define _C_CHECKARGS__(a, b)    checkArgument((a), sprintf(internal_name, "%s\t[行:%d 文件:%s]", (b), __LINE__, __FILE__) > 0 ? internal_name : (b))
#define _C_CHECKNULL_(a, b)    checkNull((a), sprintf(internal_name, "%s\t[行:%d 文件:%s]", (b), __LINE__, __FILE__) > 0 ? internal_name : (b))
#define _C_CHECKSTATE_(a, b)    checkNull((a), sprintf(internal_name, "%s\t[行:%d 文件:%s]", (b), __LINE__, __FILE__) > 0 ? internal_name : (b))

namespace su {

JNIHelper::JNIHelper() {
    this->env = get_env();
}

JNIHelper::JNIHelper(JNIEnv *env) {
    this->env = env;
}

JNIHelper::JNIHelper(const JNIHelper& other) {
    env = other.env;
}

JNIHelper::~JNIHelper() {
    this->env = NULL;
}

JNIHelper& JNIHelper::operator=(const JNIHelper& other) {
    env = other.env;
    return *this;
}

JNIEnv* JNIHelper::getEnv() const {
    return env;
}

jint JNIHelper::throwException(const jthrowable throwable) const {
    if (throwable == NULL) {
        fatalError("jint JNIHelper::throwException(jthrowable throwable) const\n"
                "\tthrowable can't be NULL!");
        return -1;
    } else {
        return env->Throw(throwable);
    }
}

jint JNIHelper::throwNewException(const jclass clazz, const char *msg) const {
    if (clazz == NULL) {
        fatalError("jint JNIHelper::throwNewException(jclass clazz, const char *msg) const\n"
                "\tclazz can't be NULL!");
        return -1;
    } else {
        return env->ThrowNew(clazz, msg);
    }
}

jboolean JNIHelper::exceptionCheck() const {
    return env->ExceptionCheck();
}

void JNIHelper::exceptionClear() const {
    env->ExceptionClear();
}

void JNIHelper::exceptionDescribe() const {
    if (exceptionCheck()) {
        env->ExceptionDescribe();
    }
}

jthrowable JNIHelper::exceptionOccurred() const {
    return env->ExceptionOccurred();
}

void JNIHelper::fatalError(const char *msg) const {
    return env->FatalError(msg);
}

void JNIHelper::throwRuntime(const char *msg) const {
    jclass clazz = env->FindClass("java/lang/RuntimeException");
    if (clazz == NULL) {
        fatalError("void JNIHelper::throwRuntime(const char *msg) const\n"
                "\tCan't find java.lang.RuntimeException class!");
    } else {
        env->ThrowNew(clazz, msg);
    }
}

void JNIHelper::throwIllegalArgument(const char *msg) const {
    jclass clazz = env->FindClass("java/lang/IllegalArgumentException");
    if (clazz == NULL) {
        fatalError("void JNIHelper::throwIllegalArgument(const char *msg) const\n"
                "\tCan't find java.lang.IllegalArgumentException class!");
    } else {
        env->ThrowNew(clazz, msg);
    }
}

void JNIHelper::throwIllegalState(const char *msg) const {
    jclass clazz = env->FindClass("java/lang/IllegalStateException");
    if (clazz == NULL) {
        fatalError("void JNIHelper::throwIllegalState(const char *msg) const\n"
                "\tCan't find java.lang.IllegalStateException class!");
    } else {
        env->ThrowNew(clazz, msg);
    }
}

void JNIHelper::throwNullPointer(const char *msg) const {
    jclass clazz = env->FindClass("java/lang/NullPointerException");
    if (clazz == NULL) {
        fatalError("void JNIHelper::throwNullPointer(const char *msg) const\n"
                "\tCan't find java.lang.NullPointerException class!");
    } else {
        env->ThrowNew(clazz, msg);
    }
}

jboolean JNIHelper::checkNull(const void *p, const char *msg) const {
    if (p == NULL) {
        throwNullPointer(msg);
        exceptionDescribe();
    }

    return exceptionCheck();
}

//jboolean JNIHelper::checkNull1(const void *p, const char *msg) const {
//    if (p == NULL) {
//        throwNullPointer(msg);
//        exceptionDescribe();
//    }
//
//    return exceptionCheck();
//}

jboolean JNIHelper::checkArgument(const int& b, const char *msg) const {
    if (b == 0) {
        throwIllegalArgument(msg);
        exceptionDescribe();
    }

    return exceptionCheck();
}

jboolean JNIHelper::checkState(const int& b, const char *msg) const {
    if (b == 0) {
        throwIllegalState(msg);
        exceptionDescribe();
    }

    return exceptionCheck();
}

jclass JNIHelper::findClass(const char *classname) const {
    jclass clazz = env->FindClass(classname);
    strcpy(internal_name, "Class not found: ");
    strcat(internal_name, classname);
    if (_C_CHECKNULL_(clazz, internal_name)) {
        return NULL;
    }
    return clazz;
}

jclass JNIHelper::findClass(const jobject obj) const {
    if (_C_CHECKARGS__(obj != NULL, "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass clazz = env->GetObjectClass(obj);
    strcpy(internal_name, "Class not found [jobject]");
    if (_C_CHECKNULL_(clazz, internal_name)) {
        return NULL;
    }
    return clazz;
}

jmethodID JNIHelper::findConstructor(const jclass clazz, const char *signture) const {
    if (_C_CHECKARGS__(clazz != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return findMethod(clazz, "<init>", signture);
}

jmethodID JNIHelper::findMethod(const jclass clazz, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jmethodID methodId = env->GetMethodID(clazz, name, signture);
    strcpy(internal_name, "Method not found: ");
    strcat(internal_name, name);
    strcat(internal_name, " ");
    strcat(internal_name, signture);
    if (_C_CHECKNULL_(methodId, internal_name)) {
        return NULL;
    }
    return methodId;
}

jmethodID JNIHelper::findStaticMethod(const jclass clazz, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jmethodID methodId = env->GetStaticMethodID(clazz, name, signture);
    strcpy(internal_name, "Static Method not found: ");
    strcat(internal_name, name);
    strcat(internal_name, " ");
    strcat(internal_name, signture);
    if (_C_CHECKNULL_(methodId, internal_name)) {
        return NULL;
    }
    return methodId;
}

jmethodID JNIHelper::findConstructor(const char *className, const char *signture) const {
    return findConstructor(findClass(className), signture);
}

jmethodID JNIHelper::findMethod(const char *className, const char *name, const char *signture) const {
    return findMethod(findClass(className), name, signture);
}

jmethodID JNIHelper::findStaticMethod(const char *className, const char *name, const char *signture) const {
    return findStaticMethod(findClass(className), name, signture);
}

jfieldID JNIHelper::findField(const jclass clazz, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jfieldID fieldId = env->GetFieldID(clazz, name, signture);
    strcpy(internal_name, "Field not found: ");
    strcat(internal_name, name);
    strcat(internal_name, " ");
    strcat(internal_name, signture);
    if (_C_CHECKNULL_(fieldId, internal_name)) {
        return NULL;
    }
    return fieldId;
}

jfieldID JNIHelper::findStaticField(const jclass clazz, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jfieldID fieldId = env->GetStaticFieldID(clazz, name, signture);
    strcpy(internal_name, "Field not found: ");
    strcat(internal_name, name);
    strcat(internal_name, " ");
    strcat(internal_name, signture);
    if (_C_CHECKNULL_(fieldId, internal_name)) {
        return NULL;
    }
    return fieldId;
}

jfieldID JNIHelper::findField(const char *className, const char *name, const char *signture) const {
    return findField(findClass(className), name, signture);
}

jfieldID JNIHelper::findStaticField(const char *className, const char *name, const char *signture) const {
    return findStaticField(findClass(className), name, signture);
}

void JNIHelper::callVoidMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    va_list args;
    va_start(args, method);
    env->CallVoidMethodV(obj, method, args);
    va_end(args);
}

void JNIHelper::callNonvirtualVoidMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    va_list args;
    va_start(args, method);
    env->CallNonvirtualVoidMethodV(obj, findClass(obj), method, args);
    va_end(args);
}

void JNIHelper::callStaticVoidMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    va_list args;
    va_start(args, method);
    env->CallStaticVoidMethodV(clazz, method, args);
    va_end(args);
}

jobject JNIHelper::callObjectMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    va_list args;
    va_start(args, method);
    jobject res = env->CallObjectMethodV(obj, method, args);
    va_end(args);
    return res;
}

jobject JNIHelper::callNonvirtualObjectMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    va_list args;
    va_start(args, method);
    jobject res = env->CallNonvirtualObjectMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jobject JNIHelper::callStaticObjectMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    va_list args;
    va_start(args, method);
    jobject res = env->CallStaticObjectMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callBooleanMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jboolean res = env->CallBooleanMethodV(obj, method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callNonvirtualBooleanMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jboolean res = env->CallNonvirtualBooleanMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callStaticBooleanMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jboolean res = env->CallStaticBooleanMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callByteMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jbyte res = env->CallByteMethodV(obj, method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callNonvirtualByteMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jbyte res = env->CallNonvirtualByteMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callStaticByteMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jbyte res = env->CallStaticByteMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callCharMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jchar res = env->CallCharMethodV(obj, method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callNonvirtualCharMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jchar res = env->CallNonvirtualCharMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callStaticCharMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jchar res = env->CallStaticCharMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callShortMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jshort res = env->CallShortMethodV(obj, method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callNonvirtualShortMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jshort res = env->CallNonvirtualShortMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callStaticShortMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jshort res = env->CallStaticShortMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callIntMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jint res = env->CallIntMethodV(obj, method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callNonvirtualIntMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jint res = env->CallNonvirtualIntMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callStaticIntMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jint res = env->CallStaticIntMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callLongMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jlong res = env->CallLongMethodV(obj, method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callNonvirtualLongMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jlong res = env->CallNonvirtualLongMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callStaticLongMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    va_list args;
    va_start(args, method);
    jlong res = env->CallStaticLongMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callFloatMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jfloat res = env->CallFloatMethodV(obj, method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callNonvirtualFloatMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jfloat res = env->CallNonvirtualFloatMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callStaticFloatMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jfloat res = env->CallStaticFloatMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callDoubleMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jdouble res = env->CallDoubleMethodV(obj, method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callNonvirtualDoubleMethod(const jobject obj, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(obj != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jdouble res = env->CallNonvirtualDoubleMethodV(obj, findClass(obj), method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callStaticDoubleMethod(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    va_list args;
    va_start(args, method);
    jdouble res = env->CallStaticDoubleMethodV(clazz, method, args);
    va_end(args);
    return res;
}

void JNIHelper::callVoidMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return;
    }
    va_list args;
    va_start(args, signture);
    env->CallVoidMethodV(obj, method, args);
    va_end(args);
}

jobject JNIHelper::callObjectMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return NULL;
    }
    va_list args;
    va_start(args, signture);
    jobject res = env->CallObjectMethodV(obj, method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callBooleanMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jboolean res = env->CallBooleanMethodV(obj, method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callByteMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jbyte res = env->CallByteMethodV(obj, method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callCharMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jchar res = env->CallCharMethodV(obj, method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callShortMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jshort res = env->CallShortMethodV(obj, method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callIntMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jint res = env->CallIntMethodV(obj, method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callLongMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jlong res = env->CallLongMethodV(obj, method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callFloatMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jfloat res = env->CallFloatMethodV(obj, method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callDoubleMethod(const jobject obj, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jmethodID method = findMethod(findClass(obj), name, signture);
    if (method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jdouble res = env->CallDoubleMethodV(obj, method, args);
    va_end(args);
    return res;
}

void JNIHelper::callStaticVoidMethod(const jclass clazz, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return;
    }
    va_list args;
    va_start(args, signture);
    env->CallStaticVoidMethodV(clazz, method, args);
    va_end(args);
}

jobject JNIHelper::callStaticObjectMethod(const jclass clazz, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return NULL;
    }
    va_list args;
    va_start(args, signture);
    jobject res = env->CallStaticObjectMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callStaticBooleanMethod(const jclass clazz, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jboolean res = env->CallStaticBooleanMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callStaticByteMethod(const jclass clazz, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jbyte res = env->CallStaticByteMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callStaticCharMethod(const jclass clazz, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jchar res = env->CallStaticCharMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callStaticShortMethod(const jclass clazz, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jshort res = env->CallStaticShortMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callStaticIntMethod(const jclass clazz, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jint res = env->CallStaticIntMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callStaticLongMethod(const jclass clazz, const char *name, const char *signture, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jlong res = env->CallStaticLongMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callStaticFloatMethod(const jclass clazz, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jfloat res = env->CallStaticFloatMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callStaticDoubleMethod(const jclass clazz, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazz != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jdouble res = env->CallStaticDoubleMethodV(clazz, method, args);
    va_end(args);
    return res;
}

void JNIHelper::callStaticVoidMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return;
    }
    va_list args;
    va_start(args, signture);
    env->CallStaticVoidMethodV(clazz, method, args);
    va_end(args);
}

jobject JNIHelper::callStaticObjectMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return NULL;
    }
    va_list args;
    va_start(args, signture);
    jobject res = env->CallStaticObjectMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jboolean JNIHelper::callStaticBooleanMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jboolean res = env->CallStaticBooleanMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jbyte JNIHelper::callStaticByteMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jbyte res = env->CallStaticByteMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jchar JNIHelper::callStaticCharMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jchar res = env->CallStaticCharMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jshort JNIHelper::callStaticShortMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jshort res = env->CallStaticShortMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jint JNIHelper::callStaticIntMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jint res = env->CallStaticIntMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jlong JNIHelper::callStaticLongMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0;
    }
    va_list args;
    va_start(args, signture);
    jlong res = env->CallStaticLongMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jfloat JNIHelper::callStaticFloatMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jfloat res = env->CallStaticFloatMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jdouble JNIHelper::callStaticDoubleMethod(const char *clazzName, const char *name, const char *signture,
        ...) const {
    if (_C_CHECKARGS__(clazzName != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jclass clazz = findClass(clazzName);
    jmethodID method = findStaticMethod(clazz, name, signture);
    if (clazz == NULL || method == NULL) {
        return 0.;
    }
    va_list args;
    va_start(args, signture);
    jdouble res = env->CallStaticDoubleMethodV(clazz, method, args);
    va_end(args);
    return res;
}

jobject JNIHelper::newObject(const jclass clazz, const jmethodID method, ...) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    va_list args;
    va_start(args, method);
    jobject res = env->NewObjectV(clazz, method, args);
    va_end(args);
    return res;
}

jobject JNIHelper::newObject(const char *classname, const char *methodsignture, ...) const {
    if (_C_CHECKARGS__(classname != NULL && methodsignture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass clazz = findClass(classname);
    jmethodID constructor = findConstructor(clazz, methodsignture);
    if (clazz == NULL || constructor == NULL) {
        return NULL;
    }
    va_list args;
    va_start(args, methodsignture);
    jobject res = env->NewObjectV(clazz, constructor, args);
    va_end(args);
    return res;
}

jobjectArray JNIHelper::newObjectArray(const jsize& length, const jclass elementType,
        const jobject initObj) const {
    if (_C_CHECKARGS__(length > 0 && elementType != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->NewObjectArray(length, elementType, initObj);
}

jobjectArray JNIHelper::newObjectArray(const jsize& length, const char *classname,
        const jobject initObj) const {
    if (_C_CHECKARGS__(length > 0 && classname != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass clazz = findClass(classname);
    if (clazz == NULL) {
        return NULL;
    }
    return env->NewObjectArray(length, clazz, initObj);
}

jsize JNIHelper::getArrayLength(const jarray array) const {
    if (_C_CHECKARGS__(array != NULL, "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetArrayLength(array);
}

void JNIHelper::setObjectArrayElement(const jobjectArray array, const jsize& index,
        const jobject value) const {
    if (_C_CHECKARGS__(array != NULL, "Arguments can't be NULL!")) {
        return;
    }
    if (_C_CHECKARGS__(index >= 0 && index < getArrayLength(array),
            "Index must be in the range of array!")) {
        return;
    }

    env->SetObjectArrayElement(array, index, value);
}

jobject JNIHelper::getObjectArrayElement(const jobjectArray array, const jsize& index) const {
    if (_C_CHECKARGS__(array != NULL, "Arguments can't be NULL!")) {
        return NULL;
    }
    if (_C_CHECKARGS__(index >= 0 && index < getArrayLength(array),
            "Index must be in the range of array!")) {
        return NULL;
    }

    return env->GetObjectArrayElement(array, index);
}

jobject JNIHelper::getObjectField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->GetObjectField(obj, field);
}

jboolean JNIHelper::getBooleanField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetBooleanField(obj, field);
}

jbyte JNIHelper::getByteField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetByteField(obj, field);
}

jchar JNIHelper::getCharField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetCharField(obj, field);
}

jshort JNIHelper::getShortField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetShortField(obj, field);
}

jint JNIHelper::getIntField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetIntField(obj, field);
}

jlong JNIHelper::getLongField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetLongField(obj, field);
}

jfloat JNIHelper::getFloatField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    return env->GetFloatField(obj, field);
}

jdouble JNIHelper::getDoubleField(const jobject obj, const jfieldID field) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    return env->GetDoubleField(obj, field);
}

jobject JNIHelper::getObjectField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return NULL;
    }
    return env->GetObjectField(obj, field);
}

jboolean JNIHelper::getBooleanField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetBooleanField(obj, field);
}

jbyte JNIHelper::getByteField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetByteField(obj, field);
}

jchar JNIHelper::getCharField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetCharField(obj, field);
}

jshort JNIHelper::getShortField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetShortField(obj, field);
}

jint JNIHelper::getIntField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetIntField(obj, field);
}

jlong JNIHelper::getLongField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0;
    }
    return env->GetLongField(obj, field);
}

jfloat JNIHelper::getFloatField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0.;
    }
    return env->GetFloatField(obj, field);
}

jdouble JNIHelper::getDoubleField(const jobject obj, const char *name, const char *signture) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return 0.;
    }
    return env->GetDoubleField(obj, field);
}

jobject JNIHelper::getStaticObjectField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->GetStaticObjectField(clazz, field);
}

jboolean JNIHelper::getStaticBooleanField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticBooleanField(clazz, field);
}

jbyte JNIHelper::getStaticByteField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticByteField(clazz, field);
}

jchar JNIHelper::getStaticCharField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticCharField(clazz, field);
}

jshort JNIHelper::getStaticShortField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticShortField(clazz, field);
}

jint JNIHelper::getStaticIntField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticIntField(clazz, field);
}

jlong JNIHelper::getStaticLongField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStaticLongField(clazz, field);
}

jfloat JNIHelper::getStaticFloatField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    return env->GetStaticFloatField(clazz, field);
}

jdouble JNIHelper::getStaticDoubleField(const jclass clazz, const jfieldID field) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    return env->GetStaticDoubleField(clazz, field);
}

jobject JNIHelper::getStaticObjectField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return NULL;
    }
    return env->GetStaticObjectField(clazz, field);
}

jboolean JNIHelper::getStaticBooleanField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticBooleanField(clazz, field);
}

jbyte JNIHelper::getStaticByteField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticByteField(clazz, field);
}

jchar JNIHelper::getStaticCharField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticCharField(clazz, field);
}

jshort JNIHelper::getStaticShortField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticShortField(clazz, field);
}

jint JNIHelper::getStaticIntField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticIntField(clazz, field);
}

jlong JNIHelper::getStaticLongField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0;
    }
    return env->GetStaticLongField(clazz, field);
}

jfloat JNIHelper::getStaticFloatField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0.;
    }
    return env->GetStaticFloatField(clazz, field);
}

jdouble JNIHelper::getStaticDoubleField(const char *classname, const char *fieldname,
        const char *fieldsignture) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return 0.;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return 0.;
    }
    return env->GetStaticDoubleField(clazz, field);
}

void JNIHelper::setObjectField(const jobject obj, const jfieldID field, const jobject value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetObjectField(obj, field, value);
}

void JNIHelper::setBooleanField(const jobject obj, const jfieldID field, const jboolean& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetBooleanField(obj, field, value);
}

void JNIHelper::setByteField(const jobject obj, const jfieldID field, const jbyte& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetByteField(obj, field, value);
}

void JNIHelper::setCharField(const jobject obj, const jfieldID field, const jchar& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetCharField(obj, field, value);
}

void JNIHelper::setShortField(const jobject obj, const jfieldID field, const jshort& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetShortField(obj, field, value);
}

void JNIHelper::setIntField(const jobject obj, const jfieldID field, const jint& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetIntField(obj, field, value);
}

void JNIHelper::setLongField(const jobject obj, const jfieldID field, const jlong& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetLongField(obj, field, value);
}

void JNIHelper::setFloatField(const jobject obj, const jfieldID field, const jfloat& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetFloatField(obj, field, value);
}

void JNIHelper::setDoubleField(const jobject obj, const jfieldID field, const jdouble& value) const {
    if (_C_CHECKARGS__(obj != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetDoubleField(obj, field, value);
}

void JNIHelper::setObjectField(const jobject obj, const char *name, const char *signture,
        const jobject value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetObjectField(obj, field, value);
}

void JNIHelper::setBooleanField(const jobject obj, const char *name, const char *signture,
        const jboolean& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetBooleanField(obj, field, value);
}

void JNIHelper::setByteField(const jobject obj, const char *name, const char *signture,
        const jbyte& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetByteField(obj, field, value);
}

void JNIHelper::setCharField(const jobject obj, const char *name, const char *signture,
        const jchar& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetCharField(obj, field, value);
}

void JNIHelper::setShortField(const jobject obj, const char *name, const char *signture,
        const jshort& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetShortField(obj, field, value);
}

void JNIHelper::setIntField(const jobject obj, const char *name, const char *signture,
        const jint& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetIntField(obj, field, value);
}

void JNIHelper::setLongField(const jobject obj, const char *name, const char *signture,
        const jlong& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetLongField(obj, field, value);
}

void JNIHelper::setFloatField(const jobject obj, const char *name, const char *signture,
        const jfloat& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetFloatField(obj, field, value);
}

void JNIHelper::setDoubleField(const jobject obj, const char *name, const char *signture,
        const jdouble& value) const {
    if (_C_CHECKARGS__(obj != NULL && name != NULL && signture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jfieldID field = findField(findClass(obj), name, signture);
    if (field == NULL) {
        return;
    }
    env->SetDoubleField(obj, field, value);
}

void JNIHelper::setStaticObjectField(const jclass clazz, const jfieldID field, const jobject value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticObjectField(clazz, field, value);
}

void JNIHelper::setStaticBooleanField(const jclass clazz, const jfieldID field, const jboolean& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticBooleanField(clazz, field, value);
}

void JNIHelper::setStaticByteField(const jclass clazz, const jfieldID field, const jbyte& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticByteField(clazz, field, value);
}

void JNIHelper::setStaticCharField(const jclass clazz, const jfieldID field, const jchar& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticCharField(clazz, field, value);
}

void JNIHelper::setStaticShortField(const jclass clazz, const jfieldID field, const jshort& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticShortField(clazz, field, value);
}

void JNIHelper::setStaticIntField(const jclass clazz, const jfieldID field, const jint& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticIntField(clazz, field, value);
}

void JNIHelper::setStaticLongField(const jclass clazz, const jfieldID field, const jlong& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticLongField(clazz, field, value);
}

void JNIHelper::setStaticFloatField(const jclass clazz, const jfieldID field, const jfloat& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticFloatField(clazz, field, value);
}

void JNIHelper::setStaticDoubleField(const jclass clazz, const jfieldID field, const jdouble& value) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    env->SetStaticDoubleField(clazz, field, value);
}

void JNIHelper::setStaticObjectField(const char *classname, const char *fieldname, const char *fieldsignture,
        jobject value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticObjectField(clazz, field, value);
}

void JNIHelper::setStaticBooleanField(const char *classname, const char *fieldname, const char *fieldsignture,
        jboolean value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticBooleanField(clazz, field, value);
}

void JNIHelper::setStaticByteField(const char *classname, const char *fieldname, const char *fieldsignture,
        jbyte value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticByteField(clazz, field, value);
}

void JNIHelper::setStaticCharField(const char *classname, const char *fieldname, const char *fieldsignture,
        jchar value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticCharField(clazz, field, value);
}

void JNIHelper::setStaticShortField(const char *classname, const char *fieldname, const char *fieldsignture,
        jshort value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticShortField(clazz, field, value);
}

void JNIHelper::setStaticIntField(const char *classname, const char *fieldname, const char *fieldsignture,
        jint value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticIntField(clazz, field, value);
}

void JNIHelper::setStaticLongField(const char *classname, const char *fieldname, const char *fieldsignture,
        jlong value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticLongField(clazz, field, value);
}

void JNIHelper::setStaticFloatField(const char *classname, const char *fieldname, const char *fieldsignture,
        jfloat value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticFloatField(clazz, field, value);
}

void JNIHelper::setStaticDoubleField(const char *classname, const char *fieldname, const char *fieldsignture,
        jdouble value) const {
    if (_C_CHECKARGS__(
            classname != NULL && fieldname != NULL && fieldsignture != NULL,
            "Arguments can't be NULL!")) {
        return;
    }
    jclass clazz = findClass(classname);
    jfieldID field = findStaticField(clazz, fieldname, fieldsignture);
    if (clazz == NULL || field == NULL) {
        return;
    }
    env->SetStaticDoubleField(clazz, field, value);
}

jstring JNIHelper::newStringUTF(const char *s) const {
    if (s != NULL) {
        jstring str = env->NewStringUTF(s);
        return intern(str);
    } else {
        return NULL;
    }
}

jstring JNIHelper::newString(const jchar *s, const jsize len) const {
    if (s != NULL && len > 0) {
        return env->NewString(s, len);
    } else {
        return NULL;
    }
}

const char* JNIHelper::getStringUTFChars(const jstring s) const {
    if (s != NULL) {
        jboolean b = 1;
        return env->GetStringUTFChars(s, &b);
    } else {
        return NULL;
    }
}

const jchar* JNIHelper::getStringChars(const jstring s) const {
    if (s != NULL) {
        jboolean b = 1;
        return env->GetStringChars(s, &b);
    } else {
        return NULL;
    }
}

void JNIHelper::releaseStringChars(const jstring s, const jchar *buf) const {
    if (_C_CHECKARGS__(s != NULL && buf != NULL, "Arguments can't be NULL!")) {
        return;
    }
    env->ReleaseStringChars(s, buf);
}

void JNIHelper::releaseStringUTFChars(const jstring s, const char *buf) const {
    if (_C_CHECKARGS__(s != NULL && buf != NULL, "Arguments can't be NULL!")) {
        return;
    }
    env->ReleaseStringUTFChars(s, buf);
}

jsize JNIHelper::getStringLength(const jstring s) const {
    if (_C_CHECKARGS__(s != NULL, "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStringLength(s);
}

jsize JNIHelper::getStringUTFLength(const jstring s) const {
    if (_C_CHECKARGS__(s != NULL, "Arguments can't be NULL!")) {
        return 0;
    }
    return env->GetStringUTFLength(s);
}

jfieldID JNIHelper::fromReflectedField(const jobject field) const {
    if (_C_CHECKARGS__(field != NULL, "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->FromReflectedField(field);
}

jmethodID JNIHelper::fromReflectedMethod(const jobject method) const {
    if (_C_CHECKARGS__(method != NULL, "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->FromReflectedMethod(method);
}

jobject JNIHelper::toReflectedField(const jclass clazz, const jfieldID field,
        const jboolean& isStatic) const {
    if (_C_CHECKARGS__(clazz != NULL && field != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->ToReflectedField(clazz, field, isStatic);
}

jobject JNIHelper::toReflectedField(const char *clazz, const char *fieldName, const char *fieldSignture,
        const jboolean& isStatic) const {
    if (_C_CHECKARGS__(
            clazz != NULL && fieldName != NULL && fieldSignture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass c = findClass(clazz);
    jfieldID f = NULL;
    if (isStatic) {
        f = findStaticField(clazz, fieldName, fieldSignture);
    } else {
        f = findField(clazz, fieldName, fieldSignture);
    }
    if (c == NULL || f == NULL) {
        return NULL;
    }
    return env->ToReflectedField(c, f, isStatic);
}

jobject JNIHelper::toReflectedMethod(const jclass clazz, const jmethodID method,
        const jboolean& isStatic) const {
    if (_C_CHECKARGS__(clazz != NULL && method != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->ToReflectedMethod(clazz, method, isStatic);
}

jobject JNIHelper::toReflectedMethod(const char *clazz, const char *methodName, const char *methodSignture,
        const jboolean& isStatic) const {
    if (_C_CHECKARGS__(
            clazz != NULL && methodName != NULL && methodSignture != NULL,
            "Arguments can't be NULL!")) {
        return NULL;
    }
    jclass c = findClass(clazz);
    jmethodID m = NULL;
    if (isStatic) {
        m = findStaticMethod(clazz, methodName, methodSignture);
    } else {
        m = findMethod(clazz, methodName, methodSignture);
    }
    if (c == NULL || m == NULL) {
        return NULL;
    }
    return env->ToReflectedMethod(c, m, isStatic);
}

jclass JNIHelper::getSuperclass(const jclass clazz) const {
    if (_C_CHECKARGS__(clazz != NULL, "Arguments can't be NULL!")) {
        return NULL;
    }
    return env->GetSuperclass(clazz);
}

jboolean JNIHelper::isAssignableFrom(const jclass clazz1, const jclass clazz2) const {
    if (_C_CHECKARGS__(clazz1 != NULL && clazz2 != NULL,
            "Arguments can't be NULL!")) {
        return JNI_FALSE;
    }
    return env->IsAssignableFrom(clazz1, clazz2);
}

jboolean JNIHelper::isInstanceOf(const jobject obj, const jclass clazz) const {
    if (_C_CHECKARGS__(obj != NULL && clazz != NULL,
            "Arguments can't be NULL!")) {
        return JNI_FALSE;
    }
    return env->IsInstanceOf(obj, clazz);
}

jboolean JNIHelper::isSameObject(const jobject obj1, const jobject obj2) const {
    if (_C_CHECKARGS__(obj1 != NULL && obj2 != NULL,
            "Arguments can't be NULL!")) {
        return JNI_FALSE;
    }
    return env->IsSameObject(obj1, obj2);
}

jint JNIHelper::lock(const jobject obj) const {
    if (_C_CHECKARGS__(obj != NULL, "Arguments can't be NULL!")) {
        return -1;
    }
    return env->MonitorEnter(obj);
}

jint JNIHelper::unlock(const jobject obj) const {
    if (_C_CHECKARGS__(obj != NULL, "Arguments can't be NULL!")) {
        return -1;
    }
    return env->MonitorExit(obj);
}

const JNIHelper& JNIHelper::print(const char *s) const {
    jobject out = getStaticObjectField("java/lang/System", "out", "Ljava/io/PrintStream;");
    callVoidMethod(out, "print", "(Ljava/lang/String;)V", newStringUTF(s));
    return *this;
}

const JNIHelper& JNIHelper::print(jobject obj) const {
    jobject out = getStaticObjectField("java/lang/System", "out", "Ljava/io/PrintStream;");
    callVoidMethod(out, "print", "(Ljava/lang/Object;)V", obj);
    return *this;
}

const JNIHelper& JNIHelper::println(const char *s) const {
    jobject out = getStaticObjectField("java/lang/System", "out", "Ljava/io/PrintStream;");
    callVoidMethod(out, "println", "(Ljava/lang/String;)V", newStringUTF(s));
    return *this;
}

const JNIHelper& JNIHelper::println(jobject obj) const {
    jobject out = getStaticObjectField("java/lang/System", "out", "Ljava/io/PrintStream;");
    callVoidMethod(out, "println", "(Ljava/lang/Object;)V", obj);
    return *this;
}

const JNIHelper& JNIHelper::println() const {
    jobject out = getStaticObjectField("java/lang/System", "out", "Ljava/io/PrintStream;");
    callVoidMethod(out, "println", "()V");
    return *this;
}

const JNIHelper& JNIHelper::error(const char *s) const {
    jobject err = getStaticObjectField("java/lang/System", "err", "Ljava/io/PrintStream;");
    callVoidMethod(err, "print", "(Ljava/lang/String;)V", newStringUTF(s));
    return *this;
}

const JNIHelper& JNIHelper::error(jobject obj) const {
    jobject err = getStaticObjectField("java/lang/System", "err", "Ljava/io/PrintStream;");
    callVoidMethod(err, "print", "(Ljava/lang/Object;)V", obj);
    return *this;
}

const JNIHelper& JNIHelper::errorln(const char *s) const {
    jobject err = getStaticObjectField("java/lang/System", "err", "Ljava/io/PrintStream;");
    callVoidMethod(err, "println", "(Ljava/lang/String;)V", newStringUTF(s));
    return *this;
}

const JNIHelper& JNIHelper::errorln(jobject obj) const {
    jobject err = getStaticObjectField("java/lang/System", "err", "Ljava/io/PrintStream;");
    callVoidMethod(err, "println", "(Ljava/lang/Object;)V", obj);
    return *this;
}

const JNIHelper& JNIHelper::errorln() const {
    jobject err = getStaticObjectField("java/lang/System", "err", "Ljava/io/PrintStream;");
    callVoidMethod(err, "println", "()V");
    return *this;
}

jstring JNIHelper::toString(jobject obj) const {
    if (obj == NULL) {
        return newStringUTF("NULL");
    } else {
        return (jstring) callObjectMethod(obj, "toString", "()Ljava/lang/String;");
    }
}

jstring JNIHelper::intern(jstring s) const {
    if (s != NULL) {
        return (jstring) callObjectMethod(s, "intern", "()Ljava/lang/String;");
    } else {
        return (jstring) callObjectMethod(env->NewStringUTF("NULL"), "intern", "()Ljava/lang/String;");
    }
}

//jobject JNIHelper::newStringBuilder() const {
//    return newObject("java/lang/StringBuilder", "()V");
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jstring s) const {
//    return callObjectMethod(stringbuilder, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", s);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, const char *s) const {
//    return appendStringBuilder(stringbuilder, newStringUTF(s));
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jobject o) const {
//    return callObjectMethod(stringbuilder, "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", o);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jbyte b) const {
//    return callObjectMethod(stringbuilder, "append", "(B)Ljava/lang/StringBuilder;", b);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jboolean b) const {
//    return callObjectMethod(stringbuilder, "append", "(Z)Ljava/lang/StringBuilder;", b);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jchar ch) const {
//    return callObjectMethod(stringbuilder, "append", "(C)Ljava/lang/StringBuilder;", ch);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jcharArray chs) const {
//    return callObjectMethod(stringbuilder, "append", "([C)Ljava/lang/StringBuilder;", chs);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jdouble d) const {
//    return callObjectMethod(stringbuilder, "append", "(D)Ljava/lang/StringBuilder;", d);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jfloat f) const {
//    return callObjectMethod(stringbuilder, "append", "(F)Ljava/lang/StringBuilder;", f);
//}
//
//jobject JNIHelper::appendStringBuilder(jobject stringbuilder, jint i) const {
//    return callObjectMethod(stringbuilder, "append", "(I)Ljava/lang/StringBuilder;", i);
//}

}// namespace su

