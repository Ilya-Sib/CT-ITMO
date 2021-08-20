#pragma once

#include <cstddef>
#include <functional>
#include <initializer_list>
#include <memory>
#include <new>
#include <stdlib.h>

namespace pool {

class Pool;

Pool * create_pool(std::size_t count, std::initializer_list<std::size_t> sizes);

void * allocate(Pool & pool, std::size_t n);

void deallocate(Pool & pool, const void * ptr);

void destroy_pool(Pool * pool);

} // namespace pool

class PoolAllocator
{
public:
    PoolAllocator(const std::size_t count, std::initializer_list<std::size_t> sizes)
        : m_pool(pool::create_pool(count, sizes), pool::destroy_pool)
    {
    }

    void * allocate(const std::size_t n)
    {
        return pool::allocate(*m_pool, n);
    }

    void deallocate(const void * ptr)
    {
        pool::deallocate(*m_pool, ptr);
    }

private:
    std::unique_ptr<pool::Pool, decltype(&pool::destroy_pool)> m_pool;
};
