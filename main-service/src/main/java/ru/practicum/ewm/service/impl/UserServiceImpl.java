package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.api.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	@Override
	public List<UserDto> getAllUsers(int from, int size) {
		return userRepository.findAll(page(from, size))
				.stream()
				.map(Mapper::toUserDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<UserDto> getUsersById(List<Long> ids) {
		return userRepository.findAllById(ids)
				.stream()
				.map(Mapper::toUserDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserDto addUser(NewUserRequest newUserRequest) {
		User user = userRepository.save(Mapper.toNewUser(newUserRequest));
		return Mapper.toUserDto(user);
	}

	@Override
	@Transactional
	public void delete(long userId) {
		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
		} else {
			throw new NotFoundException("Пользователь", userId);
		}
	}

	private static PageRequest page(int from, int size) {
		return PageRequest.of(from > 0 ? from / size : 0, size);
	}
}
