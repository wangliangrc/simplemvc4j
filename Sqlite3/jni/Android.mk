LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sqlite_jni
LOCAL_SRC_FILES := sqlite_jni.c \
				sqlite3.c
LOCAL_LDLIBS    :=

include $(BUILD_SHARED_LIBRARY)