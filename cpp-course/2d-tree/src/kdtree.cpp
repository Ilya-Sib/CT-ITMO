#include "primitives.h"

#include <algorithm>
#include <fstream>

namespace kdtree {

std::pair<PointSet::PNode, PointSet::PNode> PointSet::Node::get_ordered_children(const Point & point) const
{
    if (!m_left) {
        return std::make_pair(m_right, m_left);
    }
    if (!m_right) {
        return std::make_pair(m_left, m_right);
    }
    if (m_left->m_rect.distance(point) > m_right->m_rect.distance(point)) {
        return std::make_pair(m_right, m_left);
    }
    return std::make_pair(m_left, m_right);
}

// sizeof the comparator is also 1 byte but now it's more useful
struct LessComparator
{
    LessComparator(bool is_even)
        : m_is_even(is_even)
    {
    }

    bool operator()(const Point & p1, const Point & p2) const
    {
        return m_is_even ? p1.x() < p2.x() : p1.y() < p2.y();
    }

private:
    bool m_is_even;
};

void PointSet::get_tree(size_t left_bound, size_t right_bound, bool is_even, PVector & input)
{
    if (right_bound != left_bound) {
        std::sort(input.begin() + left_bound, input.begin() + right_bound, LessComparator(is_even));
        size_t medium = (right_bound + left_bound) / 2;
        m_tree.push_back(input[medium]);
        get_tree(left_bound, medium, !is_even, input);
        get_tree(medium + 1, right_bound, !is_even, input);
    }
}

PointSet::PointSet(const std::string & filename)
{
    std::ifstream fin(filename);
    if (fin.is_open()) {
        PVector input;
        double x, y;
        while (fin >> x >> y) {
            input.push_back(Point(x, y));
        }
        m_tree.reserve(input.size());
        get_tree(0, input.size(), true, input);
        for (const Point & point : m_tree) {
            put(point);
        }
    }
}

bool PointSet::empty() const
{
    return !m_root;
}

size_t PointSet::size() const
{
    return m_size;
}

void PointSet::put(const Point & point)
{
    put(m_root, point, MIN_VALUE, MIN_VALUE, MAX_VALUE, MAX_VALUE, true);
}

bool PointSet::contains(const Point & point) const
{
    return contains(m_root, point, true);
}

std::pair<PointSet::iterator, PointSet::iterator> PointSet::range(const Rect & rect) const
{
    auto result = std::make_shared<PVector>();
    range(m_root, rect, result);
    return std::make_pair(result, result->end());
}

PointSet::iterator PointSet::begin() const
{
    update_vector();
    return m_tree.begin();
}

PointSet::iterator PointSet::end() const
{
    update_vector();
    return m_tree.end();
}

std::optional<Point> PointSet::nearest(const Point & point) const
{
    if (empty()) {
        return std::nullopt;
    }
    return *nearest(point, 1).first;
}

std::pair<PointSet::iterator, PointSet::iterator> PointSet::nearest(const Point & point, size_t k) const
{
    auto result = std::make_shared<PVector>();
    if (k >= m_size) {
        return std::make_pair(begin(), end());
    }
    if (k != 0) {
        std::map<double, const Point> map;
        nearest(m_root, point, k, map);
        result->reserve(map.size());
        for (const auto & map_entries : map) {
            result->push_back(map_entries.second);
        }
    }
    return std::make_pair(result, result->end());
}

void PointSet::put(PNode & node, const Point & point, double xmin, double ymin, double xmax, double ymax, bool is_even)
{
    if (!node) {
        ++m_size;
        node = std::make_shared<Node>(point, Rect(Point(xmin, ymin), Point(xmax, ymax)));
        return;
    }
    if (node->m_point == point) {
        return;
    }
    if (LessComparator(is_even)(point, node->m_point)) {
        put(node->m_left, point, xmin, ymin, is_even ? node->m_point.x() : xmax, is_even ? ymax : node->m_point.y(), !is_even);
    }
    else {
        put(node->m_right, point, is_even ? node->m_point.x() : xmin, is_even ? ymin : node->m_point.y(), xmax, ymax, !is_even);
    }
}

bool PointSet::contains(const PNode & node, const Point & point, bool is_even) const
{
    if (!node) {
        return false;
    }
    if (node->m_point == point) {
        return true;
    }
    if (LessComparator(is_even)(point, node->m_point)) {
        return contains(node->m_left, point, !is_even);
    }
    return contains(node->m_right, point, !is_even);
}

void PointSet::range(const PNode & node, const Rect & rect, std::shared_ptr<PVector> & result) const
{
    if (node && node->m_rect.intersects(rect)) {
        if (rect.contains(node->m_point)) {
            result->push_back(node->m_point);
        }
        range(node->m_left, rect, result);
        range(node->m_right, rect, result);
    }
}

void PointSet::update_vector() const
{
    if (m_tree.size() < m_size) {
        m_tree.clear();
        tree_to_vector(m_root);
    }
}

void PointSet::tree_to_vector(const PNode & node) const
{
    if (node) {
        m_tree.push_back(node->m_point);
        tree_to_vector(node->m_left);
        tree_to_vector(node->m_right);
    }
}

void PointSet::nearest(const PNode & node, const Point & point, size_t k, std::map<double, const Point> & map) const
{
    double max_distance = map.empty() ? MAX_VALUE : map.rbegin()->first;
    if (node) {
        if (map.size() == k && max_distance < node->m_rect.distance(point)) {
            // We took k points and the distance to the rect
            // is greater than the max distance to point
            return;
        }
        double distance = node->m_point.distance(point);
        if (map.size() < k) {
            map.insert(std::make_pair(distance, node->m_point));
        }
        else if (max_distance > distance) {
            map.erase(max_distance);
            map.insert(std::make_pair(distance, node->m_point));
        }
        const auto & [first, second] = node->get_ordered_children(point);
        nearest(first, point, k, map);
        nearest(second, point, k, map);
    }
}

std::ostream & operator<<(std::ostream & stm, const PointSet & point_set)
{
    stm << "kdtree::PointSet = {";
    for (const Point & point : point_set) {
        stm << point << " ";
    }
    return stm << "}";
}

} // namespace kdtree
