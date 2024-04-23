package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Customer;
import com.linasdev.hotel.registration.entity.CustomerRepository;
import com.linasdev.hotel.registration.entity.Room;
import com.linasdev.hotel.registration.entity.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelOccupancyHistoryMenuTest {
    @Mock
    private Scanner userInput;

    @Mock
    private PrintStream outputConsole;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private HotelOccupancyHistoryMenu target;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldShowHotelHistory_whenCorrectInformationIsSupplied(Boolean occupied) throws Exception {
        LocalDateTime bookingTime = LocalDateTime.now().minusDays(1);
        LocalDateTime checkOutTime = LocalDateTime.now();

        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(occupied)
                        .build()
        ));
        when(customerRepository.selectCustomerHistory(1)).thenReturn(List.of(
                Customer.builder()
                        .id(1)
                        .firstName("First name")
                        .lastName("Last name")
                        .roomName("#1")
                        .bookingTime(bookingTime)
                        .checkOutTime(checkOutTime)
                        .build()
        ));
        when(userInput.nextLine()).thenReturn("1");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #1. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#1'.",
                "\t- Enter 2 to view history of all rooms.",
                occupied
                        ? String.format("This room is currently occupied.%nHistory for this room:")
                        : String.format("This room is currently not occupied.%nHistory for this room:"),
                String.format("\t- Room '#1' was booked by 'First name Last name' from %s to %s.", bookingTime, checkOutTime)
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }

    @Test
    void shouldShowHotelHistory_whenCorrectInformationIsSuppliedAndAllHistoryIsRequested() throws Exception {
        LocalDateTime bookingTime = LocalDateTime.now().minusDays(1);
        LocalDateTime checkOutTime = LocalDateTime.now();

        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(false)
                        .build()
        ));
        when(customerRepository.selectAllCustomerHistory()).thenReturn(List.of(
                Customer.builder()
                        .id(1)
                        .firstName("First name")
                        .lastName("Last name")
                        .roomName("#1")
                        .bookingTime(bookingTime)
                        .checkOutTime(checkOutTime)
                        .build()
        ));
        when(userInput.nextLine()).thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #1. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#1'.",
                "\t- Enter 2 to view history of all rooms.",
                String.format("This hotel has a total of 1 rooms.%nHistory for all of these rooms:"),
                String.format("\t- Room '#1' was booked by 'First name Last name' from %s to %s.", bookingTime, checkOutTime)
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldShowHotelHistory_whenCorrectInformationIsSuppliedAndCustomerIsStillCheckedIn(Boolean occupied) throws Exception {
        LocalDateTime bookingTime = LocalDateTime.now().minusDays(1);

        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(occupied)
                        .build()
        ));
        when(customerRepository.selectCustomerHistory(1)).thenReturn(List.of(
                Customer.builder()
                        .id(1)
                        .firstName("First name")
                        .lastName("Last name")
                        .roomName("#1")
                        .bookingTime(bookingTime)
                        .build()
        ));
        when(userInput.nextLine()).thenReturn("1");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #1. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#1'.",
                "\t- Enter 2 to view history of all rooms.",
                occupied
                        ? String.format("This room is currently occupied.%nHistory for this room:")
                        : String.format("This room is currently not occupied.%nHistory for this room:"),
                String.format("\t- Room '#1' was booked by 'First name Last name' from %s to now.", bookingTime)
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }

    @Test
    void shouldShowHotelHistory_whenCorrectInformationIsSuppliedAndAllHistoryIsRequestedAndCustomerIsStillCheckedIn() throws Exception {
        LocalDateTime bookingTime = LocalDateTime.now().minusDays(1);

        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(false)
                        .build()
        ));
        when(customerRepository.selectAllCustomerHistory()).thenReturn(List.of(
                Customer.builder()
                        .id(1)
                        .firstName("First name")
                        .lastName("Last name")
                        .roomName("#1")
                        .bookingTime(bookingTime)
                        .build()
        ));
        when(userInput.nextLine()).thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #1. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#1'.",
                "\t- Enter 2 to view history of all rooms.",
                String.format("This hotel has a total of 1 rooms.%nHistory for all of these rooms:"),
                String.format("\t- Room '#1' was booked by 'First name Last name' from %s to now.", bookingTime)
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }

    @Test
    void shouldPrintFailureMessage_whenNoRoomsHistoryExistsForTheSelectedRoom() throws Exception {
        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(2)
                        .name("#2")
                        .occupied(false)
                        .build()
        ));
        when(customerRepository.selectCustomerHistory(2)).thenReturn(List.of());
        when(userInput.nextLine()).thenReturn("1");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(4)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #2. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#2'.",
                "\t- Enter 2 to view history of all rooms.",
                "This room has never been booked."
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }

    @Test
    void shouldPrintFailureMessage_whenNoRoomsWereEverBooked() throws Exception {
        when(roomRepository.selectAll()).thenReturn(List.of(
                Room.builder()
                        .id(1)
                        .name("#1")
                        .occupied(false)
                        .build()
        ));
        when(customerRepository.selectAllCustomerHistory()).thenReturn(List.of());
        when(userInput.nextLine()).thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(4)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "Currently these rooms are available: #1. Which room's history did you want?",
                "\t- Enter 1 to view history of room '#1'.",
                "\t- Enter 2 to view history of all rooms.",
                "This room has never been booked."
        );

        verifyNoMoreInteractions(roomRepository, customerRepository);
    }
}
