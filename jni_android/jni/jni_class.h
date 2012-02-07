/*
 * jni_class.h
 *
 *  Created on: Feb 3, 2012
 *      Author: clark
 */

#ifndef JNI_CLASS_H_
#define JNI_CLASS_H_

#include <jni.h>
#include <string>

namespace clark {
    namespace jnis {

        class jni_class {
        public:
            jni_class(JNIEnv *env);
            virtual ~jni_class();

            jclass getClass(const jobject obj) const;
            jclass getClass(const std::string& className) const;

            /**
             * 表示 owner 继承(owner 自身为接口的 Class 对象)或者实现的接口的 Class 对象
             */
            jobjectArray getInterfaces(const jclass owner) const;

            /**
             * 返回 owner 定义时继承的父类 Class 对象
             */
            jclass getSuperclass(const jclass owner) const;

            jobjectArray getDeclaredClasses(const jclass owner) const;
            jclass getEnclosingClass(const jclass owner) const;
            jclass getDeclaringClass(const jclass owner) const;

            jobject getClassLoader(const jclass owner) const;

            jobject getAnnotation(const jclass owner,
                    const jclass annotation) const;

            /**
             * 如果 getAnnotation(const jclass owner, const jclass annotation) 不返回 NULL 那么本方法r
             */
            bool isAnnotationPresent(const jclass owner,
                    const jclass annotation) const;
            jobjectArray getAnnotations(const jclass owner) const;
            jobjectArray getDeclaredAnnotations(const jclass owner) const;

            jstring getCanonicalName(const jclass owner) const;
            jstring getName(const jclass owner) const;
            jstring getSimpleName(const jclass owner) const;
            jobject getPackage(const jclass owner) const;

            /**
             * 如果 owner 是一个数组，则该函数返回值为概述组的元素类型
             */
            jclass getComponentType(const jclass owner) const;

            /**
             * 如果 owner 是一个枚举，则返回该枚举的几个常量对象
             */
            jobjectArray getEnumConstants(const jclass owner) const;

            jobject getConstructor(const jclass owner,
                    const jobjectArray param_classes) const;
            jobjectArray getConstructors(const jclass owner) const;
            jobject getDeclaredConstructor(const jclass owner,
                    const jobjectArray param_classes) const;
            jobjectArray getDeclaredConstructors(const jclass owner) const;
            jobject getEnclosingConstructor(const jclass owner) const;

            jobject getDeclaredField(const jclass owner,
                    const std::string& name) const;
            jobjectArray getDeclaredFields(const jclass owner) const;
            jobject getField(const jclass owner, const std::string& name) const;
            jobjectArray getFields(const jclass owner) const;

            jobject getDeclaredMethod(const jclass owner,
                    const std::string& name,
                    const jobjectArray param_classes) const;
            jobjectArray getDeclaredMethods(const jclass owner) const;
            jobject getEnclosingMethod(const jclass owner) const;
            jobject getMethod(const jclass owner, const std::string& name,
                    const jobjectArray param_classes) const;
            jobjectArray getMethods(const jclass owner) const;

            bool isAnnotation(const jclass owner) const;
            bool isAnonymousClass(const jclass owner) const;
            bool isArray(const jclass owner) const;
            bool isPrimitiveArray(const jclass owner) const;
            bool isObjectArray(const jclass owner) const;
            bool isEnum(const jclass owner) const;
            bool isInstanceOf(const jclass target, const jobject obj) const;
            bool isInterface(const jclass owner) const;
            bool isLocalClass(const jclass owner) const;
            bool isMemberClass(const jclass owner) const;
            bool isPrimitive(const jclass owner) const;
            bool isSynthetic(const jclass owner) const;

            jint getModifiers(const jclass owner) const;
            /**
             * 返回 java.net.URL 对象
             */
            jobject getResource(const jclass owner,
                    const std::string& name) const;
            /**
             * 返回 java.io.InputStream 对象
             */
            jobject getResourceAsStream(const jclass owner,
                    const std::string& name) const;

            /**
             * Java Field 对象转换为 JNI 的 jfieldID 指针
             */
            jfieldID fromReflectedField(const jobject field) const;

            /**
             * Java Method 对象转换为 JNI 的 jmethodID 指针
             */
            jmethodID fromReflectedMethod(const jobject method) const;

        private:
            JNIEnv *env;
        };

    } /* namespace jnis */
} /* namespace clark */
#endif /* JNI_CLASS_H_ */
