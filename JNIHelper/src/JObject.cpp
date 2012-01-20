/*
 * JObject.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#include "JObject.h"
#include "JString.h"
#include <cstdio>

namespace su {

std::ostream& operator<<(std::ostream& out, const JObject& obj) {
    out << obj.toString();
    return out;
}

std::ostream& operator<<(std::ostream& out, const jobject obj) {
    if (obj == NULL) {
        out << "NULL";
    } else {
        JNIHelper p;
        jstring str = p.toString(obj);
        const char *temp = p.getStringUTFChars(str);
        out << temp;
        std::fflush(stdout);
        p.releaseStringUTFChars(str, temp);
    }
    return out;
}

std::ostream& operator<<(std::ostream& out, const jclass clazz) {
    return operator<<(out, (jobject) clazz);
}

std::ostream & operator <<(std::ostream & out, const jstring s) {
    return operator <<(out, (jobject) s);
}

std::ostream & operator <<(std::ostream & out, const jarray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jthrowable throwable) {
    return operator <<(out, (jobject) throwable);
}

std::ostream & operator <<(std::ostream & out, const jbooleanArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jbyteArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jcharArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jshortArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jintArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jlongArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jfloatArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jdoubleArray array) {
    return operator <<(out, (jobject) array);
}

std::ostream & operator <<(std::ostream & out, const jobjectArray array) {
    return operator <<(out, (jobject) array);
}

JObject::JObject() :
        pj() {
    error = new char[256];

    obj = pj.newObject("java/lang/Object", "()V");
}

JObject::JObject(const jobject obj) :
        pj() {
    error = new char[256];

    if (obj == NULL) {
        this->obj = pj.newObject("java/lang/Object", "()V");
    } else {
        this->obj = obj;
    }
}

JObject::~JObject() {
    delete[] error;
}

jstring JObject::toString() const {
    return pj.toString(obj);
}

jint JObject::hashCode() const {
    return pj.callIntMethod(obj, "hashCode", "()I");
}

jboolean JObject::equals(const JObject & other) const {
    return pj.callBooleanMethod(obj, "equals", "(Ljava/lang/Object;)Z", other.obj);
}

jclass JObject::getClass() const {
    return (jclass) (((((pj.callObjectMethod(obj, "getClass", "()Ljava/lang/Class;"))))));
}

void JObject::notify() const {
    pj.callVoidMethod(obj, "notify", "()V");
}

void JObject::notifyAll() const {
    pj.callVoidMethod(obj, "notifyAll", "()V");
}

void JObject::wait() const {
    pj.callVoidMethod(obj, "wait", "()V");
}

void JObject::wait(const jlong & timeout) const {
    pj.callVoidMethod(obj, "wait", "(J)V");
}

jboolean JObject::operator ==(const JObject & other) const {
    return equals(other);
}

void JObject::wait(const jlong & timeout, const jint & nanos) const {
    pj.callVoidMethod(obj, "wait", "(JI)V");
}

jboolean JObject::checkArgument(const jboolean & cond, const char *msg) const {
    return pj.checkArgument(cond, msg);
}

jboolean JObject::checkState(const jboolean & cond, const char *msg) const {
    return pj.checkState(cond, msg);
}

jboolean JObject::checkNull(const void *p, const char *msg) const {
    return pj.checkNull(p, msg);
}

jboolean JObject::operator ==(const char *s) const {
    JString str(s);
    return equals(str);
}

const char *JObject::_c_log__(const char *msg, const long & line, const char *fname) const {
    sprintf(error, "[行:%ld  文件:%s] %s\n", line, fname, msg);
    return error;
}

} /* namespace su */
