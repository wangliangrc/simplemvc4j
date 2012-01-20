/*
 * com_clark_app_Main.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#include "com_clark_app_Main.h"
#include "global.h"
#include "JThread.h"
using namespace std;

JNIEXPORT void JNICALL Java_com_clark_app_Main_main(JNIEnv *env, jclass clazz, jobjectArray args) {
    su::init_jni(env);

}
