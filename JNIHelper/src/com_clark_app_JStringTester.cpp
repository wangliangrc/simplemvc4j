/*
 * com_clark_app_JStringTester.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */
#define DEBUG

#include "global.h"
#include "JString.h"
#include "JNIHelper.h"
#include "com_clark_app_JStringTester.h"
#include <cassert>

namespace {
const char *HELLO_WORLD = "你好，世界";
}

using namespace std;

JNIEXPORT void JNICALL Java_com_clark_app_JStringTester_main(JNIEnv *env, jclass clazz, jobjectArray args) {
    su::init_jni(env); // 初始化全局变量
    su::JString s("Yeah!"); // 默认使用""初始化 JString 对象
    su::JNIHelper p;
    su::JString other("other JString");
    jobject testObject = p.newObject("java/lang/Object", "()V");

    // 测试 clear 方法
    cout << "测试 clear 方法" << endl;
    cout << s << endl;
    s.clear();
    cout << s << endl;

    // 测试 reset 方法
    cout << "测试 reset 方法" << endl;
    cout << s << endl;
    s.reset("^_^");
    cout << s << endl;

    s.reset(p.newStringUTF("new java.lang.String"));
    cout << s << endl;

    s.reset(other);
    cout << s << endl;

    s.reset((jboolean) JNI_TRUE);
    cout << s << endl;

    s.reset((jbyte) 111);
    cout << s << endl;

    s.reset((jshort) 111);
    cout << s << endl;

    s.reset((jchar) 'a');
    cout << s << endl;

    s.reset((jint) 111);
    cout << s << endl;

    s.reset((jlong) 111);
    cout << s << endl;

    s.reset((jfloat) 111);
    cout << s << endl;

    s.reset((jdouble) 111);
    cout << s << endl;

    s.reset(testObject);
    cout << s << endl;
    s.clear();

    // 测试 == 运算符重载
    cout << "测试 == 运算符重载" << endl;
    other.reset(s);
    assert(s == other);

    // 测试 + 运算符重载
    cout << "测试 + 运算符重载" << endl;
    other.reset(HELLO_WORLD);
    s.reset("s");
    cout << s + other << endl;
    assert(s == "s");

    cout << s + p.newStringUTF(HELLO_WORLD) << endl;
    assert(s == "s");

    cout << s + HELLO_WORLD << endl;
    assert(s == "s");

    cout << s + (jboolean) JNI_TRUE << endl;
    assert(s == "s");

    cout << s + (jbyte) 100 << endl;
    assert(s == "s");

    cout << s + (jchar) 'A' << endl;
    assert(s == "s");

    cout << s + (jshort) 100 << endl;
    assert(s == "s");

    cout << s + (jint) 100 << endl;
    assert(s == "s");

    cout << s + (jlong) 100 << endl;
    assert(s == "s");

    cout << s + (jfloat) 100. << endl;
    assert(s == "s");

    cout << s + (jdouble) 100. << endl;
    assert(s == "s");

    cout << s + testObject << endl;
    assert(s == "s");

    cout << p.newStringUTF(HELLO_WORLD) + s << endl;
    assert(s == "s");

    cout << HELLO_WORLD + s << endl;
    assert(s == "s");

    cout << (jboolean) JNI_TRUE + s << endl;
    assert(s == "s");

    cout << (jbyte) 100 + s << endl;
    assert(s == "s");

    cout << (jchar) 'A' + s << endl;
    assert(s == "s");

    cout << (jshort) 100 + s << endl;
    assert(s == "s");

    cout << (jint) 100 + s << endl;
    assert(s == "s");

    cout << (jlong) 100 + s << endl;
    assert(s == "s");

    cout << (jfloat) 100. + s << endl;
    assert(s == "s");

    cout << (jdouble) 100. + s << endl;
    assert(s == "s");

    cout << testObject + s << endl;
    assert(s == "s");

    cout << "测试 [] 运算符重载" << endl;
    s.reset("Hello");
    cout << s << endl;
    cout << (char) s[0] << (char) s[1] << (char) s[2] << endl;
    s.clear();

    s.reset("测试 << 运算符重载");
    cout << s << endl;
    cout << s.toString() << endl;
    cout << s.hashCode() << endl;
    s.clear();

    s.reset("测试 length 方法");
    cout << "测试 length 方法" << endl;
    cout << s.length() << endl;
    s.clear();

    s.reset("111122223333");
    cout << "测试 replace 方法" << endl;
    cout << s.replace('1', '2') << endl;
    s.clear();
}

