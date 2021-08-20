#include <algorithm>
#include <cstring>
#include <fstream>
#include <iterator>
#include <iostream>
#include <string>
#include <vector>
#include <optional>

namespace {

using std::string;

std::optional<int> get_numeric_value(const string &str)
{
    size_t i = 0;
    while (isblank(str[i]) && i < str.length()) {
        ++i;
    }
    if (i == str.length() - 1) {
        return 0;
    }
    else {
        bool is_neg = false;
        int value = 0;
        if (str[i] == '-') {
            is_neg = true;
            ++i;
        }
        for (size_t j = i; j < str.length(); ++j) {
            if (std::isdigit(str[j])) {
                value = value * 10 + (str[j] - '0');
            }
            else {
                return std::nullopt;
            }
        }
        return is_neg ? -value : value;
    }
}

struct NumericComparator {
    bool operator()(const string &s1, const string &s2) {
        const auto n1 = get_numeric_value(s1);
        const auto n2 = get_numeric_value(s2);
        if (n1 && n2) {
            return n1 < n2;
        }
        else if (n1) {
            return n1 <= 0;
        }
        else if (n2) {
            return n2 > 0;
        }
        else {
            return s1 < s2;
        }
    }
};

struct CaseIgnoreComparator {
    bool operator()(const string &s1, const string &s2) {
        for (size_t i = 0; i < std::min(s1.length(), s2.length()); ++i) {
            if (toupper(s1[i]) < toupper(s2[i])) {
                return true;
            }
            else if (toupper(s1[i]) > toupper(s2[i])) {
                return false;
            }
        }
        return s1.length() < s2.length();
    }
};

void print_out(std::ostream &strm, const std::vector<string> &c)
{
    std::ostream_iterator<string> out(strm, "\n");
    std::copy(c.begin(), c.end(), out);
}

void sort_stream(std::istream &input, const bool case_ignore, const bool num_sort)
{
    std::vector<string> lines;

    string line;
    while (std::getline(input, line)) {
        lines.push_back(line);
    }

    if (num_sort) {
        std::sort(lines.begin(), lines.end(), NumericComparator());
    }
    else if (case_ignore) {
        std::sort(lines.begin(), lines.end(), CaseIgnoreComparator());
    }
    else {
        std::sort(lines.begin(), lines.end());
    }

    print_out(std::cout, lines);
}

} // anonymous namespace

int main(int argc, char **argv)
{
    bool case_ignore = false;
    bool num_sort = false;
    const char *input_name = nullptr;
    for (int i = 1; i < argc; ++i) {
        if (argv[i][0] == '-') {
            if (argv[i][1] != '-') {
                const size_t len = std::strlen(argv[i]);
                for (size_t j = 1; j < len; ++j) {
                    switch (argv[i][j]) {
                        case 'f':
                            case_ignore = true;
                            break;
                        case 'n':
                            num_sort = true;
                            break;
                    }
                }
            }
            else {
                if (std::strcmp(argv[i], "--ignore-case") == 0) {
                    case_ignore = true;
                }
                else if (std::strcmp(argv[i], "--numeric-sort") == 0) {
                    num_sort = true;
                }
            }
        }
        else {
            input_name = argv[i];
        }
    }

    if (input_name != nullptr) {
        std::ifstream f(input_name);
        sort_stream(f, case_ignore, num_sort);
    }
    else {
        sort_stream(std::cin, case_ignore, num_sort);
    }
}
