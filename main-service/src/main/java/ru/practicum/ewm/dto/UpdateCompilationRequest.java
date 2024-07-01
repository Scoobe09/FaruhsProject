package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCompilationRequest implements Serializable {
	private final String title;

	private final Boolean pinned;

	private final Set<Long> events;

	@JsonIgnore
	public boolean isTitleNeedUpdate() {
		return title != null && !title.isBlank();
	}

	@JsonIgnore
	public boolean isPinnedFlagNeedUpdate() {
		return pinned != null;
	}

	@JsonIgnore
	public boolean isEventListNeedUpdate() {
		return events != null && !events.isEmpty();
	}
}