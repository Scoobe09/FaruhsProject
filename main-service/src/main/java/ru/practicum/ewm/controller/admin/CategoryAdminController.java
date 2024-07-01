package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.api.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryAdminController {
	private final CategoryService categoryService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto category) {
		return categoryService.save(category);
	}

	@PatchMapping("/{catId}")
	public CategoryDto updateCategory(@PathVariable long catId, @Valid @RequestBody CategoryDto category) {
		category.setId(catId);
		return categoryService.update(category);
	}

	@DeleteMapping("/{catId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable long catId) {
		categoryService.delete(catId);
	}
}
