/*
 * jni_class.cpp
 *
 *  Created on: Feb 3, 2012
 *      Author: clark
 */

#include "jni_class.h"
#include "jni_method.h"

namespace clark {
    namespace jnis {

        namespace {
            const char *TAG = "jni_class";
        }

        jni_class::jni_class(JNIEnv *env) :
                env(env) {
        }

        jni_class::~jni_class() {
        }

        jclass jni_class::getClass(const jobject obj) const {
            if (!obj) {
                return 0;
            }

            return env->GetObjectClass(obj);
        }

        jclass jni_class::getClass(const std::string & className) const {
            if (className.empty()) {
                return 0;
            }

            return env->FindClass(className.c_str());
        }

        jclass jni_class::getSuperclass(const jclass owner) const {
            if (!owner) {
                return 0;
            }

            return env->GetSuperclass(owner);
        }

        bool jni_class::isInstanceOf(const jclass target,
                const jobject obj) const {
            if (!target || !obj) {
                return false;
            }

            return env->IsInstanceOf(obj, target);
        }

        jfieldID jni_class::fromReflectedField(const jobject field) const {
            if (field == 0) {
                return 0;
            }

            return env->FromReflectedField(field);
        }

        jint jni_class::getModifiers(const jclass owner) const {
            if (owner == 0) {
                return 0;
            }

            jni_method m("int java.lang.Class#getModifiers()");
            return m.call(env, owner).i;
        }

        jobject jni_class::getResource(const jclass owner,
                const std::string & name) const {
            if (owner == 0 || name.empty()) {
                return 0;
            }

            jni_method m("java.net.URL "
                    "java.lang.Class#getResource(java.lang.String name)");
            return m.call(env, owner, env->NewStringUTF(name.c_str())).l;
        }

        jobject jni_class::getResourceAsStream(const jclass owner,
                const std::string & name) const {
            if (owner == 0 || name.empty()) {
                return 0;
            }

            jni_method m(
                    "java.io.InputStream "
                            "java.lang.Class#getResourceAsStream(java.lang.String name)");
            return m.call(env, owner, env->NewStringUTF(name.c_str())).l;
        }

        bool jni_class::isAnnotation(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isAnnotation()");
            return m.call(env, owner).z;
        }

        bool jni_class::isAnonymousClass(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isAnonymousClass()");
            return m.call(env, owner).z;
        }

        bool jni_class::isArray(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isArray()");
            return m.call(env, owner).z;
        }

        bool jni_class::isPrimitiveArray(const jclass owner) const {
            return isArray(owner) && isPrimitive(getComponentType(owner));
        }

        bool jni_class::isObjectArray(const jclass owner) const {
            return isArray(owner) && !isPrimitive(getComponentType(owner));
        }

        bool jni_class::isEnum(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isEnum()");
            return m.call(env, owner).z;
        }

        bool jni_class::isInterface(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isInterface()");
            return m.call(env, owner).z;
        }

        bool jni_class::isLocalClass(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isLocalClass()");
            return m.call(env, owner).z;
        }

        bool jni_class::isMemberClass(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isMemberClass()");
            return m.call(env, owner).z;
        }

        bool jni_class::isPrimitive(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isPrimitive()");
            return m.call(env, owner).z;
        }

        bool jni_class::isSynthetic(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("boolean java.lang.Class#isSynthetic()");
            return m.call(env, owner).z;
        }

        jobject jni_class::getClassLoader(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.ClassLoader "
                    "java.lang.Class#getClassLoader()");
            return m.call(env, owner).l;
        }

        jstring jni_class::getCanonicalName(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.String java.lang.Class#getCanonicalName()");
            return static_cast<jstring>(m.call(env, owner).l);
        }

        jstring jni_class::getName(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.String java.lang.Class#getName()");
            return static_cast<jstring>(m.call(env, owner).l);
        }

        jstring jni_class::getSimpleName(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.String java.lang.Class#getSimpleName()");
            return static_cast<jstring>(m.call(env, owner).l);
        }

        jobject jni_class::getPackage(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Package java.lang.Class#getPackage()");
            return m.call(env, owner).l;
        }

        jclass jni_class::getComponentType(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Class java.lang.Class#getComponentType()");
            return static_cast<jclass>(m.call(env, owner).l);
        }

        jobjectArray jni_class::getEnumConstants(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Object[] "
                    "java.lang.Class#getEnumConstants()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobjectArray jni_class::getInterfaces(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Class[] java.lang.Class#getInterfaces()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobjectArray jni_class::getDeclaredClasses(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.Class[] java.lang.Class#getDeclaredClasses()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jclass jni_class::getEnclosingClass(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Class java.lang.Class#getEnclosingClass()");
            return static_cast<jclass>(m.call(env, owner).l);
        }

        jclass jni_class::getDeclaringClass(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.Class java.lang.Class#getDeclaringClass()");
            return static_cast<jclass>(m.call(env, owner).l);
        }

        jobject jni_class::getAnnotation(const jclass owner,
                const jclass annotation) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.Object java.lang.Class#getAnnotation(java.lang.Class annotationClass)");
            return m.call(env, owner, annotation).l;
        }

        bool jni_class::isAnnotationPresent(const jclass owner,
                const jclass annotation) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "boolean java.lang.Class#isAnnotationPresent(java.lang.Class annotationClass)");
            return m.call(env, owner, annotation).z;
        }

        jobjectArray jni_class::getAnnotations(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.annotation.Annotation[] java.lang.Class#getAnnotations()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobjectArray jni_class::getDeclaredAnnotations(
                const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.annotation.Annotation[] java.lang.Class#getDeclaredAnnotations()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getConstructor(const jclass owner,
                const jobjectArray param_classes) const {
            if (owner == 0) {
                return false;
            }

            jni_method m("java.lang.reflect.Constructor java.lang.Class"
                    "#getConstructor(java.lang.Class... parameterTypes)");
            return m.call(env, owner, param_classes).l;
        }

        jobjectArray jni_class::getConstructors(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Constructor[] java.lang.Class#getConstructors()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getDeclaredConstructor(const jclass owner,
                const jobjectArray param_classes) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Constructor java.lang.Class"
                            "#getDeclaredConstructor(java.lang.Class... parameterTypes)");
            return m.call(env, owner, param_classes).l;
        }

        jobjectArray jni_class::getDeclaredConstructors(
                const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Constructor[] java.lang.Class#getDeclaredConstructors()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getEnclosingConstructor(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Constructor java.lang.Class#getEnclosingConstructor()");
            return m.call(env, owner).l;
        }

        jobject jni_class::getDeclaredField(const jclass owner,
                const std::string & name) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Field java.lang.Class#getDeclaredField(java.lang.String name)");
            return m.call(env, owner, env->NewStringUTF(name.c_str())).l;
        }

        jobjectArray jni_class::getDeclaredFields(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Field[] java.lang.Class#getDeclaredFields()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getField(const jclass owner,
                const std::string & name) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Field java.lang.Class#getField(java.lang.String name)");
            return m.call(env, owner, env->NewStringUTF(name.c_str())).l;
        }

        jobjectArray jni_class::getFields(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Field[] java.lang.Class#getFields()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getDeclaredMethod(const jclass owner,
                const std::string & name,
                const jobjectArray param_classes) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Method java.lang.Class"
                            "#getDeclaredMethod(java.lang.String name, java.lang.Class... parameterTypes)");
            return m.call(env, owner, env->NewStringUTF(name.c_str()),
                    param_classes).l;
        }

        jobjectArray jni_class::getDeclaredMethods(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Method[] java.lang.Class#getDeclaredMethods()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jobject jni_class::getEnclosingMethod(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Method java.lang.Class#getEnclosingMethod()");
            return m.call(env, owner).l;
        }

        jobject jni_class::getMethod(const jclass owner,
                const std::string & name,
                const jobjectArray param_classes) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Method java.lang.Class"
                            "#getMethod(java.lang.String name, java.lang.Class... parameterTypes)");
            return m.call(env, owner, env->NewStringUTF(name.c_str()),
                    param_classes).l;
        }

        jobjectArray jni_class::getMethods(const jclass owner) const {
            if (owner == 0) {
                return false;
            }

            jni_method m(
                    "java.lang.reflect.Method[] java.lang.Class#getMethods()");
            return static_cast<jobjectArray>(m.call(env, owner).l);
        }

        jmethodID jni_class::fromReflectedMethod(const jobject method) const {
            if (method == 0) {
                return 0;
            }

            return env->FromReflectedMethod(method);
        }

    } /* namespace jnis */
} /* namespace clark */
