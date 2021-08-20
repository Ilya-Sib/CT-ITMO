#include "allocator.h"

#include <deque>
#include <unordered_map>

namespace pool {

class Pool
{
public:
    Pool(std::size_t count, std::initializer_list<std::size_t> sizes)
        : m_index_to_size(sizes)
        , m_storage(sizes.size(), std::vector<std::byte>(count))
        , m_unused_map(sizes.size())
    {
        auto it = sizes.begin();
        for (size_t i = 0; i < sizes.size(); ++i, ++it) {
            m_size_to_index.insert(std::make_pair(*it, i));
            for (size_t j = 0; j < count / *it; ++j) {
                m_unused_map[i].push_back(j);
            }
        }
    }

    void * allocate(size_t n);
    void deallocate(const void * ptr);

private:
    static constexpr size_t npos = static_cast<size_t>(-1);

    size_t find_empty_place(size_t ind);
    size_t find_index(const std::byte * ptr);

    std::unordered_map<size_t, size_t> m_size_to_index;
    const std::vector<size_t> m_index_to_size;
    std::vector<std::vector<std::byte>> m_storage;
    std::vector<std::vector<size_t>> m_unused_map;
};

size_t Pool::find_empty_place(const size_t ind)
{
    return m_unused_map[ind].empty() ? npos : m_unused_map[ind].back();
}

void * Pool::allocate(const size_t n)
{
    auto it = m_size_to_index.find(n);
    if (it != m_size_to_index.end()) {
        size_t ind = it->second;
        const size_t pos = find_empty_place(ind);
        if (pos != npos) {
            m_unused_map[ind].pop_back();
            return &m_storage[ind][pos * n];
        }
    }
    throw std::bad_alloc{};
}

void Pool::deallocate(const void * ptr)
{
    auto b_ptr = static_cast<const std::byte *>(ptr);
    size_t ind = find_index(b_ptr);
    const size_t offset = (b_ptr - &m_storage[ind][0]) / m_index_to_size[ind];
    m_unused_map[ind].push_back(offset);
}

size_t Pool::find_index(const std::byte * ptr)
{
    for (size_t i = 0; i < m_storage.size(); ++i) {
        if (&m_storage[i].front() <= ptr && ptr <= &m_storage[i].back()) {
            return i;
        }
    }
    return 0;
}

Pool * create_pool(const std::size_t count, const std::initializer_list<std::size_t> sizes)
{
    return new Pool(count, sizes);
}

void * allocate(Pool & pool, const size_t n)
{
    return pool.allocate(n);
}

void deallocate(Pool & pool, const void * ptr)
{
    pool.deallocate(ptr);
}

void destroy_pool(Pool * pool)
{
    delete pool;
}

} // namespace pool
