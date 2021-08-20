#include "primitives.h"

#include <algorithm>
#include <fstream>

namespace rbtree {

PointSet::PointSet(const std::string & filename)
{
    std::ifstream fin(filename);
    if (fin.is_open()) {
        double x, y;
        while (fin >> x >> y) {
            put(Point(x, y));
        }
    }
}

bool PointSet::empty() const
{
    return m_set.empty();
}

std::size_t PointSet::size() const
{
    return m_set.size();
}

void PointSet::put(const Point & point)
{
    m_set.insert(point);
}

bool PointSet::contains(const Point & point) const
{
    return m_set.find(point) != m_set.end();
}

std::pair<PointSet::iterator, PointSet::iterator> PointSet::range(const Rect & rect) const
{
    auto result = std::make_shared<PSet>(m_set);
    for (auto it = result->begin(); it != result->end();) {
        if (rect.contains(*it)) {
            ++it;
        }
        else {
            it = result->erase(it);
        }
    }
    return std::make_pair(result, result->end());
}

PointSet::iterator PointSet::begin() const
{
    return m_set.begin();
}

PointSet::iterator PointSet::end() const
{
    return m_set.end();
}

std::optional<Point> PointSet::nearest(const Point & point) const
{
    if (empty()) {
        return std::nullopt;
    }
    return *nearest(point, 1).first;
}

std::pair<PointSet::iterator, PointSet::iterator> PointSet::nearest(const Point & point, std::size_t k) const
{
    auto result = std::make_shared<PSet>();
    if (k >= size()) {
        return std::make_pair(begin(), end());
    }
    if (k != 0) {
        std::map<double, const Point> map;
        for (const Point & set_point : m_set) {
            const double distance = set_point.distance(point);
            if (map.size() < k) {
                map.insert(std::make_pair(distance, set_point));
            }
            else {
                double max_distance = map.rbegin()->first;
                if (max_distance > distance) {
                    map.erase(max_distance);
                    map.insert(std::make_pair(distance, set_point));
                }
            }
        }
        for (const auto & map_entries : map) {
            result->insert(map_entries.second);
        }
    }
    return std::make_pair(result, result->end());
}

std::ostream & operator<<(std::ostream & stm, const PointSet & point_set)
{
    stm << "rbtree::PointSet = {";
    for (const Point & point : point_set) {
        stm << point << " ";
    }
    return stm << "}";
}

} // namespace rbtree
