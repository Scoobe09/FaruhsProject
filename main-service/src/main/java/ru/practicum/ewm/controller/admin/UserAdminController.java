package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.api.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserAdminController {
	private final UserService userService;

	@GetMapping
	public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
								  @RequestParam(defaultValue = "0") int from,
								  @RequestParam(defaultValue = "10") int size) {
		if (ids == null || ids.isEmpty()) {
			return userService.getAllUsers(from, size);
		} else {
			return userService.getUsersById(ids);
		}
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public UserDto registerUser(@RequestBody @Valid NewUserRequest newUserRequest) {
		return userService.addUser(newUserRequest);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long userId) {
		userService.delete(userId);
	}
}
