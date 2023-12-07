#include <iostream>
#include <format>
#include <fstream>
#include <string>
#include <vector>
#include <cstdint>
#include <map>
#include <sstream>
#include <algorithm>

struct SeedRan {
    int64_t start;
    int64_t range;
};

struct Mapping {
    int64_t src_start;
    int64_t dst_start;
    int64_t range;

    [[nodiscard]] bool idInMapping(int64_t id) const
    {
        return (id >= src_start) && (id < src_start + range);
    }

    [[nodiscard]] int64_t mapId(int64_t id) const
    {
        auto offset = id - src_start;
        return dst_start + offset;
    }
};

std::vector<std::string> get_input()
{
    std::ifstream file("input.txt");
    std::string str;
    std::vector<std::string> input;
    while (std::getline(file, str))
    {
        input.push_back(str);
    }
    return input;
}

std::vector<Mapping> get_section(const std::string& section, const std::vector<std::string>& input)
{
    std::vector<Mapping> result;

    bool reading = false;
    for (const auto & i : input)
    {
        if (reading && i.empty()) break;

        if (reading)
        {
            Mapping m {};
            std::stringstream sstr(i);
            std::string str;
            sstr >> str;
            m.dst_start = std::stoll(str);
            sstr >> str;
            m.src_start = std::stoll(str);
            sstr >> str;
            m.range = std::stoll(str);
            result.push_back(m);
        }

        if (i == section) reading = true;
    }

    return result;
}

std::vector<SeedRan> get_seeds(const std::string& line)
{
    std::vector<SeedRan> res;
    auto nums = line.substr(7);
    std::stringstream sstr(nums);
    std::string num;
    while (sstr >> num)
    {
        SeedRan sr {};
        sr.start = std::stoll(num);
        sstr >> num;
        sr.range = std::stoll(num);
        res.push_back(sr);
    }
    std::ranges::sort(res, [](auto& a, auto& b){return a.range < b.range;} );
    return res;
}

int64_t map_id(std::vector<Mapping>& mappings, int64_t id)
{
    for (auto& m : mappings)
    {
        if (m.idInMapping(id))
        {
            return m.mapId(id);
        }
    }

    return id;
}

int64_t solve_recurse(int64_t id, int64_t idx, std::map<std::string, std::vector<Mapping>>& sections, const std::vector<std::string>& section_keys)
{
    if (idx == section_keys.size()) return id;
    auto next = map_id(sections[section_keys[idx]], id);
    return solve_recurse(next, idx + 1, sections, section_keys);
}

int main()
{
    auto input = get_input();
    auto seeds = get_seeds(input[0]);

    std::vector<std::string> types = { "seed", "soil", "fertilizer", "water", "light", "temperature", "humidity", "location" };
    std::map<std::string, std::vector<Mapping>> sections;
    std::vector<std::string> section_keys;
    for (int32_t i = 0; i < types.size() - 1; i++)
    {
        auto t2t = std::format("{}-to-{}", types[i], types[i + 1]);
        section_keys.push_back(t2t);
        auto section = get_section(std::format("{} map:", t2t), input);
        sections.insert({t2t, section});
    }

    int64_t min = 0;
    for (auto& seed : seeds)
    {
        for (int64_t i = seed.start; i < seed.start + seed.range - 1; i++)
        {
            auto loc = solve_recurse(i, 0, sections, section_keys);
            if (i == seed.start) {
                min = loc;
            } else {
                min = std::min(loc, min);
            }
        }
        std::cout << std::format("Current min: {}", min) << std::endl;
    }
    std::cout << std::format("Answer: {}", min) << std::endl;

    return 0;
}
