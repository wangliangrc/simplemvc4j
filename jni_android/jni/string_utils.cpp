/*
 * string_utils.cc
 *
 *  Created on: 2012-1-31
 *      Author: Clark
 */
//#define DEBUG
#include "string_utils.h"
#include <cctype>
#include <cstdio>
#include <cstdlib>

std::string clark::strings::trim(const std::string & in) {
    std::string res = in;
    if (res.length() > 0) {
        std::string::size_type start = 0;
        std::string::size_type end = res.length();
        bool pre = false;
        bool suf = false;
        for (int i = 0, len = res.length(); i < len; ++i) {
            if (pre && suf) {
                break;
            }

            if (!pre) {
                if (std::isspace(res[i])) {
                    start++;
                } else {
                    pre = true;
                }
            }

            if (!suf) {
                if (std::isspace(res[len - i - 1])) {
                    end--;
                } else {
                    suf = true;
                }
            }
        }
        if (start > 0 || end < res.length()) {
            res = res.substr(start, end - start);
        }
    }
    return res;
}

std::string clark::strings::toUpperCase(const std::string & in) {
    std::string res = in;
    for (std::string::iterator i = res.begin(), j = res.end(); i != j; ++i) {
        *i = std::toupper(*i);
    }
    return res;
}

std::string clark::strings::toLowerCase(const std::string & in) {
    std::string res = in;
    for (std::string::iterator i = res.begin(), j = res.end(); i != j; ++i) {
        *i = std::tolower(*i);
    }
    return res;
}

bool clark::strings::startsWiths(const std::string & in,
        const std::string & prefix) {
    std::string::size_type inLen = in.length();
    std::string::size_type preLen = prefix.length();
    if (inLen < preLen) {
        return false;
    } else if (inLen == preLen) {
        return in == prefix;
    } else {
        return in.substr(0, preLen) == prefix;
    }
}

bool clark::strings::endsWiths(const std::string & in,
        const std::string & suffix) {
    std::string::size_type inLen = in.length();
    std::string::size_type preLen = suffix.length();
    if (inLen < preLen) {
        return false;
    } else if (inLen == preLen) {
        return in == suffix;
    } else {
        return in.substr(inLen - preLen, preLen) == suffix;
    }
}

bool clark::strings::contains(const std::string & in,
        const std::string & target) {
    return in.find(target.c_str()) >= 0;
}

namespace {
    char temp[256] = { 0 };
}

std::string clark::strings::valueof(const int & i) {
    std::sprintf(temp, "%d", i);
    return std::string(temp);
}

std::string clark::strings::valueof(const long & l) {
    std::sprintf(temp, "%ld", l);
    return std::string(temp);
}

std::string clark::strings::valueof(const char & c) {
    std::sprintf(temp, "%c", c);
    return std::string(temp);
}

std::string clark::strings::valueof(const float & f) {
    std::sprintf(temp, "%g", f);
    return std::string(temp);
}

std::string clark::strings::valueof(const double & d) {
    std::sprintf(temp, "%g", d);
    return std::string(temp);
}

std::string clark::strings::valueof(const short & i) {
    std::sprintf(temp, "%hd", i);
    return std::string(temp);
}

std::string clark::strings::valueof(const bool & b) {
    return b ? std::string("true") : std::string("false");
}

std::string operator +(const std::string & left, const int & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const long & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const char & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const float & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const double & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const bool & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const int & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const long & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const char & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const float & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const double & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const std::string & left, const short & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const wchar_t & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const short & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const std::string & left, const unsigned short & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const unsigned int & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const std::string & left, const unsigned long & right) {
    return left + clark::strings::valueof(right);
}

std::string operator +(const unsigned short & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const unsigned int & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const unsigned long & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

std::string operator +(const bool & left, const std::string & right) {
    return clark::strings::valueof(left) + right;
}

short clark::strings::toShort(const std::string & s) {
    return static_cast<short>(toInt(s));
}

int clark::strings::toInt(const std::string & s) {
    return std::atoi(s.c_str());
}

long clark::strings::toLong(const std::string & s) {
    return std::atol(s.c_str());
}

char clark::strings::toChar(const std::string & s) {
    if (s.empty()) {
        return '\0';
    }
    return s[0];
}

float clark::strings::toFloat(const std::string & s) {
    return static_cast<float>(toDouble(s));
}

double clark::strings::toDouble(const std::string & s) {
    return std::atof(s.c_str());
}

std::string clark::strings::valueof(const unsigned short & i) {
    std::sprintf(temp, "%uhd", i);
    return std::string(temp);
}

std::string clark::strings::valueof(const unsigned int & i) {
    std::sprintf(temp, "%ud", i);
    return std::string(temp);
}

std::string clark::strings::valueof(const unsigned long & l) {
    std::sprintf(temp, "%lud", l);
    return std::string(temp);
}

bool clark::strings::toBool(const std::string & s) {
    return toLowerCase(s) == "true";
}

std::vector<std::string> clark::strings::split(const std::string & s,
        const char & c) {
    std::vector<std::string> array;
    std::vector<std::string::size_type> pos;
    for (int i = 0, len = s.length(); i < len; ++i) {
        if (s[i] == c) {
            pos.push_back(i);
        }
    }

    for (int i = 0, len = pos.size(); i < len; ++i) {
        if (pos[i] > 0 && i == 0) {
            array.push_back(s.substr(0, pos[i]));
        } else if (i > 0 && pos[i] - pos[i - 1] > 1) {
            array.push_back(s.substr(pos[i - 1] + 1, pos[i] - pos[i - 1] - 1));
        }

        if (i + 1 >= len && pos[i] < s.length() - 1) {
            array.push_back(s.substr(pos[i] + 1, s.length() - pos[i] - 1));
        }
    }

    if (array.empty()) {
        array.push_back(s.substr());
    }
    return array;
}

std::ostream & operator <<(std::ostream & out,
        const std::vector<std::string> array) {
    out << "<";
    for (int i = 0, len = array.size(); i < len; ++i) {
        if (i != 0) {
            out << ", ";
        }
        out << array[i];
    }
    out << ">";
    return out;
}

