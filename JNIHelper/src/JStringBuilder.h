/*
 * JStringBuilder.h
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#ifndef JSTRINGBUILDER_H_
#define JSTRINGBUILDER_H_

#include "JNIHelper.h"
#include "JString.h"
#include "JObject.h"
#include <iostream>

namespace su {

class JStringBuilder : public JObject {
    friend JStringBuilder operator+(const JString& s, const JStringBuilder& sb);
    friend JStringBuilder operator+(const char *s, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jobject obj, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jstring s, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jboolean& b, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jbyte& b, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jchar& ch, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jshort& sh, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jint& i, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jlong& l, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jfloat& f, const JStringBuilder& sb);
    friend JStringBuilder operator+(const jdouble& d, const JStringBuilder& sb);

    friend std::ostream& operator<<(std::ostream& out, const JStringBuilder& builder);

public:
    JStringBuilder();
    JStringBuilder(const JStringBuilder& other);
    JStringBuilder(const JString& s);
    JStringBuilder(const char *s);
    JStringBuilder(const jobject obj);
    JStringBuilder(const jstring s);
    JStringBuilder(const jboolean& b);
    JStringBuilder(const jbyte& b);
    JStringBuilder(const jchar& ch);
    JStringBuilder(const jshort& sh);
    JStringBuilder(const jint& i);
    JStringBuilder(const jlong& l);
    JStringBuilder(const jfloat& f);
    JStringBuilder(const jdouble& d);

    JStringBuilder& operator=(const JStringBuilder& other);
    virtual ~JStringBuilder();

    JStringBuilder& operator+=(const JStringBuilder& s);
    JStringBuilder& operator+=(const JString& s);
    JStringBuilder& operator+=(const char *s);
    JStringBuilder& operator+=(const jobject obj);
    JStringBuilder& operator+=(const jstring s);
    JStringBuilder& operator+=(const jboolean& b);
    JStringBuilder& operator+=(const jbyte& b);
    JStringBuilder& operator+=(const jchar& ch);
    JStringBuilder& operator+=(const jshort& sh);
    JStringBuilder& operator+=(const jint& i);
    JStringBuilder& operator+=(const jlong& l);
    JStringBuilder& operator+=(const jfloat& f);
    JStringBuilder& operator+=(const jdouble& d);

    JStringBuilder& clear();
    jint length() const;

};

} /* namespace su */
#endif /* JSTRINGBUILDER_H_ */
