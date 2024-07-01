package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.service.api.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompilationPublicController {
	private final CompilationService compService;

	@GetMapping
	public List<CompilationDto> getCompilations(
			@RequestParam(required = false) Boolean pinned,
			@RequestParam(defaultValue = "0") int from,
			@RequestParam(defaultValue = "10") int size) {
		if (pinned == null) {
			return compService.getAll(from, size);
		}
		return compService.getByPinFlag(pinned, from, size);
	}

	@GetMapping("/{compId}")
	public CompilationDto getCompilation(@PathVariable long compId) {
		return compService.getById(compId);
	}
}
