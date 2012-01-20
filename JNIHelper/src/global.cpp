/*
 * global.cpp
 *
 *  Created on: Jan 16, 2012
 *      Author: clark
 */
#include "global.h"
#include <cassert>
#include <cstddef>
using namespace std;

static JNIEnv *_GLOBAL_ENV_;

namespace su {

void init_jni(JNIEnv *env) {
    assert(env != NULL);
    _GLOBAL_ENV_ = env;
}

JNIEnv* get_env() {
    return _GLOBAL_ENV_;
}

}

