package ru.practicum.ewm.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

	public static Event toEvent(NewEventDto dto, User initiator, Category category) {
		return Event.builder()
				.title(dto.getTitle())
				.annotation(dto.getAnnotation())
				.description(dto.getDescription())
				.category(category)
				.participantLimit(dto.getParticipantLimit())
				.paid(dto.isPaid())
				.eventDate(dto.getEventDate())
				.initiator(initiator)
				.latitude(dto.getLocation().getLat())
				.longitude(dto.getLocation().getLon())
				.requestModeration(dto.isRequestModeration())
				.build();
	}

	public static EventFullDto toEventFullDto(Event event) {
		return toEventFullDto(event, null, null);
	}

	public static EventFullDto toEventFullDto(Event event, Long views, Long confirmedRequests) {
		return EventFullDto.builder()
				.id(event.getId())
				.title(event.getTitle())
				.annotation(event.getAnnotation())
				.description(event.getDescription())
				.category(toCategoryDto(event.getCategory()))
				.participantLimit(event.getParticipantLimit())
				.state(event.getState().name())
				.paid(event.isPaid())
				.eventDate(event.getEventDate())
				.createdOn(event.getCreatedOn())
				.publishedOn(event.getPublishedOn())
				.initiator(Mapper.toUserShortDto(event.getInitiator()))
				.location(Location.of(event.getLatitude(), event.getLongitude()))
				.requestModeration(event.isRequestModeration())
				.rejectionReason(event.getRejectionReason())
				.views(views)
				.confirmedRequests(confirmedRequests)
				.build();
	}

	public static EventShortDto toEventShortDto(Event event) {
		return toEventShortDto(event, null, null);
	}

	public static EventRejectedDto toEventRejectedDto(Event event) {
		return EventRejectedDto.builder()
				.id(event.getId())
				.title(event.getTitle())
				.annotation(event.getAnnotation())
				.category(toCategoryDto(event.getCategory()))
				.paid(event.isPaid())
				.eventDate(event.getEventDate())
				.initiator(Mapper.toUserShortDto(event.getInitiator()))
				.rejectionReason(event.getRejectionReason())
				.build();
	}

	public static EventShortDto toEventShortDto(Event event, Long viewsCount, Long confirmedReqsCount) {
		return EventShortDto.builder()
				.id(event.getId())
				.title(event.getTitle())
				.annotation(event.getAnnotation())
				.category(toCategoryDto(event.getCategory()))
				.paid(event.isPaid())
				.eventDate(event.getEventDate())
				.initiator(Mapper.toUserShortDto(event.getInitiator()))
				.views(viewsCount)
				.confirmedRequests(confirmedReqsCount)
				.build();
	}

	public static UserShortDto toUserShortDto(User user) {
		return UserShortDto.of(user.getId(), user.getName());
	}

	public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
		return ParticipationRequestDto.of(
				request.getId(),
				request.getRequester().getId(),
				request.getEvent().getId(),
				request.getStatus().name(),
				request.getCreated()
		);
	}

	public static ParticipationRequest toNewParticipationRequest(User participant, Event event) {
		ParticipationRequest request = new ParticipationRequest();
		request.setRequester(participant);
		request.setEvent(event);
		// если для события отключена пре-модерация заявок на участие,
		// то запрос на участие считается подтвержденным автоматически
		if (!event.isRequestModeration()) {
			request.setStatus(RequestStatus.CONFIRMED);
		}
		return request;
	}

	public static CompilationDto toCompilationDto(Compilation compilation) {
		return CompilationDto.builder()
				.id(compilation.getId())
				.title(compilation.getTitle())
				.pinned(compilation.isPinned())
				.events(
						compilation
								.getEvents()
								.stream()
								.map(Mapper::toEventShortDto)
								.collect(Collectors.toSet())
				)
				.build();
	}

	public static UserDto toUserDto(User user) {
		return UserDto.of(user.getId(), user.getName(), user.getEmail());
	}

	public static User toNewUser(NewUserRequest dto) {
		return User.of(null, dto.getName(), dto.getEmail());
	}

	public static Compilation toNewCompilation(NewCompilationDto dto, Collection<Event> events) {
		Compilation compilation = new Compilation();
		compilation.setTitle(dto.getTitle());
		compilation.setPinned(dto.isPinned());
		compilation.setEvents(new HashSet<>(events));
		return compilation;
	}

	public static CategoryDto toCategoryDto(Category category) {
		return CategoryDto.of(category.getId(), category.getName());
	}

    public static Category toNewCategory(NewCategoryDto categoryDto) {
		Category category = new Category();
		category.setName(categoryDto.getName());
		return category;
    }
}