LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := vm
LOCAL_SRC_FILES := coms_Vm.cpp

include $(BUILD_SHARED_LIBRARY)