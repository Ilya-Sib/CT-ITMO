#include "primitives.h"

#include <cmath>
#include <functional>

Point::Point(double x, double y)
    : m_x(x)
    , m_y(y)
{
}

Point::Point(const Point &) = default;

double Point::x() const
{
    return m_x;
}
double Point::y() const
{
    return m_y;
}
double Point::distance(const Point & that) const
{
    return std::hypot(m_x - that.m_x, m_y - that.m_y);
}

bool Point::operator<(const Point & that) const
{
    return (m_y < that.m_y) || (m_y == that.m_y && m_x < that.m_x);
}
bool Point::operator>(const Point & that) const
{
    return that < *this;
}
bool Point::operator<=(const Point & that) const
{
    return !(*this > that);
}
bool Point::operator>=(const Point & that) const
{
    return !(*this < that);
}
bool Point::operator==(const Point & that) const
{
    return m_x == that.m_x && m_y == that.m_y;
}
bool Point::operator!=(const Point & that) const
{
    return !(*this == that);
}
Point & Point::operator=(const Point &) = default;
std::ostream & operator<<(std::ostream & stm, const Point & point)
{
    return stm << "(x = " << point.m_x << ", y = " << point.m_y << ")";
}

Rect::Rect(const Point & left_bottom, const Point & right_top)
    : m_left_bottom(left_bottom)
    , m_right_top(right_top)
{
}

double Rect::xmin() const
{
    return m_left_bottom.x();
}
double Rect::ymin() const
{
    return m_left_bottom.y();
}
double Rect::xmax() const
{
    return m_right_top.x();
}
double Rect::ymax() const
{
    return m_right_top.y();
}
double Rect::distance(const Point & point) const
{
    double x = std::max(xmin() - point.x(), point.x() - xmax());
    double y = std::max(ymin() - point.y(), point.y() - ymax());
    return std::hypot(std::max(x, 0.0), std::max(y, 0.0));
}

bool Rect::contains(const Point & point) const
{
    return xmin() <= point.x() && point.x() <= xmax() && ymin() <= point.y() && point.y() <= ymax();
}
bool Rect::intersects(const Rect & that) const
{
    return !(xmin() > that.xmax() || xmax() < that.xmin() || ymin() > that.ymax() || ymax() < that.ymin());
}
