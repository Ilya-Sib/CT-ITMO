#pragma once

#include <algorithm>
#include <cstddef>
#include <deque>
#include <new>
#include <ostream>

template <class Key, class KeyProvider, class Allocator>
class Cache
{
public:
    template <class... AllocArgs>
    Cache(const std::size_t cache_size, AllocArgs &&... alloc_args)
        : m_max_top_size(cache_size)
        , m_max_low_size(cache_size)
        , m_alloc(std::forward<AllocArgs>(alloc_args)...)
    {
    }

    std::size_t size() const
    {
        return m_low_queue.size() + m_top_queue.size();
    }

    bool empty() const
    {
        return m_low_queue.empty() && m_top_queue.empty();
    }

    template <class T>
    T & get(const Key & key);

    std::ostream & print(std::ostream & strm) const;

    friend std::ostream & operator<<(std::ostream & strm, const Cache & cache)
    {
        return cache.print(strm);
    }

private:
    const std::size_t m_max_top_size;
    const std::size_t m_max_low_size;
    Allocator m_alloc;
    std::deque<KeyProvider *> m_top_queue;
    std::deque<KeyProvider *> m_low_queue;
};

template <class Key, class KeyProvider, class Allocator>
template <class T>
inline T & Cache<Key, KeyProvider, Allocator>::get(const Key & key)
{
    auto it = std::find_if(m_top_queue.begin(), m_top_queue.end(), [&key](const KeyProvider * elem) {
        return *elem == key;
    });
    if (it != m_top_queue.end()) {
        m_top_queue.push_front(*it);
        m_top_queue.erase(it);
        return *static_cast<T *>(m_top_queue.front());
    }
    else {
        it = std::find_if(m_low_queue.begin(), m_low_queue.end(), [&key](const KeyProvider * elem) {
            return *elem == key;
        });
        if (it != m_low_queue.end()) {
            m_low_queue.push_back(*it);
            m_low_queue.erase(it);
            KeyProvider * tmp = nullptr;
            if (m_max_top_size == m_top_queue.size()) {
                tmp = m_top_queue.back();
                m_top_queue.pop_back();
            }
            m_top_queue.push_front(m_low_queue.back());
            m_low_queue.pop_back();
            if (tmp != nullptr) {
                m_low_queue.push_front(tmp);
            }
            return *static_cast<T *>(m_top_queue.front());
        }
        else {
            if (m_max_low_size == m_low_queue.size()) {
                m_alloc.template destroy<KeyProvider>(m_low_queue.back());
                m_low_queue.pop_back();
            }
            m_low_queue.push_front(m_alloc.template create<T>(key));
            return *static_cast<T *>(m_low_queue.front());
        }
    }
}

template <class Key, class KeyProvider, class Allocator>
inline std::ostream & Cache<Key, KeyProvider, Allocator>::print(std::ostream & strm) const
{
    strm << "Priority:";
    for (const auto x : m_top_queue) {
        strm << " " << *x;
    }
    strm << "\nRegular:";
    for (const auto x : m_low_queue) {
        strm << " " << *x;
    }
    strm << "\n";
    return strm;
}
