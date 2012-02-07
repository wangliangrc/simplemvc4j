/*
 * jni_utils.cc
 *
 *  Created on: 2012-1-31
 *      Author: Clark
 */
//#define DEBUG
#include "jni_utils.h"
#include "string_utils.h"
using clark::strings::trim;
using clark::strings::split;

namespace {
    std::string replaceDot(const std::string & signture) {
        std::string res = signture;
        for (std::string::iterator i = res.begin(), j = res.end(); i != j;
                ++i) {
            if (*i == '.' && *(i + 1) != '.') {
                *i = '/';
            }
        }
        return res;
    }

    int getArraysWrapCount(const std::string & signture) {
        int count = 0;
        for (std::string::const_iterator i = signture.begin(), j =
                signture.end(); i != j; ++i) {
            if (*i == '[' && *(i + 1) == ']') {
                count++;
            } else if (*i == '.' && *(i + 1) == '.' && *(i + 2) == '.') {
                count++;
            }
        }

        return count;
    }

    std::string trimArraysSig(const std::string & signture) {
        std::string res = trim(signture);
        int index = res.find("...");
        if (index > 0) {
            res = res.substr(0, index);
        }

        index = res.find("[]");
        if (index > 0) {
            res = res.substr(0, index);
        }
        return trim(res);
    }

    std::string trimGenericSig(const std::string & signture) {
        std::string res = trim(signture);
        int index = res.find("<");
        if (index > 0) {
            res = res.substr(0, index);
        }
        return trim(res);
    }
}

std::string clark::jnis::toJNIFieldDesc(const std::string & signture) {
    int arraysCount = getArraysWrapCount(signture);
    std::string res = trimGenericSig(trimArraysSig(signture));
    if (res == "int") {
        res = "I";
    } else if (res == "boolean") {
        res = "Z";
    } else if (res == "byte") {
        res = "B";
    } else if (res == "char") {
        res = "C";
    } else if (res == "short") {
        res = "S";
    } else if (res == "long") {
        res = "J";
    } else if (res == "float") {
        res = "F";
    } else if (res == "double") {
        res = "D";
    } else if (res == "void") {
        res = "V";
    } else {
        res = "L" + res + ";";
    }

    std::string pre;
    for (int i = 0; i < arraysCount; i++) {
        pre.append("[");
    }
    pre.append(res);
    return replaceDot(pre);
}

std::string clark::jnis::toJNIClassDesc(const std::string & signture) {
    return replaceDot(signture);
}

jstring clark::jnis::toJString(JNIEnv* env, const std::string & s) {
    return env->NewStringUTF(s.c_str());
}

std::string clark::jnis::toString(JNIEnv* env, const jstring s) {
    if (s == 0) {
        return std::string("");
    }

    const char* temp = env->GetStringUTFChars(s, 0);
    std::string str(temp);
    env->ReleaseStringUTFChars(s, temp);
    return str;
}

jobject clark::jnis::newObject(JNIEnv *env) {
    jclass clazz = env->FindClass("java/lang/Object");
    return env->NewObject(clazz, env->GetMethodID(clazz, "<init>", "()V"));
}

