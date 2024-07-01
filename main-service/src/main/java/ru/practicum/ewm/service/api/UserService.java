package ru.practicum.ewm.service.api;

import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {
	List<UserDto> getAllUsers(int from, int size);

	List<UserDto> getUsersById(List<Long> ids);

	UserDto addUser(NewUserRequest registrationRequest);

	void delete(long userId);
}
