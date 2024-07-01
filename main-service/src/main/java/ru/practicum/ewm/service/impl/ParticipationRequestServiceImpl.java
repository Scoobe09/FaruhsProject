package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.api.ParticipationRequestService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
	private final ParticipationRequestRepository requestRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	@Override
	public List<ParticipationRequestDto> getUserRequests(long userId) {
		return requestRepository.findByRequesterId(userId)
				.stream()
				.map(Mapper::toParticipationRequestDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ParticipationRequestDto addParticipationRequest(long participantId, long eventId) {
		// если запрос уже существует, то не создаем новый
		Optional<ParticipationRequest> optRequest =
				requestRepository.findByRequesterIdAndId(participantId, eventId);

		if (optRequest.isPresent()) {
			//noinspection OptionalGetWithoutIsPresent
			return optRequest.map(Mapper::toParticipationRequestDto).get();
		}

		// находим указанное событие
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException("Event", eventId));

		// если запрос на участие отправил инициатор события, то возвращаем ошибку
		if (event.getInitiator().getId().equals(participantId)) {
			throw new IllegalStateException("The initiator of the event is already a participant.");
		}

		// если событие не опубликовано, то в нем нельзя участвовать
		if (!event.isPublished()) {
			throw new IllegalStateException("Event not published");
		}

		// получаем количество подтвержденных заявок на участие и сравниваем его с лимитом
		// если лимит достигнут возвращаем ошибку
		int participantCount = requestRepository.countRequestsWithStatus(eventId, RequestStatus.CONFIRMED);

		if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= participantCount) {
			throw new IllegalStateException("The participant limit has been reached");
		}

		// находим пользователя отправившего заявку и создаём новый запрос на участие
		User participant = userRepository.findById(participantId)
				.orElseThrow(() -> new NotFoundException("User", participantId));

		ParticipationRequest request =
				requestRepository.save(Mapper.toNewParticipationRequest(participant, event));

		return Mapper.toParticipationRequestDto(request);
	}

	@Override
	public ParticipationRequestDto cancelOwnRequest(long requesterId, long requestId) {
		ParticipationRequest request = requestRepository.findByRequesterIdAndId(requesterId, requestId)
				.orElseThrow(() -> new IllegalArgumentException("No request for participation was " +
						"found according to the specified conditions"));
		request.setStatus(RequestStatus.CANCELED);

		return Mapper.toParticipationRequestDto(request);
	}
}
