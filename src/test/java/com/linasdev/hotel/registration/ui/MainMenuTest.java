package com.linasdev.hotel.registration.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainMenuTest {
    @Mock
    private Scanner userInput;

    @Mock
    private PrintStream outputConsole;

    @Mock
    private BookARoomMenu bookARoomMenu;

    @Mock
    private CheckOutMenu checkOutMenu;

    @Mock
    private HotelOccupancyMenu hotelOccupancyMenu;

    @Mock
    private HotelOccupancyHistoryMenu hotelOccupancyHistoryMenu;

    @InjectMocks
    private MainMenu target;

    @Test
    void shouldEnterBookARoomMenu() throws Exception {
        when(userInput.nextLine()).thenReturn("1");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "What would you like to do?",
                MainMenuOption.BOOK_A_ROOM.getName(),
                MainMenuOption.CHECK_OUT.getName(),
                MainMenuOption.HOTEL_OCCUPANCY.getName(),
                MainMenuOption.HOTEL_OCCUPANCY_HISTORY.getName()
        );

        verify(bookARoomMenu).enter();
    }

    @Test
    void shouldEnterCheckOutMenu() throws Exception {
        when(userInput.nextLine()).thenReturn("2");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "What would you like to do?",
                MainMenuOption.BOOK_A_ROOM.getName(),
                MainMenuOption.CHECK_OUT.getName(),
                MainMenuOption.HOTEL_OCCUPANCY.getName(),
                MainMenuOption.HOTEL_OCCUPANCY_HISTORY.getName()
        );

        verify(checkOutMenu).enter();
    }

    @Test
    void shouldEnterHotelOccupancyMenu() throws Exception {
        when(userInput.nextLine()).thenReturn("3");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "What would you like to do?",
                MainMenuOption.BOOK_A_ROOM.getName(),
                MainMenuOption.CHECK_OUT.getName(),
                MainMenuOption.HOTEL_OCCUPANCY.getName(),
                MainMenuOption.HOTEL_OCCUPANCY_HISTORY.getName()
        );

        verify(hotelOccupancyMenu).enter();
    }

    @Test
    void shouldEnterHotelOccupancyHistoryMenu() throws Exception {
        when(userInput.nextLine()).thenReturn("4");

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "What would you like to do?",
                MainMenuOption.BOOK_A_ROOM.getName(),
                MainMenuOption.CHECK_OUT.getName(),
                MainMenuOption.HOTEL_OCCUPANCY.getName(),
                MainMenuOption.HOTEL_OCCUPANCY_HISTORY.getName()
        );

        verify(hotelOccupancyHistoryMenu).enter();
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "5"})
    void shouldDoNothing_whenIncorrectInformationIsSupplied(String userSelection) throws Exception {
        when(userInput.nextLine()).thenReturn(userSelection);

        target.enter();

        ArgumentCaptor<String> consoleOutputCaptor = ArgumentCaptor.forClass(String.class);
        verify(outputConsole, times(5)).println(consoleOutputCaptor.capture());
        assertThat(consoleOutputCaptor.getAllValues()).containsExactly(
                "What would you like to do?",
                MainMenuOption.BOOK_A_ROOM.getName(),
                MainMenuOption.CHECK_OUT.getName(),
                MainMenuOption.HOTEL_OCCUPANCY.getName(),
                MainMenuOption.HOTEL_OCCUPANCY_HISTORY.getName()
        );

        verifyNoInteractions(bookARoomMenu, checkOutMenu, hotelOccupancyMenu, hotelOccupancyHistoryMenu);
    }
}
