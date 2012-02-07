/*
 * jni_method.h
 *
 *  Created on: 2012-2-1
 *      Author: Administrator
 */

#ifndef JNI_METHOD_H_
#define JNI_METHOD_H_

#include <string>
#include <vector>
#include <iostream>
#include <jni.h>

namespace clark {
    namespace jnis {

        class jni_output;

        /**
         * arg:
         * arg_type("[]")*("...")*[ param_name]
         * 
         * arg_list:
         * ""|arg(,[ ]arg)*
         * 
         * sig:
         * [return_type ]class_name[(.|#)method_name](arg_list)
         */
        class jni_method {
            friend std::ostream& operator <<(std::ostream& out,
                    const jni_method& method);
            friend jni_output& operator <<(jni_output& out,
                    const jni_method& method);

        public:
            jni_method();
            jni_method(const std::string& sig);
            jni_method(const jni_method& other);
            virtual ~jni_method();

            void initFromSig(const std::string& sig);
            void reset();

            const std::vector<std::string>& getArgTypes() const;
            const std::string& getClassName() const;
            const std::string& getMethodName() const;
            const std::string& getReturnType() const;
            const bool& isStatic() const;

            bool isValid(JNIEnv* env = 0) const;
            bool isInvalid(JNIEnv* env = 0) const;

            jclass getClass(JNIEnv* env) const;
            jmethodID getMethodId(JNIEnv* env) const;
            jobject getReflectedMethod(JNIEnv* env) const;
            jvalue call(JNIEnv* env, const jobject obj = 0, ...) const;

        private:
            const char* toNativeSig(std::string& holder) const;

            std::string class_name;
            std::string method_name;
            std::vector<std::string> arg_types;
            std::string return_type;
            bool is_static;
        };
    } /* namespace jnis */
} /* namespace clark */
#endif /* JNI_METHOD_H_ */
