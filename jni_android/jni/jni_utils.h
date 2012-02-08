/*
 * jni_utils.h
 *
 *  Created on: 2012-1-31
 *      Author: Clark
 */

#ifndef JNI_UTILS_H_
#define JNI_UTILS_H_

#include <jni.h>
#include <string>
#include <vector>

namespace clark {

    namespace jnis {

        /**
         * 将 com.foo.Foo 形式更改为 Lcom/foo/Foo;
         */
        std::string toJNIFieldDesc(const std::string& signture);

        /**
         * 将 com.foo.Foo 形式更改为 com/foo/Foo
         */
        std::string toJNIClassDesc(const std::string& signture);

        jstring toJString(JNIEnv* env, const std::string& s);
        std::string toString(JNIEnv* env, const jstring s);

        jobject newObject(JNIEnv* env);

        /**
         * 判断弱全局引用是否还有效
         */
        bool isReferenceValid(JNIEnv *env, const jobject obj);

    } // namespace jnis

} // namespace clark

#endif /* JNI_UTILS_H_ */
