package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.api.CompilationService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
	private final CompilationRepository repository;
	private final EventRepository eventRepository;

	@Override
	public List<CompilationDto> getAll(int from, int size) {
		return repository.findAll(page(from, size))
				.stream()
				.map(Mapper::toCompilationDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<CompilationDto> getByPinFlag(boolean pinned, int from, int size) {
		return repository.findByPinned(pinned, page(from, size))
				.stream()
				.map(Mapper::toCompilationDto)
				.collect(Collectors.toList());
	}

	@Override
	public CompilationDto getById(long compId) {
		return repository
				.findById(compId)
				.map(Mapper::toCompilationDto)
				.orElseThrow(() -> new NotFoundException("Подборка", compId));
	}

	@Override
	@Transactional
	public CompilationDto save(NewCompilationDto compilationDto) {
		List<Event> events;
		if (compilationDto.getEvents() == null || compilationDto.getEvents().isEmpty()) {
			events = new ArrayList<>();
		} else {
			events = eventRepository.findAllById(compilationDto.getEvents());
		}
		Compilation comp = repository.save(Mapper.toNewCompilation(compilationDto, events));
		return Mapper.toCompilationDto(comp);
	}

	@Override
	@Transactional
	public void addEvents(long compId, Collection<Long> eventIds) {
		Compilation compilation = repository.findById(compId)
				.orElseThrow(() -> new NotFoundException("Подборка", compId));

		List<Event> events = eventRepository.findAllById(eventIds);

		compilation.getEvents().addAll(events);

		repository.save(compilation);
	}

	@Override
	@Transactional
	public void removeEvents(long compId, Set<Long> eventIds) {
		Compilation compilation = repository.findById(compId)
				.orElseThrow(() -> new NotFoundException("Подборка", compId));

		compilation.getEvents().removeIf(event -> eventIds.contains(event.getId()));
		repository.save(compilation);
	}

	@Override
	@Transactional
	public void delete(long compId) {
		if (repository.existsById(compId)) {
			repository.deleteById(compId);
		} else {
			throw new NotFoundException("Подборка", compId);
		}
	}

	@Transactional
	@Override
	public CompilationDto update(long compId, UpdateCompilationRequest updateRequest) {
		Compilation compilation = repository.findById(compId)
				.orElseThrow(() -> new NotFoundException("Подборка", compId));
		if (updateRequest.isTitleNeedUpdate()) {
			compilation.setTitle(updateRequest.getTitle());
		}
		if (updateRequest.isPinnedFlagNeedUpdate()) {
			compilation.setPinned(updateRequest.getPinned());
		}

		if (updateRequest.isEventListNeedUpdate()) {
			List<Event> events = eventRepository.findAllById(updateRequest.getEvents());
			compilation.setEvents(new HashSet<>(events));
		}

		repository.save(compilation);

		return Mapper.toCompilationDto(compilation);
	}

	private static PageRequest page(int from, int size) {
		return PageRequest.of(from > 0 ? from / size : 0, size);
	}
}
