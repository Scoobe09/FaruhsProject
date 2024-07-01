package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.service.api.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryPublicController {
	private final CategoryService categoryService;

	@GetMapping
	public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
			@RequestParam(defaultValue = "10") int size) {
		return categoryService.getAll(from, size);
	}

	@GetMapping("/{catId}")
	public CategoryDto getCategory(@PathVariable long catId) {
		return categoryService.getCategory(catId);
	}
}
