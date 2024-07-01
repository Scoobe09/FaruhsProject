package ru.practicum.ewm.controller.priv;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.api.ParticipationRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class RequestPrivateController {
	private final ParticipationRequestService requestService;

	@GetMapping
	public List<ParticipationRequestDto> getUserRequests(@PathVariable long userId) {
		return requestService.getUserRequests(userId);
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ParticipationRequestDto addParticipationRequest(@PathVariable long userId, @RequestParam long eventId) {
		return requestService.addParticipationRequest(userId, eventId);
	}

	@PatchMapping("/{requestId}/cancel")
	public ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
		return requestService.cancelOwnRequest(userId, requestId);
	}
}