package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Customer;
import com.linasdev.hotel.registration.entity.CustomerRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HotelOccupancyMenuTest {
    @Mock
    private Scanner userInput;

    @Mock
    private PrintStream outputConsole;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private HotelOccupancyMenu target;

    @Test
    void shouldShowCurrentlyOccupiedRooms_whenSuchExist() throws Exception {
        LocalDateTime bookingTime = LocalDateTime.now();
        when(customerRepository.selectCurrentCustomers()).thenReturn(List.of(
                Customer.builder()
                        .id(1)
                        .firstName("First name")
                        .lastName("Last name")
                        .roomName("#1")
                        .bookingTime(bookingTime)
                        .build()
        ));

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(2)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "These are the currently booked rooms and their guest information:",
                String.format("\t- Room '#1' is occupied by 'First name Last name' since %s.", bookingTime)
        );

        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void shouldPrintFailureMessage_whenNoRoomsAreBooked() throws Exception {
        when(customerRepository.selectCurrentCustomers()).thenReturn(List.of());

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getValue()).isEqualTo("Currently no rooms are booked.");

        verifyNoMoreInteractions(customerRepository);
    }
}
