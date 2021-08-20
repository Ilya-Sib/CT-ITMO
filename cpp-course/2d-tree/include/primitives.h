#pragma once

#include <limits>
#include <map>
#include <memory>
#include <optional>
#include <ostream>
#include <set>
#include <vector>

class Point
{
public:
    Point(double x, double y);
    Point(const Point &);

    double x() const;
    double y() const;
    double distance(const Point &) const;

    bool operator<(const Point &) const;
    bool operator>(const Point &) const;
    bool operator<=(const Point &) const;
    bool operator>=(const Point &) const;
    bool operator==(const Point &) const;
    bool operator!=(const Point &) const;
    Point & operator=(const Point &);

    friend std::ostream & operator<<(std::ostream &, const Point &);

private:
    double m_x, m_y;
};

class Rect
{
public:
    Rect(const Point & left_bottom, const Point & right_top);

    double xmin() const;
    double ymin() const;
    double xmax() const;
    double ymax() const;
    double distance(const Point &) const;

    bool contains(const Point &) const;
    bool intersects(const Rect &) const;

private:
    const Point m_left_bottom, m_right_top;
};

namespace my_iterator {

template <class T>
class iterator
{
public:
    static typename T::iterator npos()
    {
        return T::iterator();
    }

    using difference_type = std::ptrdiff_t;
    using value_type = Point;
    using pointer = const value_type *;
    using reference = const value_type &;
    using iterator_category = std::forward_iterator_tag;

    iterator(const std::shared_ptr<T> & shared_t)
        : m_shared_t(shared_t)
        , m_iterator(m_shared_t->begin())
    {
    }

    iterator(const typename T::iterator & iterator)
        : m_iterator(iterator)
    {
    }

    iterator()
        : m_iterator(npos())
    {
    }

    reference operator*() const
    {
        return *m_iterator;
    }

    pointer operator->() const
    {
        return m_iterator.operator->();
    }

    iterator & operator++()
    {
        ++m_iterator;
        return *this;
    }

    iterator operator++(int)
    {
        iterator temp = *this;
        operator++();
        return temp;
    }

    friend bool operator==(const iterator & lhs, const iterator & rhs)
    {
        return lhs.m_iterator == rhs.m_iterator;
    }

    friend bool operator!=(const iterator & lhs, const iterator & rhs)
    {
        return !(lhs == rhs);
    }

private:
    std::shared_ptr<T> m_shared_t;
    typename T::iterator m_iterator;
};

} // namespace my_iterator

namespace rbtree {

class PointSet
{
    using PSet = std::set<Point>;

public:
    using iterator = my_iterator::iterator<PSet>;

    PointSet(const std::string & filename = {});

    bool empty() const;
    std::size_t size() const;
    void put(const Point &);
    bool contains(const Point &) const;

    // second iterator points to an element out of range
    std::pair<iterator, iterator> range(const Rect &) const;
    iterator begin() const;
    iterator end() const;

    std::optional<Point> nearest(const Point &) const;
    // second iterator points to an element out of range
    std::pair<iterator, iterator> nearest(const Point &, std::size_t) const;

    friend std::ostream & operator<<(std::ostream &, const PointSet &);

private:
    PSet m_set;
};

} // namespace rbtree

namespace kdtree {

class PointSet
{
public:
    struct Node;
    using PNode = std::shared_ptr<Node>;
    using PVector = std::vector<Point>;

    static constexpr double MIN_VALUE = std::numeric_limits<double>::min();
    static constexpr double MAX_VALUE = std::numeric_limits<double>::max();

    struct Node
    {
        const Point m_point;
        const Rect m_rect;
        PNode m_left;
        PNode m_right;

        Node(const Point & point, const Rect & rect)
            : m_point(point)
            , m_rect(rect)
        {
        }

        std::pair<PNode, PNode> get_ordered_children(const Point & point) const;
    };

    using iterator = my_iterator::iterator<PVector>;

    PointSet(const std::string & filename = {});

    bool empty() const;
    std::size_t size() const;
    void put(const Point &);
    bool contains(const Point &) const;

    std::pair<iterator, iterator> range(const Rect &) const;
    iterator begin() const;
    iterator end() const;

    std::optional<Point> nearest(const Point &) const;
    std::pair<iterator, iterator> nearest(const Point &, std::size_t) const;

    friend std::ostream & operator<<(std::ostream &, const PointSet &);

private:
    void get_tree(size_t, size_t, bool, PVector &);
    void put(PNode &, const Point &, double, double, double, double, bool);
    bool contains(const PNode &, const Point &, bool) const;
    void range(const PNode &, const Rect &, std::shared_ptr<PVector> &) const;
    void update_vector() const;
    void tree_to_vector(const PNode & node) const;
    void nearest(const PNode &, const Point &, size_t, std::map<double, const Point> &) const;

    mutable PVector m_tree;
    PNode m_root;
    size_t m_size = 0;
};

} // namespace kdtree
