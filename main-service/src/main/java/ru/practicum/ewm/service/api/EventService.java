package ru.practicum.ewm.service.api;

import ru.practicum.ewm.dto.*;

import java.util.List;

public interface EventService {
	List<? extends EventBase> find(GetEventsRequest request);

	EventFullDto findPublishedById(long id);

	EventFullDto addEvent(NewEventDto eventDto, long initiatorId);

	List<ParticipationRequestDto> findUserEventParticipationRequests(long initiatorId, long eventId);

	EventFullDto updateEventByInitiator(long initiatorId, long eventId, UpdateEventUserRequest request);

	List<EventShortDto> findUserEvents(long userId, int from, int size);

	List<EventShortDto> findUserRejectedEvents(long userId, int from, int size);

	EventFullDto findUserEventById(long userId, long eventId);

	EventFullDto updateEventByAdmin(long eventId, UpdateEventAdminRequest request);

	EventRequestStatusUpdateResult changeParticipationReqStatus(long userId,
																long eventId,
																EventRequestStatusUpdateRequest updateRequests);
}
