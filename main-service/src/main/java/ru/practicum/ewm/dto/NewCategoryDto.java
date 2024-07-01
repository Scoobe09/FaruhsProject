package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data(staticConstructor = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCategoryDto implements Serializable {
	private @NotBlank String name;
}
