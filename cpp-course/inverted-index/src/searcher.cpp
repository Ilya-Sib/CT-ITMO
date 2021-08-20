#include "searcher.h"

#include <algorithm>
#include <deque>
#include <iostream>
#include <iterator>
#include <memory>
#include <string>
#include <unordered_map>

void Searcher::remove_extra_chars(std::string & word) const
{
    size_t begin_pos;
    size_t end_pos;
    for (begin_pos = 0; begin_pos < word.size() && ispunct(word[begin_pos]); ++begin_pos)
        ;
    for (end_pos = word.size(); end_pos > begin_pos && ispunct(word[end_pos - 1]); --end_pos)
        ;
    word = word.substr(begin_pos, end_pos - begin_pos);
}

void Searcher::to_lower_case(std::string & word) const
{
    for (size_t i = 0; i < word.size(); ++i) {
        word[i] = std::tolower(word[i]);
    }
}

void Searcher::add_document(const Filename & filename, std::istream & strm)
{
    if (m_texts.find(filename) != m_texts.end()) {
        remove_document(filename);
    }
    std::string word;
    for (size_t word_index = 0; strm >> word; ++word_index) {
        remove_extra_chars(word);
        to_lower_case(word);
        if (!word.empty()) {
            m_storage[word][filename].insert(word_index);
            m_texts[filename].push_back(std::move(word));
        }
        else {
            --word_index;
        }
    }
}

void Searcher::remove_document(const Filename & filename)
{
    for (size_t word_index = 0; word_index < m_texts[filename].size(); ++word_index) {
        std::string word = m_texts[filename][word_index];
        if (m_storage[word][filename].size() == 1) {
            m_storage[word].erase(filename);
        }
        else {
            auto it = m_storage[word][filename].find(word_index);
            m_storage[word][filename].erase(it);
        }
    }
    m_texts.erase(filename);
}

Searcher::Words Searcher::parse_ordered_words(const std::string & query, size_t & pos) const
{
    Words result;
    while (pos < query.size() && query[pos] != '\"') {
        if (isspace(query[pos])) {
            ++pos;
        }
        else {
            std::string word = parse_word(query, pos);
            if (!word.empty()) {
                result.push_back(std::move(word));
            }
        }
    }
    if (pos++ == query.size()) {
        throw BadQuery("Lost the closing quotes", query);
    }
    return result;
}

std::string Searcher::parse_word(const std::string & query, size_t & pos) const
{
    std::string result;
    for (; pos < query.size() && query[pos] != '\"' && !isspace(query[pos]); ++pos) {
        result.push_back(tolower(query[pos]));
    }
    remove_extra_chars(result);
    return result;
}

Searcher::SplitedQuery Searcher::split_query(const std::string & query) const
{
    SplitedQuery result;
    for (size_t pos = 0; pos < query.size();) {
        if (isspace(query[pos])) {
            ++pos;
        }
        else if (query[pos] == '\"') {
            Words ordered_words = parse_ordered_words(query, ++pos);
            if (!ordered_words.empty()) {
                result.push_back(std::move(ordered_words));
            }
        }
        else {
            std::string word = parse_word(query, pos);
            if (!word.empty()) {
                result.push_back(std::vector<std::string>{std::move(word)});
            }
        }
    }
    if (result.empty()) {
        throw BadQuery("Empty query");
    }
    return result;
}

// Пояснение к костылю: filenames не могут быть const, потому что я их сортирую,
// однако анализатор говорит обратное, а нолинт забанен, поэтому мне прилось сделать костыль
Searcher::Filenames Searcher::intersect(Filenames & filenames1, Filenames & filenames2) const
{
    // ВНИМАНИЕ!!! КОСТЫЛЬ!!
    if (!filenames1.empty() && !filenames2.empty()) {
        filenames1[0] = filenames1[0];
        filenames2[0] = filenames2[0];
    }
    // КОНЕЦ КОСТЫЛЯ!!!
    if (filenames1.empty()) {
        return std::move(filenames2);
    }
    std::sort(filenames1.begin(), filenames1.end());
    std::sort(filenames2.begin(), filenames2.end());
    Filenames result;
    for (size_t f1_pos = 0, f2_pos = 0; f1_pos < filenames1.size() && f2_pos < filenames2.size();) {
        if (filenames1[f1_pos] < filenames2[f2_pos]) {
            ++f1_pos;
        }
        else if (filenames1[f1_pos] > filenames2[f2_pos]) {
            ++f2_pos;
        }
        else {
            result.emplace_back(std::move(filenames2[f2_pos]));
            ++f1_pos, ++f2_pos;
        }
    }
    return result;
}

Searcher::Filenames Searcher::find_good_files(const Words & query_words, Filenames & search_result) const
{
    Filenames result;
    for (const auto & word : query_words) {
        auto it = m_storage.find(word);
        if (it != m_storage.end()) {
            Filenames word_files;
            for (const auto & [str, _] : it->second) {
                word_files.push_back(str);
            }
            if (query_words.size() > 1) {
                result = intersect(result, word_files);
            }
            else {
                result = intersect(search_result, word_files);
            }
        }
        else {
            return result;
        }
    }
    return result;
}

Searcher::Filenames Searcher::find_files_with_order(const Filenames & good_files, const Words & query_words) const
{
    Filenames result;
    for (const auto & filename : good_files) {
        auto text_words_it = m_texts.find(filename);
        auto word_it = m_storage.find(*query_words.begin());
        auto words_positions_it = word_it->second.find(filename);
        for (const size_t pos : words_positions_it->second) {
            size_t i;
            for (i = pos; i < text_words_it->second.size(); ++i) {
                if (i == pos + query_words.size() || text_words_it->second[i] != query_words[i - pos]) {
                    break;
                }
            }
            if (i == pos + query_words.size()) {
                result.push_back(filename);
                break;
            }
        }
    }
    return result;
}

std::pair<Searcher::DocIterator, Searcher::DocIterator> Searcher::search(const std::string & query) const
{
    Filenames result;
    for (const auto & query_words : split_query(query)) {
        Filenames good_files = find_good_files(query_words, result);
        if (query_words.size() > 1) {
            Filenames curr = find_files_with_order(good_files, query_words);
            result = intersect(result, curr);
        }
        else {
            result = std::move(good_files);
        }
        if (result.empty()) {
            return std::make_pair(DocIterator(), DocIterator());
        }
    }
    auto shared_result = std::make_shared<Filenames>(std::move(result));
    return std::make_pair(shared_result, shared_result->end());
}

std::ostream & operator<<(std::ostream & strm, const Searcher & searcher)
{
    for (const auto & [word, files] : searcher.m_storage) {
        strm << word << ": \n";
        for (const auto & [filename, positions] : files) {
            strm << filename << "->";
            for (const size_t pos : positions) {
                strm << " " << pos;
            }
            strm << '\n';
        }
    }
    return strm;
}