LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := db
LOCAL_SRC_FILES := NativeDB.c \
			sqlite3.c
LOCAL_LDLIBS    :=

include $(BUILD_SHARED_LIBRARY)
