package ru.practicum.ewm.service.api;

import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
	List<ParticipationRequestDto> getUserRequests(long userId);

	ParticipationRequestDto addParticipationRequest(long userId, long eventId);

	ParticipationRequestDto cancelOwnRequest(long requesterId, long requestId);
}
