/*
 * JThread.h
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#ifndef JTHREAD_H_
#define JTHREAD_H_

#include "JObject.h"
#include "JString.h"

namespace su {

enum ThreadState {
    BLOCKED, NEW, RUNNABLE, TERMINATED, TIMED_WAITING, WAITING
};

class JThread: public su::JObject {
public:
    JThread(const jobject thread);
    virtual ~JThread();

    static JThread currentThread();
    static void yield();
    static void sleep(const jlong& millis);
    static void sleep(const jlong& millis, const jint& nanos);
    static jboolean interrupted();
    static jint activeCount();
    static void dumpStack();
    static jboolean holdsLock(const jobject obj);

    void start() const;
    void interrupt() const;
    jboolean isInterrupted() const;
    jboolean isAlive() const;
    void setPriority(const jint& newPriority) const;
    jint getPriority() const;
    void setName(const JString& name) const;
    JString getName() const;
    void join(const jlong& millis) const;
    void join(const jlong& millis, const jint& nanos) const;
    void join() const;
    void setDaemon(const jboolean& on) const;
    jboolean isDaemon() const;
    jlong getId() const;
    ThreadState getState() const;

private:
    JThread();
};

} /* namespace su */
#endif /* JTHREAD_H_ */
