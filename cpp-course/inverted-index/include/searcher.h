#pragma once

#include <deque>
#include <memory>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <utility>
#include <vector>

class Searcher
{
public:
    using Filename = std::string; // or std::filesystem::path
    using Filenames = std::vector<Filename>;
    using SplitedQuery = std::vector<std::vector<std::string>>;
    using Words = std::vector<std::string>;
    using Storage = std::unordered_map<std::string, std::unordered_map<Filename, std::unordered_set<size_t>>>;
    using Texts = std::unordered_map<Filename, std::deque<std::string>>;

    // index modification
    void add_document(const Filename &, std::istream &);

    void remove_document(const Filename &);

    // queries
    class DocIterator
    {
    public:
        static constexpr Filenames::iterator npos = Filenames::iterator();

        using difference_type = std::ptrdiff_t;
        using value_type = const Filename;
        using pointer = value_type *;
        using reference = value_type &;
        using iterator_category = std::forward_iterator_tag;

        DocIterator(const std::shared_ptr<Filenames> & filenames)
            : m_filenames_storage(filenames)
            , m_iterator(m_filenames_storage->begin())
        {
        }

        DocIterator(const Filenames::iterator & iterator)
            : m_iterator(iterator)
        {
        }

        DocIterator()
            : m_iterator(npos)
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

        DocIterator & operator++()
        {
            ++m_iterator;
            return *this;
        }

        DocIterator operator++(int)
        {
            DocIterator temp = *this;
            operator++();
            return temp;
        }

        friend bool operator==(const DocIterator & lhs, const DocIterator & rhs)
        {
            return lhs.m_iterator == rhs.m_iterator;
        }

        friend bool operator!=(const DocIterator & lhs, const DocIterator & rhs)
        {
            return !(lhs == rhs);
        }

    private:
        std::shared_ptr<Filenames> m_filenames_storage;
        Filenames::iterator m_iterator;
    };

    class BadQuery : public std::exception
    {
    public:
        explicit BadQuery(const std::string & error_message)
            : m_error_message("Search error_message syntax error: " + error_message)
        {
        }

        explicit BadQuery(const std::string & error_message, const std::string & query)
            : m_error_message("Search error_message syntax error: " + error_message + " in query \'" + query + '\'')
        {
        }

        const char * what() const noexcept override
        {
            return m_error_message.c_str();
        }

    private:
        const std::string m_error_message;
    };

    std::pair<DocIterator, DocIterator> search(const std::string &) const;

    friend std::ostream & operator<<(std::ostream &, const Searcher &);

private:
    void remove_extra_chars(std::string &) const;
    void to_lower_case(std::string &) const;
    Words parse_ordered_words(const std::string &, size_t &) const;
    std::string parse_word(const std::string &, size_t &) const;
    SplitedQuery split_query(const std::string &) const;
    Filenames intersect(Filenames &, Filenames &) const;
    Filenames find_good_files(const Words &, Filenames &) const;
    Filenames find_files_with_order(const Filenames &, const Words &) const;

    Storage m_storage;
    Texts m_texts;
};
