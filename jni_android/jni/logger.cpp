/*
 * logger.cpp
 *
 *  Created on: 2012-2-7
 *      Author: Administrator
 */

#include "logger.h"
#include <cstdarg>

namespace clark {
    namespace androids {

        int logger::v(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_VERBOSE, tag, text);
        }

        int logger::v_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_VERBOSE, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::v_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_VERBOSE, tag, fmt, ap);
        }

        int logger::d(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_DEBUG, tag, text);
        }

        int logger::d_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_DEBUG, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::d_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_DEBUG, tag, fmt, ap);
        }

        int logger::i(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_INFO, tag, text);
        }

        int logger::i_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_INFO, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::i_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_INFO, tag, fmt, ap);
        }

        int logger::w(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_WARN, tag, text);
        }

        int logger::w_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_WARN, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::w_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_WARN, tag, fmt, ap);
        }

        int logger::e(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_ERROR, tag, text);
        }

        int logger::e_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_ERROR, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::e_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_ERROR, tag, fmt, ap);
        }

        int logger::f(const char *tag, const char *text) {
            return __android_log_write(ANDROID_LOG_FATAL, tag, text);
        }

        int logger::f_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_FATAL, tag, fmt, args);
            va_end(args);
            return res;
        }

        int logger::f_vprint(const char *tag, const char *fmt, va_list ap) {
            return __android_log_vprint(ANDROID_LOG_FATAL, tag, fmt, ap);
        }

        void logger::asserts(const char *tag, const char *text,
                const char *cond) {
            if (cond == 0) {
                __android_log_assert("", tag, "%s", text);
            } else {
                __android_log_assert(cond, tag, "%s", text);
            }
        }

        logger::logger() {
        }

        logger::~logger() {
        }

    } /* namespace androids */
} /* namespace clark */
