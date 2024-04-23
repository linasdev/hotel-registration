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
class BookARoomMenuTest {
    @Mock
    private Scanner userInput;

    @Mock
    private PrintStream outputConsole;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookARoomMenu target;

    @Test
    void shouldBookARoom_whenCorrectInformationIsSupplied() throws Exception {
        when(roomRepository.selectUnoccupiedRooms()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(false)
                        .build()
        ));

        when(userInput.nextLine())
                .thenReturn("1")
                .thenReturn("First name")
                .thenReturn("Last name");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are unnocupied: #1. Which room would you like to book?",
                "\t- Enter 1 to book room '#1'.",
                "Enter your first name:",
                "Enter your last name:",
                "You successfully booked room '#1'. Please do not forget to check out when leaving!"
        );

        verify(roomRepository).bookARoom(eq(1), eq("First name"), eq("Last name"), any(LocalDateTime.class));
    }

    @Test
    void shouldPrintFailureMessage_whenNoRoomsAreAvailable() throws Exception {
        when(roomRepository.selectUnoccupiedRooms()).thenReturn(List.of());

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getValue()).isEqualTo("Currently no rooms are available. Please come back later.");

        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void shouldDoNothing_whenIncorrectInformationIsSupplied() throws Exception {
        when(roomRepository.selectUnoccupiedRooms()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(false)
                        .build()
        ));

        when(userInput.nextLine()).thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(2)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are unnocupied: #1. Which room would you like to book?",
                "\t- Enter 1 to book room '#1'."
        );

        verifyNoMoreInteractions(roomRepository);
    }
}
