/*
 * JStringBuilder.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#include "JStringBuilder.h"

namespace su {

static jobject appendBuilder(const jobject builder, const jstring s) {
    JNIHelper p;
    p.callObjectMethod(builder, "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", s);
    return builder;
}

JStringBuilder operator+(const JString& s, const JStringBuilder& sb) {
    JStringBuilder res(s);
    return res += sb;
}

JStringBuilder operator+(const char *s, const JStringBuilder& sb) {
    JStringBuilder res(s);
    return res += sb;
}

JStringBuilder operator+(const jobject obj, const JStringBuilder& sb) {
    JStringBuilder res(obj);
    return res += sb;
}

JStringBuilder operator+(const jstring s, const JStringBuilder& sb) {
    JStringBuilder res(s);
    return res += sb;
}

JStringBuilder operator+(const jboolean& b, const JStringBuilder& sb) {
    JStringBuilder res(b);
    return res += sb;
}

JStringBuilder operator+(const jbyte& b, const JStringBuilder& sb) {
    JStringBuilder res(b);
    return res += sb;
}

JStringBuilder operator+(const jchar& ch, const JStringBuilder& sb) {
    JStringBuilder res(ch);
    return res += sb;
}

JStringBuilder operator+(const jshort& sh, const JStringBuilder& sb) {
    JStringBuilder res(sh);
    return res += sb;
}

JStringBuilder operator+(const jint& i, const JStringBuilder& sb) {
    JStringBuilder res(i);
    return res += sb;
}

JStringBuilder operator+(const jlong& l, const JStringBuilder& sb) {
    JStringBuilder res(l);
    return res += sb;
}

JStringBuilder operator+(const jfloat& f, const JStringBuilder& sb) {
    JStringBuilder res(f);
    return res += sb;
}

JStringBuilder operator+(const jdouble& d, const JStringBuilder& sb) {
    JStringBuilder res(d);
    return res += sb;
}

std::ostream& operator<<(std::ostream& out, const JStringBuilder& builder) {
    out << builder.toString();
    return out;
}

JStringBuilder::JStringBuilder() :
        JObject() {
    obj = pj.newObject("java/lang/StringBuilder", "()V");
}

JStringBuilder::JStringBuilder(const JStringBuilder& other) :
        JObject() {
    obj = other.obj;
}

JStringBuilder::JStringBuilder(const JString& s) :
        JObject() {
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", s.toString());
}

JStringBuilder::JStringBuilder(const char *s) :
        JObject() {
    JString str(s);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jobject obj) :
        JObject() {
    JString str(obj);
    this->obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jstring s) :
        JObject() {
    JString str(s);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jboolean& b) :
        JObject() {
    JString str(b);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jbyte& b) :
        JObject() {
    JString str(b);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jchar& ch) :
        JObject() {
    JString str(ch);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jshort& sh) :
        JObject() {
    JString str(sh);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jint& i) :
        JObject() {
    JString str(i);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jlong& l) :
        JObject() {
    JString str(l);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jfloat& f) :
        JObject() {
    JString str(f);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder::JStringBuilder(const jdouble& d) :
        JObject() {
    JString str(d);
    obj = pj.newObject("java/lang/StringBuilder", "(Ljava/lang/String;)V", str.toString());
}

JStringBuilder& JStringBuilder::operator=(const JStringBuilder& other) {
    obj = other.obj;
    return *this;
}

JStringBuilder::~JStringBuilder() {
}

JStringBuilder& JStringBuilder::operator+=(const JStringBuilder& s) {
    appendBuilder(obj, s.toString());
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const JString& s) {
    appendBuilder(obj, s.toString());
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const char *s) {
    if (s == NULL) {
        return operator+=("NULL");
    } else {
        return operator+=(pj.newStringUTF(s));
    }
}

JStringBuilder& JStringBuilder::operator+=(const jobject obj) {
    appendBuilder(this->obj, pj.toString(obj));
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jstring s) {
    if (s == NULL) {
        return operator+=("NULL");
    } else {
        appendBuilder(obj, s);
    }
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jboolean& b) {
    pj.callObjectMethod(obj, "append", "(Z)Ljava/lang/StringBuilder;", b);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jbyte& b) {
    pj.callObjectMethod(obj, "append", "(I)Ljava/lang/StringBuilder;", b);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jchar& ch) {
    pj.callObjectMethod(obj, "append", "(C)Ljava/lang/StringBuilder;", ch);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jshort& sh) {
    pj.callObjectMethod(obj, "append", "(I)Ljava/lang/StringBuilder;", sh);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jint& i) {
    pj.callObjectMethod(obj, "append", "(I)Ljava/lang/StringBuilder;", i);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jlong& l) {
    pj.callObjectMethod(obj, "append", "(J)Ljava/lang/StringBuilder;", l);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jfloat& f) {
    pj.callObjectMethod(obj, "append", "(F)Ljava/lang/StringBuilder;", f);
    return *this;
}

JStringBuilder& JStringBuilder::operator+=(const jdouble& d) {
    pj.callObjectMethod(obj, "append", "(D)Ljava/lang/StringBuilder;", d);
    return *this;
}

JStringBuilder& JStringBuilder::clear() {
    pj.callObjectMethod(obj, "setLength", "(I)V", 0);
    return *this;
}

jint JStringBuilder::length() const {
    return pj.callIntMethod(obj, "length", "()I");
}

} /* namespace su */
