package ru.practicum.ewm.stats.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsRepository {
	private final JdbcTemplate jdbc;

	private static final String INSERT_HIT_SQL =
			"INSERT INTO endpoint_hit(app_id, uri, ip, ts) VALUES (?, ?, ?, ?)";

	private static final String VIEWS_STATS_SQL =
			"SELECT a.name app, h.uri, #1# hits \n" +
					"FROM endpoint_hit as h \n" +
					"join apps as a on a.id = h.app_id \n" +
					"where h.ts >= ? and h.ts <= ? #2# \n" +
					"group by a.name, h.uri \n" +
					"order by hits desc";

	@Transactional
	public void saveHit(EndpointHit hit) {
		Optional<Integer> appIdOpt = getAppId(hit.getApp());

		if (appIdOpt.isEmpty()) {
			jdbc.update("INSERT INTO apps(name) VALUES(?)", hit.getApp());
			appIdOpt = getAppId(hit.getApp());
		}

		jdbc.update(INSERT_HIT_SQL, appIdOpt.get(), hit.getUri(), hit.getIp(), hit.getTimestamp());
	}

	public List<ViewStats> getIntervalStats(ViewsStatsRequest request) {
		return jdbc.query(
				getViewsStatsSql(request),
				BeanPropertyRowMapper.newInstance(ViewStats.class),
				request.getStart(),
				request.getEnd()
		);
	}

	private Optional<Integer> getAppId(String application) {
		try {
			Integer appId = jdbc.queryForObject("SELECT id FROM apps where name like ?", Integer.class, application);
			return Optional.ofNullable(appId);
		} catch (EmptyResultDataAccessException e) {
			log.debug("No record found for application [" + application + "]", e);
			return Optional.empty();
		}
	}

	private String getViewsStatsSql(ViewsStatsRequest request) {
		String sql;
		if (request.isUnique()) {
			sql = VIEWS_STATS_SQL.replace("#1#", "count(distinct h.ip)");
		} else {
			sql = VIEWS_STATS_SQL.replace("#1#", "count(*)");
		}
		if (request.hasUriCondition()) {
			String uriString = request.getUris()
					.stream()
					.map(path -> "'" + path + "'")
					.collect(Collectors.joining(","));
			sql = sql.replace("#2#", " and h.uri in (" + uriString + ")");
		} else {
			sql = sql.replace("#2#", "");
		}
		return request.hasLimitCondition() ? sql + " limit " + request.getLimit() : sql;
	}
}
