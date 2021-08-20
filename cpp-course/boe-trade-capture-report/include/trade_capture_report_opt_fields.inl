#ifndef FIELD
#  error You need to define FIELD macro
#else
FIELD(capacity, 2, 1)
FIELD(party_role, 2, 16)
FIELD(symbol, 1, 1)
FIELD(trade_publish_indicator, 3, 32)
#undef FIELD
#endif
