package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.core.lang.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateEventAdminRequest extends UpdateEventBaseRequest {
	private @Nullable StateAction stateAction;

	private @Nullable String rejectionReason;

	@JsonIgnore
	public boolean isStateNeedUpdate() {
		return stateAction != null;
	}

	public enum StateAction {
		PUBLISH_EVENT,
		REJECT_EVENT
	}
}
