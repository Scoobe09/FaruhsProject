package ru.practicum.ewm.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;
import ru.practicum.ewm.stats.repository.StatsRepository;
import ru.practicum.ewm.stats.service.api.StatsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository hitRepository;

    @Override
    public void recordHit(EndpointHit hit) {
        hitRepository.saveHit(hit);
    }

    @Override
    public List<ViewStats> calculateViews(ViewsStatsRequest request) {
        return hitRepository.getIntervalStats(request);
    }
}
