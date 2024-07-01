package ru.practicum.ewm.service.api;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
	List<CategoryDto> getAll(int from, int size);

	CategoryDto save(NewCategoryDto category);

	CategoryDto getCategory(long catId);

	void delete(long catId);

	CategoryDto update(CategoryDto category);
}
