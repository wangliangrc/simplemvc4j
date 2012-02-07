/*
 * std::string_utils.h
 *
 *  Created on: 2012-1-31
 *      Author: Clark
 */

#ifndef STRING_UTILS_H_
#define STRING_UTILS_H_

#include <string>
#include <vector>
#include <iostream>

std::string operator +(const std::string& left, const short& right);
std::string operator +(const std::string& left, const unsigned short& right);
std::string operator +(const std::string& left, const int& right);
std::string operator +(const std::string& left, const unsigned int& right);
std::string operator +(const std::string& left, const long& right);
std::string operator +(const std::string& left, const unsigned long& right);
std::string operator +(const std::string& left, const char& right);
std::string operator +(const std::string& left, const float& right);
std::string operator +(const std::string& left, const double& right);
std::string operator +(const std::string& left, const bool& right);

std::string operator +(const short& left, const std::string& right);
std::string operator +(const unsigned short& left, const std::string& right);
std::string operator +(const int& left, const std::string& right);
std::string operator +(const unsigned int& left, const std::string& right);
std::string operator +(const long& left, const std::string& right);
std::string operator +(const unsigned long& left, const std::string& right);
std::string operator +(const char& left, const std::string& right);
std::string operator +(const float& left, const std::string& right);
std::string operator +(const double& left, const std::string& right);
std::string operator +(const bool& left, const std::string& right);

std::ostream& operator <<(std::ostream& out,
        const std::vector<std::string> array);

namespace clark {

    namespace strings {

        std::string trim(const std::string& in);
        std::string toUpperCase(const std::string& in);
        std::string toLowerCase(const std::string& in);
        bool startsWiths(const std::string& in, const std::string& prefix);
        bool endsWiths(const std::string& in, const std::string& suffix);
        bool contains(const std::string& in, const std::string& target);
        std::string valueof(const short& i);
        std::string valueof(const unsigned short& i);
        std::string valueof(const int& i);
        std::string valueof(const unsigned int& i);
        std::string valueof(const long& l);
        std::string valueof(const unsigned long& l);
        std::string valueof(const char& c);
        std::string valueof(const float& f);
        std::string valueof(const double& d);
        std::string valueof(const bool& b);

        short toShort(const std::string& s);
        int toInt(const std::string& s);
        long toLong(const std::string& s);
        char toChar(const std::string& s);
        float toFloat(const std::string& s);
        double toDouble(const std::string& s);
        bool toBool(const std::string& s);

        std::vector<std::string> split(const std::string& s, const char& c);

    } // namespace std::strings

} // namespace clark

#endif /* STRING_UTILS_H_ */
