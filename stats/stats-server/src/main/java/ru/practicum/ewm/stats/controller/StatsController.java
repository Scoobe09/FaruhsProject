package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;
import ru.practicum.ewm.stats.service.api.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatsController {
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final StatsService service;

	@PostMapping(path = "/hit")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void hit(@RequestBody EndpointHit hit) {
		service.recordHit(hit);
	}

	@GetMapping("/stats")
	public ResponseEntity<List<ViewStats>> getStats(@RequestParam @NonNull String start,
													@RequestParam @NonNull String end,
													@RequestParam(required = false) List<String> uris,
													@RequestParam(required = false) Integer limit,
													@RequestParam(defaultValue = "false") boolean unique) {
		LocalDateTime startDT;
		LocalDateTime endDT;
		try {
			startDT = LocalDateTime.parse(start, DTF);
			endDT = LocalDateTime.parse(end, DTF);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().build();
		}
		if (uris == null) {
			uris = new ArrayList<>();
		}
		List<ViewStats> results = service.calculateViews(
				ViewsStatsRequest.builder()
						.start(startDT)
						.end(endDT)
						.uris(uris)
						.limit(limit)
						.unique(unique)
						.build()
		);
		return ResponseEntity.ok(results);
	}
}
