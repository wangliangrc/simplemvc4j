/*
 * jni_stdout.h
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */

#ifndef JNI_STDOUT_H_
#define JNI_STDOUT_H_

#include <jni.h>
#include <iostream>
#include <string>
#include "jni_method.h"

namespace clark {
    namespace jnis {

        int endl();
        int flush();

        class jni_output {
        public:
            virtual ~jni_output();

            jni_output& operator <<(int(* const op)(void));
            jni_output& operator <<(const char* s);
            jni_output& operator <<(const std::string& s);
            jni_output& operator <<(const void* p);
            jni_output& operator <<(const bool& val);

            jni_output& operator <<(const jboolean& val);
            jni_output& operator <<(const jbyte& val);
            jni_output& operator <<(const jfloat& val);
            jni_output& operator <<(const jdouble& val);
            jni_output& operator <<(const jchar& val);
            jni_output& operator <<(const jlong& val);
            jni_output& operator <<(const jint& val);
            jni_output& operator <<(const jstring val);
            jni_output& operator <<(const jobject val);

            jni_output& print(const jbyte& val);
            jni_output& print(const jfloat& val);
            jni_output& print(const jdouble& val);
            jni_output& print(const jchar& val);
            jni_output& print(const jlong& val);
            jni_output& print(const jint& val);
            jni_output& print(const jboolean& val);
            jni_output& print(const jstring val);
            jni_output& print(const jobject val);

            jni_output& flush();

            static jni_output std_out(JNIEnv* env);
            static jni_output std_err(JNIEnv* env);

        private:
            jni_output(JNIEnv* env);
            jni_output(JNIEnv* env, const jobject printstream);

        private:
            JNIEnv* env;
            jobject printstream;
        };

    } /* namespace jnis */
} /* namespace clark */
#endif /* JNI_STDOUT_H_ */
