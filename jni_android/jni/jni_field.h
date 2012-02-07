/*
 * jni_field.h
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */

#ifndef JNI_FIELD_H_
#define JNI_FIELD_H_
#include <string>
#include <jni.h>
#include <iostream>
#include "jni_stdout.h"

namespace clark {
    namespace jnis {

        /**
         * field:
         * field_type( )*class_name(#|.)field_name
         */
        class jni_field {
            friend std::ostream& operator <<(std::ostream& out,
                    const jni_field f);
            friend jni_output& operator <<(jni_output& out, const jni_field f);

        public:
            jni_field();
            jni_field(const std::string& sig);
            jni_field(const jni_field& other);
            virtual ~jni_field();

            void reset();
            void initFromSig(const std::string& sig);
            bool isValid(JNIEnv* env = 0) const;
            bool isInvalid(JNIEnv* env = 0) const;

            jclass getClass(JNIEnv* env) const;
            jfieldID getFieldId(JNIEnv* env) const;
            jobject getReflectedField(JNIEnv* env) const;

            jvalue get(JNIEnv* env, const jobject obj = 0) const;
            void set(JNIEnv* env, const jvalue& value,
                    const jobject obj = 0) const;

            const std::string& getClassName() const;
            const std::string& getFieldName() const;
            const std::string& getFieldType() const;
            const bool& isStatic() const;

        private:
            std::string class_name;
            std::string field_name;
            std::string field_type;
            bool is_static;
        };

    } /* namespace jnis */
} /* namespace clark */
#endif /* JNI_FIELD_H_ */
