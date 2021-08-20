#include "requests.h"

namespace {

void encode_new_order_opt_fields(unsigned char * bitfield_start,
                                 const double price,
                                 const char ord_type,
                                 const char time_in_force,
                                 const unsigned max_floor,
                                 const std::string & symbol,
                                 const char capacity,
                                 const std::string & account)
{
    auto * p = bitfield_start + new_order_bitfield_num();
#define FIELD(name, bitfield_num, bit)                    \
    set_opt_field_bit(bitfield_start, bitfield_num, bit); \
    p = encode_field_##name(p, name);
#include "new_order_opt_fields.inl"
}

uint8_t encode_request_type(const RequestType type)
{
    switch (type) {
    case RequestType::New:
        return 0x38;
    case RequestType::TradeCaptureReport:
        return 0x3C;
    }
    return 0;
}

unsigned char * encode_deferred_publication(unsigned char * start, const bool & deferred_publication)
{
    start = encode_char(start, static_cast<unsigned char>(deferred_publication ? 0x2 : 0x1));
    return start;
}

unsigned char * add_request_header(unsigned char * start, unsigned length, const RequestType type, unsigned seq_no)
{
    *start++ = 0xBA;
    *start++ = 0xBA;
    start = encode(start, static_cast<uint16_t>(length));
    start = encode(start, encode_request_type(type));
    *start++ = 0;
    return encode(start, seq_no);
}

char convert_side(const Side side)
{
    switch (side) {
    case Side::Buy: return '1';
    case Side::Sell: return '2';
    }
    return 0;
}

char convert_ord_type(const OrdType ord_type)
{
    switch (ord_type) {
    case OrdType::Market: return '1';
    case OrdType::Limit: return '2';
    case OrdType::Pegged: return 'P';
    }
    return 0;
}

char convert_time_in_force(const TimeInForce time_in_force)
{
    switch (time_in_force) {
    case TimeInForce::Day: return '0';
    case TimeInForce::IOC: return '3';
    case TimeInForce::GTD: return '6';
    }
    return 0;
}

char convert_capacity(const Capacity capacity)
{
    switch (capacity) {
    case Capacity::Agency: return 'A';
    case Capacity::Principal: return 'P';
    case Capacity::RisklessPrincipal: return 'R';
    }
    return 0;
}

unsigned char * encode_sides(unsigned char * start,
                             const uint8_t & no_sides,
                             const std::vector<Side> & sides,
                             const std::vector<Capacity> & capacity,
                             const std::vector<std::string> & party_id,
                             const std::vector<char> & no)
{
    start = encode(start, no_sides);
    for (int i = 0; i < no_sides; ++i) {
        start = encode_char(start, convert_side(sides[i]));
        start = encode_char(start, convert_capacity(capacity[i]));
        start = encode_field_party_id(start, party_id[i]);
        start = encode_char(start, no[i]);
    }
    return start;
}

} // anonymous namespace

std::array<unsigned char, calculate_size(RequestType::New)> create_new_order_request(const unsigned seq_no,
                                                                                     const std::string & cl_ord_id,
                                                                                     const Side side,
                                                                                     const double volume,
                                                                                     const double price,
                                                                                     const OrdType ord_type,
                                                                                     const TimeInForce time_in_force,
                                                                                     const double max_floor,
                                                                                     const std::string & symbol,
                                                                                     const Capacity capacity,
                                                                                     const std::string & account)
{
    static_assert(calculate_size(RequestType::New) == 78, "Wrong New Order message size");

    std::array<unsigned char, calculate_size(RequestType::New)> msg;
    auto * p = add_request_header(&msg[0], msg.size() - 2, RequestType::New, seq_no);
    p = encode_text(p, cl_ord_id, 20);
    p = encode_char(p, convert_side(side));
    p = encode_binary4(p, static_cast<uint32_t>(volume));
    p = encode(p, static_cast<uint8_t>(new_order_bitfield_num()));
    encode_new_order_opt_fields(p,
                                price,
                                convert_ord_type(ord_type),
                                convert_time_in_force(time_in_force),
                                max_floor,
                                symbol,
                                convert_capacity(capacity),
                                account);
    return msg;
}

std::vector<unsigned char> create_trade_capture_report_request(
        unsigned seq_no,
        const std::string & trade_report_id,
        double volume,
        double price,
        const std::string & party_id,
        Side side,
        Capacity capacity,
        const std::string & contra_party_id,
        Capacity contra_capacity,
        const std::string & symbol,
        bool deferred_publication)
{
    std::vector<unsigned char> msg(calculate_size(RequestType::TradeCaptureReport));
    auto * p = add_request_header(&msg[0], msg.size() - 2, RequestType::TradeCaptureReport, seq_no);
    p = encode_text(p, trade_report_id, 20);
    p = encode_binary4(p, static_cast<uint32_t>(volume));
    p = encode_trade_capture_report_price(p, price);
    p = encode_bitfields(p);
    p = encode_sides(p,
                     2,
                     {side, side == Side::Sell ? Side::Buy : Side::Sell},
                     {capacity, contra_capacity},
                     {party_id, contra_party_id},
                     {0x32, 0x33});
    p = encode_field_symbol(p, symbol);
    encode_deferred_publication(p, deferred_publication);
    return msg;
}
