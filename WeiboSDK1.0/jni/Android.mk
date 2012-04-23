TOP_LOCAL_PATH:= $(call my-dir)

#########################################################################
# Build JNI Shared Library
#########################################################################

LOCAL_PATH:= $(TOP_LOCAL_PATH)

include $(CLEAR_VARS)

# Optional tag would mean it doesn't get installed by default
LOCAL_MODULE_TAGS := optional

LOCAL_CFLAGS := -Werror

LOCAL_SRC_FILES:= \
  jsonlib/dump.c \
  jsonlib/error.c \
  jsonlib/hashtable.c \
  jsonlib/load.c \
  jsonlib/memory.c \
  jsonlib/pack_unpack.c \
  jsonlib/strbuffer.c \
  jsonlib/strconv.c \
  jsonlib/utf.c \
  jsonlib/value.c \
  geo_parser.c \
  status_parser.c \
  statuslist_parser.c \
  userinfo_parser.c \
  utils.c \
  visible_parser.c \
  message_parser.c \
  messagelist_parser.c

LOCAL_MODULE := cjson

LOCAL_LDLIBS := -L$(SYSROOT)/lib -llog

include $(BUILD_SHARED_LIBRARY)
#include $(BUILD_EXECUTABLE)
