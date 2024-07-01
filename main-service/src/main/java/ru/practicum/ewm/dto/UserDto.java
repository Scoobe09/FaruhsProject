package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class UserDto implements Serializable {

	private final Long id;

	private final @NotBlank String name;

	private final @Email String email;
}
