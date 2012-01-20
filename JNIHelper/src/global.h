/*
 * global.h
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */

#ifndef GLOBAL_H_
#define GLOBAL_H_

#define DEBUG
#include <jni.h>
#include <iostream>

namespace su {

void init_jni(JNIEnv *env);
JNIEnv* get_env();

}

#endif /* GLOBAL_H_ */
