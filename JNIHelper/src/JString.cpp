/*
 * String.cpp
 *
 *  Created on: Jan 11, 2012
 *      Author: clark
 */

#include "JString.h"
#include "global.h"

namespace su {

JString operator+(const jstring s1, const JString& s2) {
    if (s1 == NULL) {
        return operator+("", s2);
    }
    return operator+((jobject) s1, s2);
}

JString operator+(const char *s1, const JString& s2) {
    if (s1 == NULL) {
        return operator+("", s2);
    }

    JString res(s1);
    return res + s2;
}

JString operator+(const jboolean& b, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(Z)Ljava/lang/String;",
                    b));
    return res + s2;
}

JString operator+(const jbyte& b, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                    b));
    return res + s2;
}

JString operator+(const jchar& ch, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(C)Ljava/lang/String;",
                    ch));
    return res + s2;
}

JString operator+(const jshort& sh, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                    sh));
    return res + s2;
}

JString operator+(const jint& i, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;",
                    i));
    return res + s2;
}

JString operator+(const jlong& l, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(J)Ljava/lang/String;",
                    l));
    return res + s2;
}

JString operator+(const jfloat& f, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(F)Ljava/lang/String;",
                    f));
    return res + s2;
}

JString operator+(const jdouble& d, const JString& s2) {
    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf", "(D)Ljava/lang/String;",
                    d));
    return res + s2;
}

JString operator+(const jobject obj, const JString& s2) {
    if (obj == NULL) {
        return operator+("", s2);
    }

    JString res(
            (jstring) s2.pj.callStaticObjectMethod("java/lang/String", "valueOf",
                    "(Ljava/lang/Object;)Ljava/lang/String;", obj));
    return res + s2;
}

std::ostream& operator<<(std::ostream& out, const JString& s) {
    const char *cs = s.getCString();
    out << cs;
    s.releaseCString(cs);
    return out;
}

std::ostream& operator<<(std::ostream& out, const JString* s) {
    if (s == NULL) {
        out << "NULL";
    } else {
        operator<<(out, *s);
    }
    return out;
}

JString::JString() :
        JObject() {
    obj = pj.newStringUTF("");
}

JString::JString(const char *s) :
        JObject() {
    if (s == NULL) {
        obj = pj.newStringUTF("");
    } else {
        obj = pj.newStringUTF(s);
    }
}

JString::JString(const jbyteArray buf) :
        JObject() {
    if (buf == NULL) {
        obj = pj.newStringUTF("");
    } else {
        obj = (jstring) pj.newObject("java/lang/String", "([B)V", buf);
    }
}

JString::JString(const jbyteArray buf, const char *charsetName) :
        JObject() {
    if (buf == NULL) {
        obj = pj.newStringUTF("");
    } else if (charsetName == NULL) {
        JString(buf);
    } else {
        obj = (jstring) pj.newObject("java/lang/String", "([BLjava/lang/String;)V", buf,
                pj.newStringUTF(charsetName));
    }
}

JString::JString(const jcharArray buf) :
        JObject() {
    if (buf == NULL) {
        obj = pj.newStringUTF("");
    } else {
        obj = (jstring) pj.newObject("java/lang/String", "([C)V", buf);
    }
}

JString::JString(const jstring s) :
        JObject() {
    if (s == NULL) {
        obj = pj.newStringUTF("");
    } else {
        obj = s;
    }
}

JString::JString(const jboolean& b) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(Z)Ljava/lang/String;", b);
}

JString::JString(const jbyte& b) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;", b);
}

JString::JString(const jchar& ch) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(C)Ljava/lang/String;", ch);
}

JString::JString(const jshort& sh) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;", sh);
}

JString::JString(const jint& i) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(I)Ljava/lang/String;", i);
}

JString::JString(const jlong& l) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(J)Ljava/lang/String;", l);
}

JString::JString(const jfloat& f) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(F)Ljava/lang/String;", f);
}

JString::JString(const jdouble& d) :
        JObject() {
    obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf", "(D)Ljava/lang/String;", d);
}

JString::JString(const jobject obj) :
        JObject() {
    if (obj == NULL) {
        this->obj = pj.newStringUTF("");
    } else {
        this->obj = (jstring) pj.callStaticObjectMethod("java/lang/String", "valueOf",
                "(Ljava/lang/Object;)Ljava/lang/String;", obj);
    }
}

JString::JString(const JString& s) :
        JObject() {
    obj = s.obj;
}

JString::~JString() {
}

JString& JString::operator=(const JString& s) {
    pj = s.pj;
    obj = s.obj;
    return *this;
}

jchar JString::operator[](const int& index) const {
    return charAt(index);
}

JString JString::operator+(const JString& s) const {
    return concat(s);
}

JString JString::operator+(const jstring s) const {
    if (s == NULL) {
        return concat("NULL");
    }
    return concat(s);
}

JString JString::operator+(const char *s) const {
    if (s == NULL) {
        return concat("NULL");
    }
    return concat(s);
}

JString JString::operator+(const jboolean& b) const {
    if (b) {
        return concat("true");
    } else {
        return concat("false");
    }
}

JString JString::operator+(const jbyte& b) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", b));
}

JString JString::operator+(const jchar& ch) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(C)Ljava/lang/String;", ch));
}

JString JString::operator+(const jshort& sh) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", sh));
}

JString JString::operator+(const jint& i) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", i));
}

JString JString::operator+(const jlong& l) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(J)Ljava/lang/String;", l));
}

JString JString::operator+(const jfloat& f) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(F)Ljava/lang/String;", f));
}

JString JString::operator+(const jdouble& d) const {
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(D)Ljava/lang/String;", d));
}

JString JString::operator+(const jobject obj) const {
    if (obj == NULL) {
        concat("NULL");
    }
    return concat(
            (jstring) pj.callStaticObjectMethod(pj.findClass(this->obj), "valueOf",
                    "(Ljava/lang/Object;)Ljava/lang/String;", obj));
}

JString& JString::reset(const char *newStr) {
    if (newStr == NULL) {
        obj = pj.newStringUTF("NULL");
    } else {
        obj = pj.newStringUTF(newStr);
    }

    return *this;
}

JString& JString::reset(const JString& newStr) {
    obj = newStr.obj;
    return *this;
}

JString& JString::reset(const jstring newStr) {
    if (newStr == NULL) {
        obj = pj.newStringUTF("NULL");
    } else {
        obj = newStr;
    }

    return *this;
}

JString& JString::reset(const jboolean& newStr) {
    if (newStr) {
        obj = pj.newStringUTF("true");
    } else {
        obj = pj.newStringUTF("false");
    }

    return *this;
}

JString& JString::reset(const jbyte& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jchar& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(C)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jshort& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jint& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(I)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jlong& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(J)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jfloat& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(F)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::reset(const jdouble& newStr) {
    obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf", "(D)Ljava/lang/String;", newStr);
    return *this;
}

JString& JString::clear() {
    obj = pj.newStringUTF("");
    return *this;
}

JString& JString::reset(const jobject& newStr) {
    if (newStr == NULL) {
        obj = pj.newStringUTF("NULL");
    } else {
        obj = (jstring) pj.callStaticObjectMethod(pj.findClass(obj), "valueOf",
                "(Ljava/lang/Object;)Ljava/lang/String;", newStr);
    }

    return *this;
}

const char* JString::getCString() const {
    return pj.getStringUTFChars((jstring) obj);
}

void JString::releaseCString(const char *s) const {
    if (checkNull(s, stackTrace("JString::releaseCString() can't accept NULL param!"))) {
        return;
    }

    pj.releaseStringUTFChars((jstring) obj, s);
}

void JString::doAction(const cstringAction act) const {
    if (checkArgument(act != NULL, "JString::doAction() can't accept NULL param!")) {
        return;
    }

    const char *tmp = getCString();
    act(tmp);
    releaseCString(tmp);
}

JString JString::trim() const {
    JString another((jstring) pj.callObjectMethod(obj, "trim", "()Ljava/lang/String;"));
    return another;
}

jint JString::length() const {
    return pj.callIntMethod(obj, "length", "()I");
}

jboolean JString::isEmpty() const {
    jint len = length();
    return len == 0 ? 1 : 0;
}

jchar JString::charAt(const jint index) const {
    return pj.callCharMethod(obj, "charAt", "(I)C", index);
}

jbyteArray JString::getBytes(const char *charsetName) const {
    if (charsetName == NULL) {
        return getBytes();
    }
    return (jbyteArray) pj.callObjectMethod(obj, "getBytes", "(Ljava/lang/String;)[B",
            pj.newStringUTF(charsetName));
}

jbyteArray JString::getBytes() const {
    return (jbyteArray) pj.callObjectMethod(obj, "getBytes", "()[B");
}

jboolean JString::equalsIgnoreCase(const jstring s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callBooleanMethod(obj, "equalsIgnoreCase", "(Ljava/lang/String;)Z", s);
}

jboolean JString::equalsIgnoreCase(const JString& s) const {
    return equalsIgnoreCase(s.obj);
}

jboolean JString::equalsIgnoreCase(const char *s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return equalsIgnoreCase(pj.newStringUTF(s));
}

jboolean JString::equals(const jobject s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callBooleanMethod(obj, "equals", "(Ljava/lang/Object;)Z", s);
}

jboolean JString::equals(const char *s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return equals(pj.newStringUTF(s));
}

jint JString::compareTo(const jstring s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callIntMethod(obj, "compareTo", "(Ljava/lang/String;)I", s);
}

jint JString::compareTo(const JString& s) const {
    return compareTo(s.obj);
}

jint JString::compareTo(const char *s) const {
    if (checkNull(s, "JString::compareTo can't accept NULL param!")) {
        return 0;
    }
    return compareTo(pj.newStringUTF(s));
}

jint JString::compareToIgnoreCase(const jstring s) const {
    if (checkNull(s, "JString::compareToIgnoreCase can't accept NULL param!")) {
        return 0;
    }
    return pj.callIntMethod(obj, "compareToIgnoreCase", "(Ljava/lang/String;)I", s);
}

jint JString::compareToIgnoreCase(const JString& s) const {
    return compareToIgnoreCase(s.obj);
}

jint JString::compareToIgnoreCase(const char *s) const {
    if (checkNull(s, "JString::compareToIgnoreCase can't accept NULL param!")) {
        return 0;
    }
    return compareToIgnoreCase(pj.newStringUTF(s));
}

jboolean JString::startsWith(const JString& s) const {
    return startsWith(s.obj);
}

jboolean JString::startsWith(const jstring s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callBooleanMethod(obj, "startsWith", "(Ljava/lang/String;)Z", s);
}

jboolean JString::startsWith(const char *s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return startsWith(pj.newStringUTF(s));
}

jboolean JString::endsWith(const JString& s) const {
    return endsWith(s.obj);
}

jboolean JString::endsWith(const jstring s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callBooleanMethod(obj, "endsWith", "(Ljava/lang/String;)Z", s);
}

jboolean JString::endsWith(const char *s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return endsWith(pj.newStringUTF(s));
}

jint JString::indexOf(const jchar ch) const {
    return pj.callIntMethod(obj, "indexOf", "(C)I", ch);
}

jint JString::indexOf(const jstring s) const {
    if (s == NULL) {
        return -1;
    }
    return pj.callIntMethod(obj, "indexOf", "(Ljava/lang/String;)I", s);
}

jint JString::indexOf(const JString& s) const {
    return indexOf(s.obj);
}

jint JString::indexOf(const char *s) const {
    if (s == NULL) {
        return -1;
    }
    return indexOf(pj.newStringUTF(s));
}

jint JString::lastIndexOf(const jchar ch) const {
    return pj.callIntMethod(obj, "lastIndexOf", "(C)I", ch);
}

jint JString::lastIndexOf(const jstring s) const {
    if (s == NULL) {
        return -1;
    }
    return pj.callIntMethod(obj, "lastIndexOf", "(Ljava/lang/String;)I", s);
}

jint JString::lastIndexOf(const JString& s) const {
    return lastIndexOf(s.obj);
}

jint JString::lastIndexOf(const char *s) const {
    if (s == NULL) {
        return -1;
    }
    return lastIndexOf(pj.newStringUTF(s));
}

JString JString::substring(const jint beginIndex) const {
    JString another((jstring) pj.callObjectMethod(obj, "substring", "(I)Ljava/lang/String;", beginIndex));
    return another;
}

JString JString::substring(const jint beginIndex, const jint endIndex) const {
    JString another(
            (jstring) pj.callObjectMethod(obj, "substring", "(II)Ljava/lang/String;", beginIndex, endIndex));
    return another;
}

JString JString::concat(const jstring s) const {
    if (s == NULL) {
        return concat("NULL");
    }
    JString another(
            (jstring) pj.callObjectMethod(obj, "concat", "(Ljava/lang/String;)Ljava/lang/String;", s));
    return another;
}

JString JString::concat(const JString& s) const {
    return concat((jstring) s.obj);
}

JString JString::concat(const char *s) const {
    if (s == NULL) {
        return concat("NULL");
    }
    return concat(pj.newStringUTF(s));
}

JString JString::replace(const jchar oldCh, const jchar newCh) const {
    JString another((jstring) pj.callObjectMethod(obj, "replace", "(CC)Ljava/lang/String;", oldCh, newCh));
    return another;
}

JString JString::replace(const JString& ojdStr, const JString& newStr) const {
    return replace(ojdStr.obj, newStr.obj);
}

JString JString::replace(const jstring ojdStr, const jstring newStr) const {
    if (checkArgument(ojdStr != NULL && newStr != NULL, "JString::replace() can't accept NULL param!")) {
        return *this;
    }
    JString another(
            (jstring) pj.callObjectMethod(obj, "replace",
                    "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", ojdStr, newStr));
    return another;
}

JString JString::replace(const char *ojdStr, const char *newStr) const {
    return replace(pj.newStringUTF(ojdStr), pj.newStringUTF(newStr));
}

JString JString::replaceAll(const JString& ojdStr, const JString& newStr) const {
    return replaceAll(ojdStr.obj, newStr.obj);
}

JString JString::replaceAll(const jstring ojdStr, const jstring newStr) const {
    if (checkArgument(ojdStr != NULL && newStr != NULL, "JString::replace() can't accept NULL param!")) {
        return *this;
    }
    JString another(
            (jstring) pj.callObjectMethod(obj, "replaceAll",
                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", ojdStr, newStr));
    return another;
}

JString JString::replaceAll(const char *ojdStr, const char *newStr) const {
    return replaceAll(pj.newStringUTF(ojdStr), pj.newStringUTF(newStr));
}

jboolean JString::contains(const JString& s) const {
    return contains(s.obj);
}

jboolean JString::contains(const jstring s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return pj.callBooleanMethod(obj, "contains", "(Ljava/lang/String;)Z", s);
}

jboolean JString::contains(const char *s) const {
    if (s == NULL) {
        return JNI_FALSE;
    }
    return contains(pj.newStringUTF(s));
}

jobjectArray JString::split(const jstring s) const {
    if (checkNull(s, "JString::split() can't accept NULL param!")) {
        return NULL;
    }
    return (jobjectArray) pj.callObjectMethod(obj, "split", "(Ljava/lang/String;)[Ljava/lang/String;", s);
}

jobjectArray JString::split(const JString& s) const {
    return split(s.obj);
}

jobjectArray JString::split(const char *s) const {
    if (checkNull(s, "JString::split() can't accept NULL param!")) {
        return NULL;
    }
    return split(pj.newStringUTF(s));
}

JString JString::toLowerCase() const {
    JString another((jstring) pj.callObjectMethod(obj, "toLowerCase", "()Ljava/lang/String;"));
    return another;
}

JString JString::toUpperCase() const {
    JString another((jstring) pj.callObjectMethod(obj, "toUpperCase", "()Ljava/lang/String;"));
    return another;
}

jcharArray JString::toCharArray() const {
    return (jcharArray) pj.callObjectMethod(obj, "toCharArray", "()[C");
}

JString& JString::intern() {
    obj = pj.intern((jstring) obj);
    return *this;
}

} // namespace su

