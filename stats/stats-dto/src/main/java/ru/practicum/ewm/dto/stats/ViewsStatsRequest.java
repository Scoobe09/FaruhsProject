package ru.practicum.ewm.dto.stats;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@ToString
@Builder(toBuilder = true)
public class ViewsStatsRequest {
	@Singular("uri")
	private Set<String> uris;

	@Builder.Default
	private LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);

	@Builder.Default
	private LocalDateTime end = LocalDateTime.now();

	private boolean unique;

	private Integer limit;

	private String application;

	public boolean hasUriCondition() {
		return uris != null && !uris.isEmpty();
	}

	public boolean hasLimitCondition() {
		return limit != null && limit != 0;
	}
}
