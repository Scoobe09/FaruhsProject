package ru.practicum.ewm.stats.service.api;

import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;

import java.util.List;

public interface StatsService {
    void recordHit(EndpointHit hit);

    List<ViewStats> calculateViews(ViewsStatsRequest request);
}
