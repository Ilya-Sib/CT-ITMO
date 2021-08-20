#include "SeamCarver.h"

#include <cmath>

SeamCarver::SeamCarver(Image image)
    : m_image(std::move(image))
{
}

const Image & SeamCarver::GetImage() const
{
    return m_image;
}

size_t SeamCarver::GetImageWidth() const
{
    return m_image.m_table.size();
}

size_t SeamCarver::GetImageHeight() const
{
    return m_image.m_table[0].size();
}

double FindDelta(Image::Pixel p1, Image::Pixel p2)
{
    return pow(p1.m_red - p2.m_red, 2) +
            pow(p1.m_green - p2.m_green, 2) +
            pow(p1.m_blue - p2.m_blue, 2);
}

double SeamCarver::GetPixelEnergy(size_t columnId, size_t rowId) const
{
    Image::Pixel left = m_image.GetPixel((columnId - 1 + GetImageWidth()) % GetImageWidth(), rowId);
    Image::Pixel right = m_image.GetPixel((columnId + 1 + GetImageWidth()) % GetImageWidth(), rowId);
    double dx = FindDelta(left, right);
    Image::Pixel down = m_image.GetPixel(columnId, (rowId - 1 + GetImageHeight()) % GetImageHeight());
    Image::Pixel up = m_image.GetPixel(columnId, (rowId + 1 + GetImageHeight()) % GetImageHeight());
    double dy = FindDelta(down, up);
    return sqrt(dx + dy);
}

SeamCarver::Seam SeamCarver::FindHorizontalSeam() const
{
    std::vector<std::vector<double>> energy_sum =
            FindEnergySum(GetImageHeight(), GetImageWidth(), true);
    return FindSeam(energy_sum);
}

SeamCarver::Seam SeamCarver::FindVerticalSeam() const
{
    std::vector<std::vector<double>> energy_sum =
            FindEnergySum(GetImageWidth(), GetImageHeight(), false);
    return FindSeam(energy_sum);
}

void SeamCarver::RemoveHorizontalSeam(const Seam & seam)
{
    for (size_t i = 0; i < GetImageWidth(); ++i) {
        for (size_t j = 0; j < GetImageHeight() - 1; ++j) {
            m_image.m_table[i][j] = seam[i] > j ? m_image.GetPixel(i, j)
                                                : m_image.GetPixel(i, j + 1);
        }
    }
    for (size_t i = 0; i < GetImageWidth(); ++i) {
        m_image.m_table[i].pop_back();
    }
}

void SeamCarver::RemoveVerticalSeam(const Seam & seam)
{
    for (size_t i = 0; i < GetImageWidth() - 1; ++i) {
        for (size_t j = 0; j < GetImageHeight(); ++j) {
            m_image.m_table[i][j] = seam[j] > i ? m_image.GetPixel(i, j)
                                                : m_image.GetPixel(i + 1, j);
        }
    }
    m_image.m_table.pop_back();
}

SeamCarver::Seam SeamCarver::FindSeam(const std::vector<std::vector<double>> & energy_sum) const
{
    size_t result_size = energy_sum[0].size();
    Seam result = Seam(result_size);
    double min_energy = energy_sum[0][result_size - 1];
    for (size_t i = 1; i < energy_sum.size(); ++i) {
        if (energy_sum[i][result_size - 1] < min_energy) {
            min_energy = energy_sum[i][result_size - 1];
            result[result_size - 1] = i;
        }
    }
    for (int i = result_size - 2; i >= 0; --i) {
        size_t prev = result[i + 1];
        result[i] = prev;
        if (prev != 0 && energy_sum[prev][i] > energy_sum[prev - 1][i]) {
            result[i] = prev - 1;
        }
        if (prev != energy_sum.size() - 1 &&
            energy_sum[result[i]][i] > energy_sum[prev + 1][i]) {
            result[i] = prev + 1;
        }
    }
    return result;
}
std::vector<std::vector<double>> SeamCarver::FindEnergySum(size_t width, size_t height, bool isHorizontal) const
{
    std::vector<std::vector<double>> result(width, std::vector<double>(height));
    for (size_t j = 0; j < height; ++j) {
        for (size_t i = 0; i < width; ++i) {
            if (j != 0) {
                result[i][j] = result[i][j - 1];
            }
            if (i != 0) {
                result[i][j] = std::min(result[i][j], result[i - 1][j - 1]);
            }
            if (i != width - 1) {
                result[i][j] = std::min(result[i][j], result[i + 1][j - 1]);
            }
            result[i][j] += GetPixelEnergy(isHorizontal ? j : i, isHorizontal ? i : j);
        }
    }
    return result;
}
