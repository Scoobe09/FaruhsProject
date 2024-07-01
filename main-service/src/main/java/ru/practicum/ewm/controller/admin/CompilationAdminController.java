package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;
import ru.practicum.ewm.service.api.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompilationAdminController {
	private final CompilationService compService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
		return compService.save(compilationDto);
	}

	@PatchMapping("/{compId}")
	public CompilationDto updateCompilation(@PathVariable long compId,
											@Valid @RequestBody UpdateCompilationRequest updateRequest) {
		return compService.update(compId, updateRequest);
	}

	@DeleteMapping("/{compId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteCompilation(@PathVariable long compId) {
		compService.delete(compId);
	}
}
