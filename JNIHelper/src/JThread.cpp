/*
 * JThread.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#include "JThread.h"

#define ERROR_OBJ checkNull(obj, "obj can't be NULL!")

namespace su {

static const char *CLASS_NAME = "java/lang/Thread";

JThread::JThread() :
        JObject() {
    obj = NULL;
}

JThread::JThread(const jobject thread) :
        JObject() {
    obj = thread;
}

JThread::~JThread() {
}

JThread JThread::currentThread() {
    JNIHelper p;
    jobject curThread = p.callStaticObjectMethod(CLASS_NAME, "currentThread", "()Ljava/lang/Thread;");
    JThread t(curThread);
    return t;
}

void JThread::yield() {
    JNIHelper p;
    p.callStaticVoidMethod(CLASS_NAME, "yield", "()V");
}

void JThread::sleep(const jlong& millis) {
    JNIHelper p;
    p.callStaticVoidMethod(CLASS_NAME, "sleep", "(J)V", millis);
}

void JThread::sleep(const jlong& millis, const jint& nanos) {
    JNIHelper p;
    p.callStaticVoidMethod(CLASS_NAME, "sleep", "(JI)V", millis, nanos);
}

void JThread::start() const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "start", "()V");
}

void JThread::interrupt() const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "interrupt", "()V");
}

jboolean JThread::interrupted() {
    JNIHelper p;
    return p.callStaticBooleanMethod(CLASS_NAME, "interrupted", "()Z");
}

jboolean JThread::isInterrupted() const {
    if (ERROR_OBJ) {
        return JNI_FALSE;
    }

    return pj.callBooleanMethod(obj, "isInterrupted", "()Z");
}

jboolean JThread::isAlive() const {
    if (ERROR_OBJ) {
        return JNI_FALSE;
    }

    return pj.callBooleanMethod(obj, "isAlive", "()Z");
}

void JThread::setPriority(const jint& newPriority) const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "setPriority", "(I)V", newPriority);
}

jint JThread::getPriority() const {
    if (ERROR_OBJ) {
        return -1;
    }

    return pj.callIntMethod(obj, "getPriority", "()I");
}

void JThread::setName(const JString& name) const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "setName", "(Ljava/lang/String;)V", name.toString());
}

JString JThread::getName() const {
    if (ERROR_OBJ) {
        JString res;
        return res;
    }

    JString res(pj.callObjectMethod(obj, "getName", "()Ljava/lang/String;"));
    return res;
}

jint JThread::activeCount() {
    JNIHelper p;
    return p.callStaticIntMethod(CLASS_NAME, "activeCount", "()I");
}

void JThread::join(const jlong& millis) const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "join", "(J)V", millis);
}

void JThread::join(const jlong& millis, const jint& nanos) const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "join", "(JI)V", millis, nanos);
}

void JThread::join() const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "join", "()V");
}

void JThread::dumpStack() {
    JNIHelper p;
    p.callStaticVoidMethod(CLASS_NAME, "dumpStack", "()V");
}

void JThread::setDaemon(const jboolean& on) const {
    if (ERROR_OBJ) {
        return;
    }

    pj.callVoidMethod(obj, "setDaemon", "(Z)V", on);
}

jboolean JThread::isDaemon() const {
    if (ERROR_OBJ) {
        return JNI_FALSE;
    }

    return pj.callBooleanMethod(obj, "isDaemon", "()Z");
}

jboolean JThread::holdsLock(const jobject obj) {
    JNIHelper p;
    return p.callStaticBooleanMethod(CLASS_NAME, "holdsLock", "(Ljava/lang/Object;)Z");
}

jlong JThread::getId() const {
    if (ERROR_OBJ) {
        return JNI_FALSE;
    }

    return pj.callLongMethod(obj, "getId", "()J");
}

ThreadState JThread::getState() const {
    if (ERROR_OBJ) {
        return NEW;
    }

    jobject state = pj.callObjectMethod(obj, "getState", "()Ljava/lang/Thread$State;");
    return (ThreadState) pj.callIntMethod(state, "ordinal", "()I");
}

} /* namespace su */
