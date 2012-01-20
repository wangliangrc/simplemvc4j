/*
 * JNIHelper.h
 *
 *  Created on: 2012-1-9
 *      Author: clark
 */

#ifndef JNIHELPER_H_
#define JNIHELPER_H_

#include <jni.h>

namespace su {

class JNIHelper {
public:
    JNIHelper();
    JNIHelper(const JNIHelper& other);
    virtual ~JNIHelper();
    JNIHelper& operator=(const JNIHelper& other);

    JNIEnv* getEnv() const;

    jint throwException(const jthrowable throwable) const;
    jint throwNewException(const jclass clazz, const char *msg) const;
    jboolean exceptionCheck() const;
    void exceptionClear() const;
    void exceptionDescribe() const;
    jthrowable exceptionOccurred() const;
    void fatalError(const char *msg) const;

    void throwRuntime(const char *msg) const;
    void throwIllegalArgument(const char *msg) const;
    void throwIllegalState(const char *msg) const;
    void throwNullPointer(const char *msg) const;

    jboolean checkNull(const void *p, const char *msg) const;
    jboolean checkArgument(const int& b, const char *msg) const;
    jboolean checkState(const int& b, const char *msg) const;

    jclass findClass(const char *classname) const;
    jclass findClass(const jobject obj) const;

    jmethodID findConstructor(const jclass clazz, const char *signture) const;
    jmethodID findMethod(const jclass clazz, const char *name, const char *signture) const;
    jmethodID findStaticMethod(const jclass clazz, const char *name, const char *signture) const;

    jmethodID findConstructor(const char *className, const char *signture) const;
    jmethodID findMethod(const char *className, const char *name, const char *signture) const;
    jmethodID findStaticMethod(const char *className, const char *name, const char *signture) const;

    jfieldID findField(const jclass clazz, const char *name, const char *signture) const;
    jfieldID findStaticField(const jclass clazz, const char *name, const char *signture) const;

    jfieldID findField(const char *className, const char *name, const char *signture) const;
    jfieldID findStaticField(const char *className, const char *name, const char *signture) const;

    void callVoidMethod(const jobject obj, const jmethodID method, ...) const;
    void callNonvirtualVoidMethod(const jobject obj, const jmethodID method, ...) const;
    void callStaticVoidMethod(const jclass clazz, const jmethodID method, ...) const;

    jobject callObjectMethod(const jobject obj, const jmethodID method, ...) const;
    jobject callNonvirtualObjectMethod(const jobject obj, const jmethodID method, ...) const;
    jobject callStaticObjectMethod(const jclass clazz, const jmethodID method, ...) const;

    jboolean callBooleanMethod(const jobject obj, const jmethodID method, ...) const;
    jboolean callNonvirtualBooleanMethod(const jobject obj, const jmethodID method, ...) const;
    jboolean callStaticBooleanMethod(const jclass clazz, const jmethodID method, ...) const;

    jbyte callByteMethod(const jobject obj, const jmethodID method, ...) const;
    jbyte callNonvirtualByteMethod(const jobject obj, const jmethodID method, ...) const;
    jbyte callStaticByteMethod(const jclass clazz, const jmethodID method, ...) const;

    jchar callCharMethod(const jobject obj, const jmethodID method, ...) const;
    jchar callNonvirtualCharMethod(const jobject obj, const jmethodID method, ...) const;
    jchar callStaticCharMethod(const jclass clazz, const jmethodID method, ...) const;

    jshort callShortMethod(const jobject obj, const jmethodID method, ...) const;
    jshort callNonvirtualShortMethod(const jobject obj, const jmethodID method, ...) const;
    jshort callStaticShortMethod(const jclass clazz, const jmethodID method, ...) const;

    jint callIntMethod(const jobject obj, const jmethodID method, ...) const;
    jint callNonvirtualIntMethod(const jobject obj, const jmethodID method, ...) const;
    jint callStaticIntMethod(const jclass clazz, const jmethodID method, ...) const;

    jlong callLongMethod(const jobject obj, const jmethodID method, ...) const;
    jlong callNonvirtualLongMethod(const jobject obj, const jmethodID method, ...) const;
    jlong callStaticLongMethod(const jclass clazz, const jmethodID method, ...) const;

    jfloat callFloatMethod(const jobject obj, const jmethodID method, ...) const;
    jfloat callNonvirtualFloatMethod(const jobject obj, const jmethodID method, ...) const;
    jfloat callStaticFloatMethod(const jclass clazz, const jmethodID method, ...) const;

    jdouble callDoubleMethod(const jobject obj, const jmethodID method, ...) const;
    jdouble callNonvirtualDoubleMethod(const jobject obj, const jmethodID method, ...) const;
    jdouble callStaticDoubleMethod(const jclass clazz, const jmethodID method, ...) const;

    void callVoidMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jobject callObjectMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jboolean callBooleanMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jbyte callByteMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jchar callCharMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jshort callShortMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jint callIntMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jlong callLongMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jfloat callFloatMethod(const jobject obj, const char *name, const char *signture, ...) const;
    jdouble callDoubleMethod(const jobject obj, const char *name, const char *signture, ...) const;

    void callStaticVoidMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jobject callStaticObjectMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jboolean callStaticBooleanMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jbyte callStaticByteMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jchar callStaticCharMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jshort callStaticShortMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jint callStaticIntMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jlong callStaticLongMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jfloat callStaticFloatMethod(const jclass clazz, const char *name, const char *signture, ...) const;
    jdouble callStaticDoubleMethod(const jclass clazz, const char *name, const char *signture, ...) const;

    void callStaticVoidMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jobject callStaticObjectMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jboolean callStaticBooleanMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jbyte callStaticByteMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jchar callStaticCharMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jshort callStaticShortMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jint callStaticIntMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jlong callStaticLongMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jfloat callStaticFloatMethod(const char *clazz, const char *name, const char *signture, ...) const;
    jdouble callStaticDoubleMethod(const char *clazz, const char *name, const char *signture, ...) const;

    jobject newObject(const jclass clazz, const jmethodID method, ...) const;
    jobject newObject(const char *classname, const char *methodsignture, ...) const;

    jsize getArrayLength(const jarray array) const;
    jobjectArray newObjectArray(const jsize& length, const jclass elementType, const jobject initObj) const;
    jobjectArray newObjectArray(const jsize& length, const char *classname, const jobject initObj) const;
    void setObjectArrayElement(const jobjectArray array, const jsize& index, const jobject value) const;
    jobject getObjectArrayElement(const jobjectArray array, const jsize& index) const;

    jobject getObjectField(const jobject obj, const jfieldID field) const;
    jboolean getBooleanField(const jobject obj, const jfieldID field) const;
    jbyte getByteField(const jobject obj, const jfieldID field) const;
    jchar getCharField(const jobject obj, const jfieldID field) const;
    jshort getShortField(const jobject obj, const jfieldID field) const;
    jint getIntField(const jobject obj, const jfieldID field) const;
    jlong getLongField(const jobject obj, const jfieldID field) const;
    jfloat getFloatField(const jobject obj, const jfieldID field) const;
    jdouble getDoubleField(const jobject obj, const jfieldID field) const;

    jobject getObjectField(const jobject obj, const char *name, const char *signture) const;
    jboolean getBooleanField(const jobject obj, const char *name, const char *signture) const;
    jbyte getByteField(const jobject obj, const char *name, const char *signture) const;
    jchar getCharField(const jobject obj, const char *name, const char *signture) const;
    jshort getShortField(const jobject obj, const char *name, const char *signture) const;
    jint getIntField(const jobject obj, const char *name, const char *signture) const;
    jlong getLongField(const jobject obj, const char *name, const char *signture) const;
    jfloat getFloatField(const jobject obj, const char *name, const char *signture) const;
    jdouble getDoubleField(const jobject obj, const char *name, const char *signture) const;

    jobject getStaticObjectField(const jclass clazz, const jfieldID field) const;
    jboolean getStaticBooleanField(const jclass clazz, const jfieldID field) const;
    jbyte getStaticByteField(const jclass clazz, const jfieldID field) const;
    jchar getStaticCharField(const jclass clazz, const jfieldID field) const;
    jshort getStaticShortField(const jclass clazz, const jfieldID field) const;
    jint getStaticIntField(const jclass clazz, const jfieldID field) const;
    jlong getStaticLongField(const jclass clazz, const jfieldID field) const;
    jfloat getStaticFloatField(const jclass clazz, const jfieldID field) const;
    jdouble getStaticDoubleField(const jclass clazz, const jfieldID field) const;

    jobject getStaticObjectField(const char *classname, const char *fieldname,
            const char *fieldsignture) const;
    jboolean getStaticBooleanField(const char *classname, const char *fieldname,
            const char *fieldsignture) const;
    jbyte getStaticByteField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jchar getStaticCharField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jshort getStaticShortField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jint getStaticIntField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jlong getStaticLongField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jfloat getStaticFloatField(const char *classname, const char *fieldname, const char *fieldsignture) const;
    jdouble getStaticDoubleField(const char *classname, const char *fieldname,
            const char *fieldsignture) const;

    void setObjectField(const jobject obj, const jfieldID field, const jobject value) const;
    void setBooleanField(const jobject obj, const jfieldID field, const jboolean& value) const;
    void setByteField(const jobject obj, const jfieldID field, const jbyte& value) const;
    void setCharField(const jobject obj, const jfieldID field, const jchar& value) const;
    void setShortField(const jobject obj, const jfieldID field, const jshort& value) const;
    void setIntField(const jobject obj, const jfieldID field, const jint& value) const;
    void setLongField(const jobject obj, const jfieldID field, const jlong& value) const;
    void setFloatField(const jobject obj, const jfieldID field, const jfloat& value) const;
    void setDoubleField(const jobject obj, const jfieldID field, const jdouble& value) const;

    void setObjectField(const jobject obj, const char *name, const char *signture, const jobject value) const;
    void setBooleanField(const jobject obj, const char *name, const char *signture,
            const jboolean& value) const;
    void setByteField(const jobject obj, const char *name, const char *signture, const jbyte& value) const;
    void setCharField(const jobject obj, const char *name, const char *signture, const jchar& value) const;
    void setShortField(const jobject obj, const char *name, const char *signture, const jshort& value) const;
    void setIntField(const jobject obj, const char *name, const char *signture, const jint& value) const;
    void setLongField(const jobject obj, const char *name, const char *signture, const jlong& value) const;
    void setFloatField(const jobject obj, const char *name, const char *signture, const jfloat& value) const;
    void setDoubleField(const jobject obj, const char *name, const char *signture,
            const jdouble& value) const;

    void setStaticObjectField(const jclass clazz, const jfieldID field, const jobject value) const;
    void setStaticBooleanField(const jclass clazz, const jfieldID field, const jboolean& value) const;
    void setStaticByteField(const jclass clazz, const jfieldID field, const jbyte& value) const;
    void setStaticCharField(const jclass clazz, const jfieldID field, const jchar& value) const;
    void setStaticShortField(const jclass clazz, const jfieldID field, const jshort& value) const;
    void setStaticIntField(const jclass clazz, const jfieldID field, const jint& value) const;
    void setStaticLongField(const jclass clazz, const jfieldID field, const jlong& value) const;
    void setStaticFloatField(const jclass clazz, const jfieldID field, const jfloat& value) const;
    void setStaticDoubleField(const jclass clazz, const jfieldID field, const jdouble& value) const;

    void setStaticObjectField(const char *classname, const char *fieldname, const char *fieldsignture,
            jobject value) const;
    void setStaticBooleanField(const char *classname, const char *fieldname, const char *fieldsignture,
            jboolean value) const;
    void setStaticByteField(const char *classname, const char *fieldname, const char *fieldsignture,
            jbyte value) const;
    void setStaticCharField(const char *classname, const char *fieldname, const char *fieldsignture,
            jchar value) const;
    void setStaticShortField(const char *classname, const char *fieldname, const char *fieldsignture,
            jshort value) const;
    void setStaticIntField(const char *classname, const char *fieldname, const char *fieldsignture,
            jint value) const;
    void setStaticLongField(const char *classname, const char *fieldname, const char *fieldsignture,
            jlong value) const;
    void setStaticFloatField(const char *classname, const char *fieldname, const char *fieldsignture,
            jfloat value) const;
    void setStaticDoubleField(const char *classname, const char *fieldname, const char *fieldsignture,
            jdouble value) const;

    jstring newStringUTF(const char *s) const;
    jstring newString(const jchar *s, const jsize len) const;
    const char* getStringUTFChars(const jstring s) const;
    const jchar* getStringChars(const jstring s) const;
    void releaseStringChars(const jstring s, const jchar *buf) const;
    void releaseStringUTFChars(const jstring s, const char *buf) const;
    // 返回 Java 字符串的长度
    jsize getStringLength(const jstring s) const;
    // 返回 C 字符串长度
    jsize getStringUTFLength(const jstring s) const;

    // 将 Java 反射中的 Field 类的实例转化为 jfieldID 类型
    jfieldID fromReflectedField(const jobject field) const;
    // 将 Java 反射中的 Method 类的实例转化为 jmethodID 类型
    jmethodID fromReflectedMethod(const jobject method) const;
    // 将 jfieldID 转换为 Java 反射的 Field 类的实例
    jobject toReflectedField(const jclass clazz, const jfieldID field, const jboolean& isStatic) const;
    jobject toReflectedField(const char *clazz, const char *fieldName, const char *fieldSignture,
            const jboolean& isStatic) const;
    // 将 jmethodID 转换为 Java 反射的 Method 类的实例
    jobject toReflectedMethod(const jclass clazz, const jmethodID method, const jboolean& isStatic) const;
    jobject toReflectedMethod(const char *clazz, const char *methodName, const char *methodSignture,
            const jboolean& isStatic) const;

    jclass getSuperclass(const jclass clazz) const;
    // 判断 clazz1 的实例是否可以安全地转换为 clazz2 类型
    jboolean isAssignableFrom(const jclass clazz1, const jclass clazz2) const;
    // 判断 obj 是否为 clazz 的实例
    jboolean isInstanceOf(const jobject obj, const jclass clazz) const;
    // 判断 obj1 和 obj2 是否为同一实例
    jboolean isSameObject(const jobject obj1, const jobject obj2) const;

    // “当前”执行线程试图获取 obj 的锁（相当与 Java 语言中试图进入同步块）
    jint lock(const jobject obj) const;
    // “当前”执行线程释放 obj 的锁（相当与 Java 语言中从同步块中出去）
    jint unlock(const jobject obj) const;

    const JNIHelper& print(const char *s) const;
    const JNIHelper& print(jobject obj) const;
    const JNIHelper& println(const char *s) const;
    const JNIHelper& println(jobject obj) const;
    const JNIHelper& println() const;
    const JNIHelper& error(const char *s) const;
    const JNIHelper& error(jobject obj) const;
    const JNIHelper& errorln(const char *s) const;
    const JNIHelper& errorln(jobject obj) const;
    const JNIHelper& errorln() const;

    jstring toString(jobject obj) const;
    jstring intern(jstring s) const;

//    jobject newStringBuilder() const;
//    jobject appendStringBuilder(jobject stringbuilder, jstring s) const;
//    jobject appendStringBuilder(jobject stringbuilder, const char *s) const;
//    jobject appendStringBuilder(jobject stringbuilder, jobject o) const;
//    jobject appendStringBuilder(jobject stringbuilder, jbyte b) const;
//    jobject appendStringBuilder(jobject stringbuilder, jboolean b) const;
//    jobject appendStringBuilder(jobject stringbuilder, jchar ch) const;
//    jobject appendStringBuilder(jobject stringbuilder, jcharArray chs) const;
//    jobject appendStringBuilder(jobject stringbuilder, jdouble d) const;
//    jobject appendStringBuilder(jobject stringbuilder, jfloat f) const;
//    jobject appendStringBuilder(jobject stringbuilder, jint i) const;
//    jstring builderToString(jobject stringbuilder) const;

private:
    JNIHelper(JNIEnv *env);

    JNIEnv * env;
};

} // namespace su

#endif /* JNIHELPER_H_ */
