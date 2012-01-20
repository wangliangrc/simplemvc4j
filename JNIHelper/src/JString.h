/*
 * String.h
 *
 *  Created on: Jan 11, 2012
 *      Author: clark
 */

#ifndef STRING_H_
#define STRING_H_

#include <jni.h>
#include <iostream>
#include "JNIHelper.h"
#include "JObject.h"

namespace su {

typedef void (*cstringAction)(const char *);

class JString : public JObject {
    friend JString operator+(const jstring s1, const JString& s2);
    friend JString operator+(const char *s1, const JString& s2);
    friend JString operator+(const jboolean& b, const JString& s2);
    friend JString operator+(const jbyte& b, const JString& s2);
    friend JString operator+(const jchar& ch, const JString& s2);
    friend JString operator+(const jshort& sh, const JString& s2);
    friend JString operator+(const jint& i, const JString& s2);
    friend JString operator+(const jlong& l, const JString& s2);
    friend JString operator+(const jfloat& f, const JString& s2);
    friend JString operator+(const jdouble& d, const JString& s2);
    friend JString operator+(const jobject obj, const JString& s2);

    friend std::ostream& operator<<(std::ostream& out, const JString& s);
    friend std::ostream& operator<<(std::ostream& out, const JString* s);

public:
    JString();
    JString(const char *s);
    JString(const jbyteArray buf);
    JString(const jbyteArray buf, const char *charsetName);
    JString(const jcharArray buf);
    JString(const jstring s);

    JString(const jboolean& b);
    JString(const jbyte& b);
    JString(const jchar& ch);
    JString(const jshort& sh);
    JString(const jint& i);
    JString(const jlong& l);
    JString(const jfloat& f);
    JString(const jdouble& d);
    JString(const jobject obj);

    JString(const JString& s);
    virtual ~JString();

    JString& operator=(const JString& s);
    jchar operator[](const int& index) const;

    JString operator+(const JString& s) const;
    JString operator+(const jstring s) const;
    JString operator+(const char *s) const;
    JString operator+(const jboolean& b) const;
    JString operator+(const jbyte& b) const;
    JString operator+(const jchar& ch) const;
    JString operator+(const jshort& sh) const;
    JString operator+(const jint& i) const;
    JString operator+(const jlong& l) const;
    JString operator+(const jfloat& f) const;
    JString operator+(const jdouble& d) const;
    JString operator+(const jobject obj) const;

    JString& reset(const char *newStr);
    JString& reset(const JString& newStr);
    JString& reset(const jstring newStr);
    JString& reset(const jboolean& newStr);
    JString& reset(const jbyte& newStr);
    JString& reset(const jchar& newStr);
    JString& reset(const jshort& newStr);
    JString& reset(const jint& newStr);
    JString& reset(const jlong& newStr);
    JString& reset(const jfloat& newStr);
    JString& reset(const jdouble& newStr);
    JString& reset(const jobject& newStr);

    JString& clear();
    const char* getCString() const;
    void releaseCString(const char *s) const;

    void doAction(const cstringAction act) const;

    JString trim() const;
    jint length() const;
    jboolean isEmpty() const;
    jchar charAt(const jint index) const;
    jbyteArray getBytes(const char *charsetName) const;
    jbyteArray getBytes() const;

    jboolean equalsIgnoreCase(const JString& s) const;
    jboolean equalsIgnoreCase(const jstring s) const;
    jboolean equalsIgnoreCase(const char *s) const;

    jboolean equals(const jobject s) const;
    jboolean equals(const char *s) const;

    jint compareTo(const JString& s) const;
    jint compareTo(const jstring s) const;
    jint compareTo(const char *s) const;

    jint compareToIgnoreCase(const JString& s) const;
    jint compareToIgnoreCase(const jstring s) const;
    jint compareToIgnoreCase(const char *s) const;

    jboolean startsWith(const JString& s) const;
    jboolean startsWith(const jstring s) const;
    jboolean startsWith(const char *s) const;

    jboolean endsWith(const JString& s) const;
    jboolean endsWith(const jstring s) const;
    jboolean endsWith(const char *s) const;

    jint indexOf(const jchar ch) const;
    jint indexOf(const JString& s) const;
    jint indexOf(const jstring s) const;
    jint indexOf(const char *s) const;

    jint lastIndexOf(const jchar ch) const;
    jint lastIndexOf(const JString& s) const;
    jint lastIndexOf(const jstring s) const;
    jint lastIndexOf(const char *s) const;

    JString substring(const jint beginIndex) const;
    JString substring(const jint beginIndex, const jint endIndex) const;

    JString concat(const JString& s) const;
    JString concat(const jstring s) const;
    JString concat(const char *s) const;

    JString replace(const jchar oldCh, const jchar newCh) const;
    JString replace(const JString& ojdStr, const JString& newStr) const;
    JString replace(const jstring ojdStr, const jstring newStr) const;
    JString replace(const char *ojdStr, const char *newStr) const;

    JString replaceAll(const JString& ojdStr, const JString& newStr) const;
    JString replaceAll(const jstring ojdStr, const jstring newStr) const;
    JString replaceAll(const char *ojdStr, const char *newStr) const;

    jboolean contains(const JString& s) const;
    jboolean contains(const jstring s) const;
    jboolean contains(const char *s) const;

    jobjectArray split(const JString& s) const;
    jobjectArray split(const jstring s) const;
    jobjectArray split(const char *s) const;

    JString toLowerCase() const;
    JString toUpperCase() const;

    jcharArray toCharArray() const;

    JString& intern();
};

} // namespace su

#endif /* STRING_H_ */
