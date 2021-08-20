#include "st_petersburg.h"

#include "random_gen.h"

double calculate_expected_value(const unsigned long runs, const double bankroll)
{
    if (runs == 0) {
        return 0;
    }
    double sum_profit = 0;
    for (unsigned long i = 0; i < runs; i++) {
        double curr_profit = 2;
        while (get_random_number() < 0.5 && curr_profit <= bankroll) {
            curr_profit *= 2;
        }
        sum_profit += bankroll < curr_profit ? bankroll : curr_profit;
    }
    return sum_profit / runs;
}
