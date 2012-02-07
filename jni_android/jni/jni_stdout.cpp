/*
 * jni_stdout.cc
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */

#include "jni_stdout.h"
#include <cstdio>
#include "jni_field.h"
#include "logger.h"

using clark::androids::logger;

namespace clark {
    namespace jnis {

        namespace {
            jni_method FLOAT_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(float)");
            jni_method DOUBLE_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(double)");
            jni_method CHAR_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(char)");
            jni_method LONG_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(long)");
            jni_method INT_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(int)");
            jni_method BYTE_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(byte)");
            jni_method BOOLEAN_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(boolean)");
            jni_method STRING_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(java.lang.String)");
            jni_method OBJECT_PRINT_METHOD = jni_method(
                    "void java.io.PrintStream#print(java.lang.Object)");

            jni_method FLUSH_METHOD = jni_method(
                    "void java.io.PrintStream#flush()");

            const char *TAG = "jni_stdout";
            const int ENDL_CODE = 1001;
            const int FLUSH_CODE = 1002;
        }

        jni_output::jni_output(JNIEnv * env) :
                env(env) {
            const jclass SYSTEM_CLASS = this->env->FindClass(
                    "java/lang/System");
            const jfieldID STD_OUT = this->env->GetStaticFieldID(SYSTEM_CLASS,
                    "out", "Ljava/io/PrintStream;");
            printstream = this->env->GetStaticObjectField(SYSTEM_CLASS,
                    STD_OUT);
        }

        jni_output jni_output::std_out(JNIEnv *env) {
            jni_field out("java.io.PrintStream java.lang.System.out");
            jni_output out_(env, out.get(env).l);
            return out_;
        }

        jni_output jni_output::std_err(JNIEnv *env) {
            jni_field err("java.io.PrintStream java.lang.System.err");
            jni_output out_(env, err.get(env).l);
            return out_;
        }

        jni_output & jni_output::operator <<(const jboolean & val) {
            return print(val);
        }

        jni_output::jni_output(JNIEnv *env, const jobject printstream) :
                env(env) {
            const jclass PRINTSTREAM_CLASS = this->env->FindClass(
                    "java/io/PrintStream");

            if (printstream != 0) {
                if (this->env->IsInstanceOf(printstream, PRINTSTREAM_CLASS)) {
                    this->printstream = printstream;
                    return;
                }
            }

            const jfieldID STD_OUT = this->env->GetStaticFieldID(
                    PRINTSTREAM_CLASS, "out", "Ljava/io/PrintStream;");
            this->printstream = this->env->GetStaticObjectField(
                    PRINTSTREAM_CLASS, STD_OUT);
        }

        jni_output::~jni_output() {
        }

        jni_output & jni_output::print(const jfloat & val) {
            FLOAT_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jdouble & val) {
            DOUBLE_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jchar & val) {
            CHAR_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jlong & val) {
            LONG_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jint & val) {
            INT_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jboolean & val) {
            BOOLEAN_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jstring val) {
            if (val == 0) {
                logger::e_print(TAG, "jni_output::print(const jstring)\n\t%s\n",
                        "can't accept NULL");
                return *this;
            }
            STRING_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        jni_output & jni_output::print(const jobject val) {
            if (val == 0) {
                logger::e_print(TAG, "jni_output::print(const jobject)\n\t%s\n",
                        "can't accept NULL");
                return *this;
            }
            OBJECT_PRINT_METHOD.call(env, printstream, val);
            return *this;
        }

        int endl() {
            return ENDL_CODE;
        }

        int flush() {
            return FLUSH_CODE;
        }

        jni_output & jni_output::operator <<(int(* const op)(void)) {
            int code = op();
            switch (code) {
                case ENDL_CODE:
                    print(env->NewStringUTF("\n"));
                    flush();
                    break;
                case FLUSH_CODE:
                    flush();
                    break;
                default:
                    break;
            }
            return *this;
        }

        jni_output & jni_output::operator <<(const char *s) {
            if (s == 0) {
                print(env->NewStringUTF("null"));
            }
            print(env->NewStringUTF(s));
            return *this;
        }

        jni_output & jni_output::operator <<(const std::string & s) {
            return operator <<(s.c_str());
        }

        jni_output & jni_output::operator <<(const void *p) {
            char buf[20];
            std::sprintf(buf, "%p", p);
            return operator <<(buf);
        }

        jni_output & jni_output::operator <<(const bool & val) {
            print((jboolean) (val));
            return *this;
        }

        jni_output & jni_output::operator <<(const jfloat & val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jdouble & val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jchar & val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jlong & val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jint & val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jstring val) {
            return print(val);
        }

        jni_output & jni_output::operator <<(const jobject val) {
            return print(val);
        }

        jni_output & jni_output::print(const jbyte & val) {
            return print((jint) val);
        }

        jni_output & jni_output::operator <<(const jbyte & val) {
            return print((jint) val);
        }

        jni_output & jni_output::flush() {
            FLUSH_METHOD.call(env, printstream);
            return *this;
        }

    } /* namespace jnis */
}

/* namespace clark */
