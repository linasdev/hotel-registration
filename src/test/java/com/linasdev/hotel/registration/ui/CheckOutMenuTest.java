package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Room;
import com.linasdev.hotel.registration.entity.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckOutMenuTest {
    @Mock
    private Scanner userInput;

    @Mock
    private PrintStream outputConsole;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private CheckOutMenu target;

    @Test
    void shouldCheckOut_whenCorrectInformationIsSupplied() throws Exception {
        when(roomRepository.selectOccupiedRooms()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(true)
                        .build()
        ));

        when(userInput.nextLine())
                .thenReturn("1");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(3)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are occupied: #1. Which room did you book?",
                "\t- Enter 1 to check out of room '#1'.",
                "You successfully checked out of room '#1'!"
        );

        verify(roomRepository).checkOutARoom(eq(1), any(LocalDateTime.class));
    }

    @Test
    void shouldPrintFailureMessage_whenNoRoomsAreBooked() throws Exception {
        when(roomRepository.selectOccupiedRooms()).thenReturn(List.of());

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getValue()).isEqualTo("Currently no rooms are occupied. Please book a room first.");

        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void shouldDoNothing_whenIncorrectInformationIsSupplied() throws Exception {
        when(roomRepository.selectOccupiedRooms()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(true)
                        .build()
        ));

        when(userInput.nextLine())
                .thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(2)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are occupied: #1. Which room did you book?",
                "\t- Enter 1 to check out of room '#1'."
        );

        verifyNoMoreInteractions(roomRepository);
    }
}
