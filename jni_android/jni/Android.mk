LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog

LOCAL_MODULE := clark-jni

LOCAL_SRC_FILES := com_clark_app_MainActivity.cpp \
        jni_field.cpp \
        jni_method.cpp \
        jni_stdout.cpp \
        jni_utils.cpp \
        string_utils.cpp \
        logger.cpp \
        jni_array.cpp \
        jni_class.cpp

LOCAL_CFLAGS := -fexceptions

include $(BUILD_SHARED_LIBRARY)