package ru.practicum.ewm.service.api;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CompilationService {
	List<CompilationDto> getAll(int from, int size);

	List<CompilationDto> getByPinFlag(boolean pinned, int from, int size);

	CompilationDto getById(long compId);

	CompilationDto save(NewCompilationDto compilationDto);

	void addEvents(long compId, Collection<Long> eventIds);

	void removeEvents(long compId, Set<Long> eventIds);

	void delete(long compId);

    CompilationDto update(long compId, UpdateCompilationRequest updateRequest);
}
