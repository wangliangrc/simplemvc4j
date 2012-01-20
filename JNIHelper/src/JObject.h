/*
 * JObject.h
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#ifndef JOBJECT_H_
#define JOBJECT_H_

#include "JNIHelper.h"
#include <iostream>

#define stackTrace(msg) _c_log__(msg, __LINE__, __FILE__)

namespace su {

class JObject {
    friend std::ostream& operator<<(std::ostream& out, const JObject& obj);
    friend std::ostream& operator<<(std::ostream& out, const jobject obj);
    friend std::ostream& operator<<(std::ostream& out, const jclass clazz);
    friend std::ostream& operator<<(std::ostream& out, const jstring s);
    friend std::ostream& operator<<(std::ostream& out, const jarray array);
    friend std::ostream& operator<<(std::ostream& out, const jthrowable throwable);
    friend std::ostream& operator<<(std::ostream& out, const jbooleanArray array);
    friend std::ostream& operator<<(std::ostream& out, const jbyteArray array);
    friend std::ostream& operator<<(std::ostream& out, const jcharArray array);
    friend std::ostream& operator<<(std::ostream& out, const jshortArray array);
    friend std::ostream& operator<<(std::ostream& out, const jintArray array);
    friend std::ostream& operator<<(std::ostream& out, const jlongArray array);
    friend std::ostream& operator<<(std::ostream& out, const jfloatArray array);
    friend std::ostream& operator<<(std::ostream& out, const jdoubleArray array);
    friend std::ostream& operator<<(std::ostream& out, const jobjectArray array);

public:
    JObject();
    JObject(const jobject obj);
    virtual ~JObject();

    virtual jstring toString() const;
    virtual jint hashCode() const;
    virtual jboolean equals(const JObject& other) const;

    jboolean operator ==(const JObject& other) const;
    jboolean operator ==(const char *s) const;

    jclass getClass() const;
    void notify() const;
    void notifyAll() const;
    void wait() const;
    void wait(const jlong& timeout) const;
    void wait(const jlong& timeout, const jint& nanos) const;

protected:
    jboolean checkArgument(const jboolean& cond, const char *msg) const;
    jboolean checkState(const jboolean& cond, const char *msg) const;
    jboolean checkNull(const void *p, const char *msg) const;
    const char* _c_log__(const char *msg, const long & line, const char *fname) const;

    jobject obj;
    JNIHelper pj;

private:
    char * error;

};

} /* namespace su */
#endif /* JOBJECT_H_ */
