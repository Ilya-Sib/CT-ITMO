#include "calc.h"

#include <cctype>   // for std::isspace
#include <cmath>    // various math functions
#include <iostream> // for error reporting via std::cerr
#include <vector>   // for operation arguments

namespace {

const std::size_t max_decimal_digits = 10;

enum class Op
{
    ERR,
    SET,
    ADD,
    SUB,
    MUL,
    DIV,
    REM,
    NEG,
    POW,
    SQRT
};

std::size_t arity(const Op op)
{
    switch (op) {
        // error
    case Op::ERR:
        return 0;
        // unary
    case Op::NEG: return 1;
    case Op::SQRT:
        return 1;
        // binary
    case Op::SET: return 2;
    case Op::ADD: return 2;
    case Op::SUB: return 2;
    case Op::MUL: return 2;
    case Op::DIV: return 2;
    case Op::REM: return 2;
    case Op::POW: return 2;
    }
    return 0;
}

Op parse_op(const std::string & line, std::size_t & i, bool & fold)
{
    const auto rollback = [&i, &line](const std::size_t n) {
        i -= n;
        std::cerr << "Unknown operation " << line << std::endl;
        return Op::ERR;
    };
    if (line[i] == '(') {
        ++i;
        fold = true;
    }
    Op result;
    switch (line[i++]) {
    case '0':
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
        --i; // a first digit is a part of op's argument
        return Op::SET;
    case '+':
        result = Op::ADD;
        break;
    case '-':
        result = Op::SUB;
        break;
    case '*':
        result = Op::MUL;
        break;
    case '/':
        result = Op::DIV;
        break;
    case '%':
        result = Op::REM;
        break;
    case '_':
        return Op::NEG;
    case '^':
        result = Op::POW;
        break;
    case 'S':
        switch (line[i++]) {
        case 'Q':
            switch (line[i++]) {
            case 'R':
                switch (line[i++]) {
                case 'T':
                    return Op::SQRT;
                default:
                    return rollback(4);
                }
            default:
                return rollback(3);
            }
        default:
            return rollback(2);
        }
    default:
        return rollback(1);
    }
    if (fold && line[i++] != ')') {
        return rollback(3);
    }
    return result;
}

std::size_t skip_ws(const std::string & line, std::size_t i)
{
    while (i < line.size() && std::isspace(line[i])) {
        ++i;
    }
    return i;
}

double parse_arg(const std::string & line, std::size_t & i, const bool & fold, bool & correct)
{
    double result = 0;
    std::size_t count = 0;
    bool good = true;
    bool integer = true;
    double fraction = 1;
    while (good && i < line.size() && count < max_decimal_digits) {
        switch (line[i]) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            if (integer) {
                result *= 10;
                result += line[i] - '0';
            }
            else {
                fraction /= 10;
                result += (line[i] - '0') * fraction;
            }
            ++i;
            ++count;
            break;
        case '.':
            integer = false;
            ++i;
            break;
        default:
            good = false;
            break;
        }
    }
    if (!good && !fold) {
        std::cerr << "Argument parsing error at " << i << ": '" << line.substr(i) << "'" << std::endl;
        correct = false;
    }
    else if (i < line.size() && !fold) {
        std::cerr << "Argument isn't fully parsed, suffix left: '" << line.substr(i) << "'" << std::endl;
    }
    return result;
}

std::vector<double> parse_args(const std::string & line, std::size_t & i, const bool & fold)
{
    std::vector<double> result;
    bool correct;
    do {
        correct = true;
        double argument = parse_arg(line, i, fold, correct);
        i = skip_ws(line, i);
        if (correct) {
            result.push_back(argument);
        }
        if (fold && i < line.size() && !std::isdigit(line[i])) {
            std::cerr << "Argument parsing error at " << i << ": '" << line.substr(i) << "'" << std::endl;
            break;
        }
    } while (fold && i < line.size());
    return result;
}

double unary(const double current, const Op op)
{
    switch (op) {
    case Op::NEG:
        return -current;
    case Op::SQRT:
        if (current > 0) {
            return std::sqrt(current);
        }
        else {
            std::cerr << "Bad argument for SQRT: " << current << std::endl;
            [[fallthrough]];
        }
    default:
        return current;
    }
}

double binary(const Op op, const double left, const std::vector<double> & args)
{
    double result = left;
    switch (op) {
    case Op::SET:
        return args.empty() ? left : args[0];
    case Op::ADD: {
        for (const double arg : args) {
            result += arg;
        }
    } break;
    case Op::SUB: {
        for (const double arg : args) {
            result -= arg;
        }
    } break;
    case Op::MUL: {
        for (const double arg : args) {
            result *= arg;
        }
    } break;
    case Op::DIV: {
        for (const double arg : args) {
            if (arg != 0) {
                result /= arg;
            }
            else {
                std::cerr << "Bad right argument for division: " << arg << std::endl;
                return left;
            }
        }
    } break;
    case Op::REM: {
        for (const double arg : args) {
            if (arg != 0) {
                result = std::fmod(result, arg);
            }
            else {
                std::cerr << "Bad right argument for remainder: " << arg << std::endl;
                return left;
            }
        }
    } break;
    case Op::POW: {
        for (const double arg : args) {
            result = std::pow(result, arg);
        }
    } break;
    default:
        return left;
    }
    return result;
}

} // anonymous namespace

double process_line(const double current, const std::string & line)
{
    std::size_t i = 0;
    bool fold = false;
    const auto op = parse_op(line, i, fold);
    std::vector<double> args;
    switch (arity(op)) {
    case 2: {
        i = skip_ws(line, i);
        const auto old_i = i;
        args = parse_args(line, i, fold);
        if (i == old_i) {
            std::cerr << "No argument for a binary operation" << std::endl;
            break;
        }
        else if (i < line.size()) {
            break;
        }
        return binary(op, current, args);
    }
    case 1: {
        if (i < line.size()) {
            std::cerr << "Unexpected suffix for a unary operation: '" << line.substr(i) << "'" << std::endl;
            break;
        }
        return unary(current, op);
    }
    default: break;
    }
    return current;
}