package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EventBase;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.GetEventsRequest;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.api.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventAdminController {
	private final EventService eventService;

	@GetMapping
	public List<? extends EventBase> getEvents(@RequestParam(required = false) List<Long> users,
											   @RequestParam(required = false) List<String> states,
											   @RequestParam(required = false) List<Long> categories,
											   @RequestParam(required = false) String rangeStart,
											   @RequestParam(required = false) String rangeEnd,
											   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
											   @RequestParam(defaultValue = "10") @Positive int size) {
		return eventService.find(
				GetEventsRequest.builder()
						.categories(categories)
						.initiators(users)
						.states(states)
						.dateRange(rangeStart, rangeEnd)
						.page(GetEventsRequest.Page.of(from, size))
						.shortFormat(false)
						.build()
		);
	}

	@GetMapping("/pending")
	public List<? extends EventBase> getPendingEvents(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
													  @RequestParam(defaultValue = "10") @Positive int size) {
		return eventService.find(
				GetEventsRequest.builder()
						.states(List.of(EventState.PENDING.name()))
						.page(GetEventsRequest.Page.of(from, size))
						.shortFormat(false)
						.build()
		);
	}

	@PatchMapping("/{eventId}")
	public EventFullDto updateEvent(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest updateInfo) {
		return eventService.updateEventByAdmin(eventId, updateInfo);
	}
}
