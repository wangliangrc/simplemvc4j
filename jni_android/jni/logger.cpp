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

        bool logger::switcher = false;

        int logger::v(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_VERBOSE, tag, text) : 0;
        }

        int logger::v_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_VERBOSE, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::v_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_VERBOSE, tag, fmt, ap) : 0;
        }

        int logger::d(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_DEBUG, tag, text) : 0;
        }

        int logger::d_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_DEBUG, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::d_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_DEBUG, tag, fmt, ap) : 0;
        }

        int logger::i(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_INFO, tag, text) : 0;
        }

        int logger::i_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_INFO, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::i_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_INFO, tag, fmt, ap) : 0;
        }

        int logger::w(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_WARN, tag, text) : 0;
        }

        int logger::w_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_WARN, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::w_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_WARN, tag, fmt, ap) : 0;
        }

        int logger::e(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_ERROR, tag, text) : 0;
        }

        int logger::e_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_ERROR, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::e_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_ERROR, tag, fmt, ap) : 0;
        }

        int logger::f(const char *tag, const char *text) {
            return switcher ?
                    __android_log_write(ANDROID_LOG_FATAL, tag, text) : 0;
        }

        int logger::f_print(const char *tag, const char *fmt, ...) {
            va_list args;
            va_start(args, fmt);
            int res = __android_log_vprint(ANDROID_LOG_FATAL, tag, fmt, args);
            va_end(args);
            return switcher ? res : 0;
        }

        int logger::f_vprint(const char *tag, const char *fmt, va_list ap) {
            return switcher ?
                    __android_log_vprint(ANDROID_LOG_FATAL, tag, fmt, ap) : 0;
        }

        void logger::asserts(const char *tag, const char *text,
                const char *cond) {
            if (cond == 0) {
                __android_log_assert("", tag, "%s", text);
            } else {
                __android_log_assert(cond, tag, "%s", text);
            }
        }

        void logger::open() {
            switcher = true;
        }

        void logger::close() {
            switcher = false;
        }

        void logger::asserts(const char *tag, const std::string & text,
                const char *cond) {
            asserts(tag, text.c_str(), cond);
        }

        int logger::v(const char *tag, const std::string & text) {
            return v(tag, text.c_str());
        }

        int logger::d(const char *tag, const std::string & text) {
            return d(tag, text.c_str());
        }

        int logger::i(const char *tag, const std::string & text) {
            return i(tag, text.c_str());
        }

        int logger::w(const char *tag, const std::string & text) {
            return w(tag, text.c_str());
        }

        int logger::e(const char *tag, const std::string & text) {
            return e(tag, text.c_str());
        }

        int logger::f(const char *tag, const std::string & text) {
            return f(tag, text.c_str());
        }

        logger::logger() {
        }

        logger::~logger() {
        }

    } /* namespace androids */
} /* namespace clark */
