/*
 * jni_method.cc
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */
//#define DEBUG
#include "jni_method.h"
#include "string_utils.h"
#include "jni_utils.h"
#include <cstdarg>
#include "jni_stdout.h"
#include "logger.h"

using clark::androids::logger;
using clark::strings::trim;
using clark::strings::split;
using clark::jnis::toJNIFieldDesc;
using clark::jnis::toJNIClassDesc;
using clark::strings::valueof;
using clark::strings::startsWiths;

namespace clark {
    namespace jnis {

        namespace {
            const char *TAG = "jni_method";
        }

        jni_method::jni_method() :
                class_name(), method_name(), arg_types(), return_type(), is_static(
                        false) {
        }

        jni_method::~jni_method() {
        }

        const std::vector<std::string>& jni_method::getArgTypes() const {
            return arg_types;
        }

        const std::string& jni_method::getClassName() const {
            return class_name;
        }

        const std::string& jni_method::getMethodName() const {
            return method_name;
        }

        const std::string& jni_method::getReturnType() const {
            return return_type;
        }

        void jni_method::initFromSig(const std::string & signture) {
            reset();

            int index = 0;
            index = signture.find(')');

            assert_android(index >= 0, TAG, "signture has no ')'!");

            std::string sig = trim(signture.substr(0, index));
            index = sig.find('(');

            assert_android(index >= 0, TAG, "signture has no '('!");

            std::string paramsPart;
            paramsPart = trim(sig.substr(index + 1));
            method_name = sig.substr(0, index);
            index = method_name.find(' ');
            if (index < 0) {
                class_name = toJNIClassDesc(method_name);
                method_name = "<init>";
                return_type = toJNIFieldDesc(std::string("void"));
                is_static = false;
            } else {
                return_type = toJNIFieldDesc(method_name.substr(0, index));
                method_name = trim(method_name.substr(index));
                index = method_name.find('#');
                if (index > 0) {
                    class_name = toJNIClassDesc(method_name.substr(0, index));
                    method_name = method_name.substr(index + 1);
                    is_static = false;
                } else {
                    index = method_name.find_last_of('.');

                    assert_android(index > 0, TAG,
                            "signture has no 'classname.methodname'!");

                    class_name = toJNIClassDesc(method_name.substr(0, index));
                    method_name = method_name.substr(index + 1);
                    is_static = true;
                }

            }

            if (!paramsPart.empty()) {
                arg_types = split(paramsPart, ',');
                for (int i = 0, len = arg_types.size(); i < len; ++i) {
                    arg_types[i] = trim(arg_types[i]);
                    index = arg_types[i].find(' ');
                    if (index > 0) {
                        arg_types[i] = toJNIFieldDesc(
                                arg_types[i].substr(0, index));
                    } else {
                        arg_types[i] = toJNIFieldDesc(arg_types[i]);
                    }
                }

            }

        }

        jni_method::jni_method(const jni_method & other) :
                class_name(other.class_name), method_name(other.method_name), arg_types(
                        other.arg_types), return_type(other.return_type), is_static(
                        other.is_static) {
        }

        void jni_method::reset() {
            class_name.clear();
            method_name.clear();
            arg_types.clear();
            return_type.clear();
            is_static = false;
        }

        jni_method::jni_method(const std::string & sig) :
                class_name(), method_name(), arg_types(), return_type(), is_static(
                        false) {
            initFromSig(sig);
        }

        std::ostream & operator <<(std::ostream & out,
                const jni_method & method) {
            out << "java class: " << method.class_name << "\n"
                    << "method name: " << method.method_name << "\n"
                    << "arguments list: <";
            for (int i = 0, len = method.arg_types.size(); i < len; ++i) {
                if (i != 0) {
                    out << ", ";
                }
                out << method.arg_types[i];
            }

            out << ">" << "\n";
            out << "return: " << method.return_type << "\n";
            out << "static: " << valueof(method.is_static) << "\n";
            std::string sig;
            out << "JNI signture: " << method.toNativeSig(sig) << "\n";
            return out;
        }

        jni_output & operator <<(jni_output & out, const jni_method & method) {
            out << "java class: " << method.class_name << "\n"
                    << "method name: " << method.method_name << "\n"
                    << "arguments list: <" << flush;
            for (int i = 0, len = method.arg_types.size(); i < len; ++i) {
                if (i != 0) {
                    out << ", ";
                }
                out << method.arg_types[i];
            }

            out << ">" << "\n";
            out << "return: " << method.return_type << "\n";
            out << "static: " << valueof(method.is_static) << "\n";
            std::string sig;
            out << "JNI signture: " << method.toNativeSig(sig) << "\n";
            return out;
        }

        const bool & jni_method::isStatic() const {
            return is_static;
        }

        jclass jni_method::getClass(JNIEnv *env) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(isValid(), TAG,
                    "Maybe your jni_method didn't init!");

            jclass internal_class = env->FindClass(class_name.c_str());

            assert_android(internal_class, TAG,
                    ("class not find < " + class_name + " >").c_str());

            return internal_class;
        }

        jmethodID jni_method::getMethodId(JNIEnv *env) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(isValid(), TAG,
                    "Maybe your jni_method didn't init!");

            std::string holder;
            jmethodID internal_method_id = 0;
            const char *csig = toNativeSig(holder);
            if (is_static) {
                internal_method_id = env->GetStaticMethodID(getClass(env),
                        method_name.c_str(), csig);
            } else {
                internal_method_id = env->GetMethodID(getClass(env),
                        method_name.c_str(), csig);
            }

            assert_android(
                    internal_method_id,
                    TAG,
                    ("jmethodID not find < " + holder + " >").c_str());

            return internal_method_id;
        }

        bool jni_method::isValid(JNIEnv *env) const {
            if (env == 0) {
                return !class_name.empty() && !return_type.empty()
                        && !method_name.empty();
            } else {
                return getClass(env) != 0 && getMethodId(env) != 0;
            }
        }

        bool jni_method::isInvalid(JNIEnv *env) const {
            return !isValid(env);
        }

        jvalue jni_method::call(JNIEnv *env, const jobject obj, ...) const {
            assert_android(env != 0, TAG, "env can't be NULL!");

            va_list args;
            va_start(args, obj);
            jvalue value;

            assert_android(
                    !(!is_static && obj == 0 && method_name != "<init>"),
                    TAG,
                    "Not static or <init> method must pass a non-null obj argument!");

            jclass clazz = getClass(env);

            assert_android(
                    !(obj != 0 && !env->IsInstanceOf(obj, clazz)),
                    TAG,
                    "obj's jclass is not the same as or father class of the method's jclass object!");

            if (return_type == "V") {
                if (is_static) {
                    env->CallStaticVoidMethodV(clazz, getMethodId(env), args);
                } else {
                    if (method_name == "<init>") {
                        value.l = env->NewObjectV(clazz, getMethodId(env),
                                args);
                    } else {
                        env->CallVoidMethodV(obj, getMethodId(env), args);
                    }
                }

            } else if (return_type == "I") {
                if (is_static) {
                    value.i = env->CallStaticIntMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.i = env->CallIntMethodV(obj, getMethodId(env), args);
                }
            } else if (return_type == "Z") {
                if (is_static) {
                    value.z = env->CallStaticBooleanMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.z = env->CallBooleanMethodV(obj, getMethodId(env),
                            args);
                }
            } else if (return_type == "B") {
                if (is_static) {
                    value.b = env->CallStaticByteMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.b = env->CallByteMethodV(obj, getMethodId(env), args);
                }
            } else if (return_type == "C") {
                if (is_static) {
                    value.c = env->CallStaticCharMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.c = env->CallCharMethodV(obj, getMethodId(env), args);
                }
            } else if (return_type == "S") {
                if (is_static) {
                    value.s = env->CallStaticShortMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.s = env->CallShortMethodV(obj, getMethodId(env),
                            args);
                }
            } else if (return_type == "J") {
                if (is_static) {
                    value.j = env->CallStaticLongMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.j = env->CallLongMethodV(obj, getMethodId(env), args);
                }
            } else if (return_type == "F") {
                if (is_static) {
                    value.f = env->CallStaticFloatMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.f = env->CallFloatMethodV(obj, getMethodId(env),
                            args);
                }
            } else if (return_type == "D") {
                if (is_static) {
                    value.d = env->CallStaticDoubleMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.d = env->CallDoubleMethodV(obj, getMethodId(env),
                            args);
                }
            } else {
                if (is_static) {
                    value.l = env->CallStaticObjectMethodV(getClass(env),
                            getMethodId(env), args);
                } else {
                    value.l = env->CallObjectMethodV(obj, getMethodId(env),
                            args);
                }
            }

            va_end(args);
            return value;
        }

        jobject jni_method::getReflectedMethod(JNIEnv *env) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            if (isInvalid(env)) {
                return 0;
            }

            return env->ToReflectedMethod(getClass(env), getMethodId(env),
                    is_static);
        }

        const char *jni_method::toNativeSig(std::string& holder) const {
            holder.clear();
            holder.append("(");
            for (int i = 0, len = arg_types.size(); i < len; ++i) {
                holder.append(arg_types[i]);
            }
            holder.append(")");
            holder.append(return_type);
            return holder.c_str();
        }

    } /* namespace jnis */
} /* namespace clark */
