/*
 * logger.h
 *
 *  Created on: 2012-2-7
 *      Author: Administrator
 */

#ifndef LOGGER_H_
#define LOGGER_H_

#include <android/log.h>
#include <string>

namespace clark {
    namespace androids {

        class logger {
        public:
            virtual ~logger();

            static void asserts(const char *tag, const char *text,
                    const char *cond = 0);
            static void asserts(const char *tag, const std::string& text,
                    const char *cond = 0);

            static int v(const char *tag, const char *text);
            static int v(const char *tag, const std::string& text);
            static int v_print(const char *tag, const char *fmt, ...);
            static int v_vprint(const char *tag, const char *fmt, va_list ap);

            static int d(const char *tag, const char *text);
            static int d(const char *tag, const std::string& text);
            static int d_print(const char *tag, const char *fmt, ...);
            static int d_vprint(const char *tag, const char *fmt, va_list ap);

            static int i(const char *tag, const char *text);
            static int i(const char *tag, const std::string& text);
            static int i_print(const char *tag, const char *fmt, ...);
            static int i_vprint(const char *tag, const char *fmt, va_list ap);

            static int w(const char *tag, const char *text);
            static int w(const char *tag, const std::string& text);
            static int w_print(const char *tag, const char *fmt, ...);
            static int w_vprint(const char *tag, const char *fmt, va_list ap);

            static int e(const char *tag, const char *text);
            static int e(const char *tag, const std::string& text);
            static int e_print(const char *tag, const char *fmt, ...);
            static int e_vprint(const char *tag, const char *fmt, va_list ap);

            static int f(const char *tag, const char *text);
            static int f(const char *tag, const std::string& text);
            static int f_print(const char *tag, const char *fmt, ...);
            static int f_vprint(const char *tag, const char *fmt, va_list ap);

            static void open();
            static void close();

        private:
            logger();

            static bool switcher;
        };

    } /* namespace androids */
} /* namespace clark */

#define assert_android(bool_exp, tag, msg) if(!(bool_exp)) \
    clark::androids::logger::e_print((tag), "ERROR: %s:%d", __FILE__, __LINE__);\
    clark::androids::logger::asserts((tag), (msg))

#endif /* LOGGER_H_ */
