/*
 * logger.h
 *
 *  Created on: 2012-2-7
 *      Author: Administrator
 */

#ifndef LOGGER_H_
#define LOGGER_H_

#include <android/log.h>

namespace clark {
    namespace androids {

        class logger {
        public:
            virtual ~logger();

            static void asserts(const char *tag, const char *text,
                    const char *cond = 0);

            static int v(const char *tag, const char *text);
            static int v_print(const char *tag, const char *fmt, ...);
            static int v_vprint(const char *tag, const char *fmt, va_list ap);

            static int d(const char *tag, const char *text);
            static int d_print(const char *tag, const char *fmt, ...);
            static int d_vprint(const char *tag, const char *fmt, va_list ap);

            static int i(const char *tag, const char *text);
            static int i_print(const char *tag, const char *fmt, ...);
            static int i_vprint(const char *tag, const char *fmt, va_list ap);

            static int w(const char *tag, const char *text);
            static int w_print(const char *tag, const char *fmt, ...);
            static int w_vprint(const char *tag, const char *fmt, va_list ap);

            static int e(const char *tag, const char *text);
            static int e_print(const char *tag, const char *fmt, ...);
            static int e_vprint(const char *tag, const char *fmt, va_list ap);

            static int f(const char *tag, const char *text);
            static int f_print(const char *tag, const char *fmt, ...);
            static int f_vprint(const char *tag, const char *fmt, va_list ap);

        private:
            logger();
        };

    } /* namespace androids */
} /* namespace clark */
#endif /* LOGGER_H_ */
