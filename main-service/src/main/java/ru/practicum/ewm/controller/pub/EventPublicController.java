package ru.practicum.ewm.controller.pub;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.EventBase;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.GetEventsRequest;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.api.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventPublicController {
	private final EventService eventService;
	private final StatsClient statsClient;

	@GetMapping
	public List<? extends EventBase> getEvents(@RequestParam(required = false) String text,
											   @RequestParam(required = false) List<Long> categories,
											   @RequestParam(required = false) Boolean paid,
											   @RequestParam(defaultValue = "0") long lat,
											   @RequestParam(defaultValue = "0") long lon,
											   @RequestParam(defaultValue = "0") short radius,
											   @RequestParam(required = false) String rangeStart,
											   @RequestParam(required = false) String rangeEnd,
											   @RequestParam(defaultValue = "false") boolean onlyAvailable,
											   @RequestParam(defaultValue = "EVENT_DATE") String sort,
											   @RequestParam(defaultValue = "0") int from,
											   @RequestParam(defaultValue = "10") int size,
											   HttpServletRequest httpRequest) {

		List<? extends EventBase> result = eventService.find(
				GetEventsRequest.builder()
						.state(EventState.PUBLISHED)
						.text(text)
						.categories(categories)
						.paid(paid)
						.location(GetEventsRequest.Location.of(lat, lon, radius))
						.dateRange(rangeStart, rangeEnd)
						.onlyAvailableForParticipation(onlyAvailable)
						.page(GetEventsRequest.Page.of(from, size))
						.shortFormat(true)
						.sort(sort)
						.build()
		);

		statsClient.hit(httpRequest);

		return result;
	}



	@GetMapping("/{id}")
	public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
		EventFullDto result = eventService.findPublishedById(id);
		statsClient.hit(request);
		return result;
	}
}