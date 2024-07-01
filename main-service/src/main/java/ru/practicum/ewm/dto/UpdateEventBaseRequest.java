package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateEventBaseRequest implements Serializable {
	@Size(min = 3, max = 120)
	private @Nullable String title;

	@Size(min = 20, max = 2000)
	private @Nullable String annotation;

	@Size(min = 20, max = 7000)
	private @Nullable String description;

	private @Nullable Long category;

	private @Nullable Integer participantLimit;

	private @Nullable Boolean paid;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private @Nullable LocalDateTime eventDate;

	private @Nullable Location location;

	private @Nullable Boolean requestModeration;

	@JsonIgnore
	public boolean isTitleNeedUpdate() {
		return title != null && !title.isBlank();
	}

	@JsonIgnore
	public boolean isAnnotationNeedUpdate() {
		return annotation != null && !annotation.isBlank();
	}

	@JsonIgnore
	public boolean isDescriptionNeedUpdate() {
		return description != null && !description.isBlank();
	}

	@JsonIgnore
	public boolean isCategoryNeedUpdate() {
		return category != null;
	}

	@JsonIgnore
	public boolean isParticipantLimitNeedUpdate() {
		return participantLimit != null;
	}

	@JsonIgnore
	public boolean isPaidFlagNeedUpdate() {
		return paid != null;
	}

	@JsonIgnore
	public boolean isEventDateNeedUpdate() {
		return eventDate != null;
	}

	@JsonIgnore
	public boolean isLocationNeedUpdate() {
		return location != null;
	}

	@JsonIgnore
	public boolean isRequestModerationNeedUpdate() {
		return requestModeration != null;
	}
}
