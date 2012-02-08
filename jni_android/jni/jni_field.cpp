/*
 * jni_field.cpp
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */
//#define DEBUG
#include "jni_field.h"
#include "string_utils.h"
#include "jni_utils.h"
#include <vector>
#include "logger.h"

using clark::strings::trim;
using clark::strings::split;
using clark::jnis::toJNIFieldDesc;
using clark::jnis::toJNIClassDesc;
using clark::strings::valueof;
using clark::androids::logger;

namespace clark {
    namespace jnis {

        namespace {
            const char *TAG = "jni_field";
        }

        jni_field::jni_field() :
                class_name(), field_name(), field_type(), is_static(false) {
        }

        jni_field::jni_field(const jni_field & other) :
                class_name(other.class_name), field_name(other.field_name), field_type(
                        other.field_type), is_static(other.is_static) {
        }

        jni_field::jni_field(const std::string & sig) :
                class_name(), field_name(), field_type(), is_static(false) {
            initFromSig(sig);
        }

        jni_field::~jni_field() {
        }

        void jni_field::reset() {
            class_name.clear();
            field_name.clear();
            field_type.clear();
            is_static = false;
        }

        void jni_field::initFromSig(const std::string & sig) {
            reset();

            std::string signture = trim(sig);
            int index = signture.find(' ');

            assert_android(index >= 0, TAG, "error sig!");

            std::vector<std::string> parts = split(signture, ' ');
            field_type = toJNIFieldDesc(parts[0]);
            index = parts[1].find('#');
            if (index > 0) {
                class_name = toJNIClassDesc(parts[1].substr(0, index));
                field_name = parts[1].substr(index + 1);
                is_static = false;
            } else {
                index = parts[1].find_last_of('.');

                assert_android(index > 0, TAG, "error sig, No '#' or '.'!");

                class_name = toJNIClassDesc(parts[1].substr(0, index));
                field_name = parts[1].substr(index + 1);
                is_static = true;
            }
        }

        bool jni_field::isValid(JNIEnv* env) const {
            if (env == 0) {
                return !class_name.empty() && !field_name.empty()
                        && !field_type.empty();
            } else {
                jclass clas = getClass(env);
                bool res = clas != 0 && getFieldId(env) != 0;
                env->DeleteLocalRef(clas);
                return res;
            }
        }

        bool jni_field::isInvalid(JNIEnv* env) const {
            return !isValid(env);
        }

        const std::string & jni_field::getClassName() const {
            return class_name;
        }

        const std::string & jni_field::getFieldName() const {
            return field_name;
        }

        const std::string & jni_field::getFieldType() const {
            return field_type;
        }

        jclass jni_field::getClass(JNIEnv * env) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(!class_name.empty(), TAG,
                    "Maybe you didn't init jni_field yet!");

            jclass internal_class = env->FindClass(class_name.c_str());

            assert_android(internal_class, TAG,
                    ("can't find java class!( " + class_name + " )").c_str());
            return internal_class;
        }

        jfieldID jni_field::getFieldId(JNIEnv * env) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(isValid(), TAG,
                    "Maybe you didn't init jni_field yet!");

            jfieldID internal_field = 0;
            if (is_static) {
                internal_field = env->GetStaticFieldID(getClass(env),
                        field_name.c_str(), field_type.c_str());
            } else {
                internal_field = env->GetFieldID(getClass(env),
                        field_name.c_str(), field_type.c_str());
            }

            assert_android(internal_field, TAG,
                    ("can't find java field!( " + class_name + " )").c_str());
            return internal_field;
        }

        std::ostream & operator <<(std::ostream & out, const jni_field f) {
            out << "class name: " << f.class_name << "\n";
            out << "field name: " << f.field_name << "\n";
            out << "field type: " << f.field_type << "\n";
            out << "static: " << valueof(f.is_static);
            return out;
        }

        jvalue jni_field::get(JNIEnv * env, const jobject obj) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(!(obj == 0 && !is_static), TAG,
                    "Non static field must belong some java.lang.Object!");

            jvalue res;
            jclass clazz = getClass(env);

            assert_android(
                    !(obj != 0 && !env->IsInstanceOf(obj, clazz)),
                    TAG,
                    "obj's class is not the same class as or father class of the field define!");

            if (field_type == "Z") {
                if (is_static) {
                    res.z = env->GetStaticBooleanField(clazz, getFieldId(env));
                } else {
                    res.z = env->GetBooleanField(obj, getFieldId(env));
                }
            } else if (field_type == "I") {
                if (is_static) {
                    res.i = env->GetStaticIntField(clazz, getFieldId(env));
                } else {
                    res.i = env->GetIntField(obj, getFieldId(env));
                }
            } else if (field_type == "B") {
                if (is_static) {
                    res.b = env->GetStaticByteField(clazz, getFieldId(env));
                } else {
                    res.b = env->GetByteField(obj, getFieldId(env));
                }
            } else if (field_type == "C") {
                if (is_static) {
                    res.c = env->GetStaticCharField(clazz, getFieldId(env));
                } else {
                    res.c = env->GetCharField(obj, getFieldId(env));
                }
            } else if (field_type == "S") {
                if (is_static) {
                    res.s = env->GetStaticShortField(clazz, getFieldId(env));
                } else {
                    res.s = env->GetShortField(obj, getFieldId(env));
                }
            } else if (field_type == "J") {
                if (is_static) {
                    res.j = env->GetStaticLongField(clazz, getFieldId(env));
                } else {
                    res.j = env->GetLongField(obj, getFieldId(env));
                }
            } else if (field_type == "F") {
                if (is_static) {
                    res.f = env->GetStaticFloatField(clazz, getFieldId(env));
                } else {
                    res.f = env->GetFloatField(obj, getFieldId(env));
                }
            } else if (field_type == "D") {
                if (is_static) {
                    res.d = env->GetStaticDoubleField(clazz, getFieldId(env));
                } else {
                    res.d = env->GetDoubleField(obj, getFieldId(env));
                }
            } else {
                if (is_static) {
                    res.l = env->GetStaticObjectField(clazz, getFieldId(env));
                } else {
                    res.l = env->GetObjectField(obj, getFieldId(env));
                }
            }

            env->DeleteLocalRef(clazz);
            return res;
        }

        void jni_field::set(JNIEnv * env, const jvalue & value,
                const jobject obj) const {
            assert_android(env != 0, TAG, "env can't be NULL!");
            assert_android(!(obj == 0 && !is_static), TAG,
                    "Non static field must belong some java.lang.Object!");

            jclass clazz = getClass(env);

            assert_android(
                    !(obj != 0 && !env->IsInstanceOf(obj, clazz)),
                    TAG,
                    "obj's class is not the same class as or father class of the field define!");

            if (field_type == "Z") {
                if (is_static) {
                    env->SetStaticBooleanField(clazz, getFieldId(env), value.z);
                } else {
                    env->SetBooleanField(obj, getFieldId(env), value.z);
                }
            } else if (field_type == "I") {
                if (is_static) {
                    env->SetStaticIntField(clazz, getFieldId(env), value.i);
                } else {
                    env->SetIntField(obj, getFieldId(env), value.i);
                }
            } else if (field_type == "B") {
                if (is_static) {
                    env->SetStaticByteField(clazz, getFieldId(env), value.b);
                } else {
                    env->SetByteField(obj, getFieldId(env), value.b);
                }
            } else if (field_type == "C") {
                if (is_static) {
                    env->SetStaticCharField(clazz, getFieldId(env), value.c);
                } else {
                    env->SetCharField(obj, getFieldId(env), value.c);
                }
            } else if (field_type == "S") {
                if (is_static) {
                    env->SetStaticShortField(clazz, getFieldId(env), value.s);
                } else {
                    env->SetShortField(obj, getFieldId(env), value.s);
                }
            } else if (field_type == "J") {
                if (is_static) {
                    env->SetStaticLongField(clazz, getFieldId(env), value.j);
                } else {
                    env->SetLongField(obj, getFieldId(env), value.j);
                }
            } else if (field_type == "F") {
                if (is_static) {
                    env->SetStaticFloatField(clazz, getFieldId(env), value.f);
                } else {
                    env->SetFloatField(obj, getFieldId(env), value.f);
                }
            } else if (field_type == "D") {
                if (is_static) {
                    env->SetStaticDoubleField(clazz, getFieldId(env), value.d);
                } else {
                    env->SetDoubleField(obj, getFieldId(env), value.d);
                }
            } else {
                if (is_static) {
                    env->SetStaticObjectField(clazz, getFieldId(env), value.l);
                } else {
                    env->SetObjectField(obj, getFieldId(env), value.l);
                }
            }

            env->DeleteLocalRef(clazz);
        }

        jni_output & operator <<(jni_output & out, const jni_field f) {
            out << "class name: " << f.class_name << "\n";
            out << "field name: " << f.field_name << "\n";
            out << "field type: " << f.field_type << "\n";
            out << "static: " << valueof(f.is_static);
            return out;
        }

        jobject jni_field::getReflectedField(JNIEnv *env) const {
            if (isInvalid(env)) {
                return 0;
            }

            return env->ToReflectedField(getClass(env), getFieldId(env),
                    is_static);
        }

        const bool & jni_field::isStatic() const {
            return is_static;
        }

    } /* namespace jnis */
} /* namespace clark */
