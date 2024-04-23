package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.CustomerRepository;
import com.linasdev.hotel.registration.entity.RoomRepository;

import java.io.PrintStream;
import java.util.Scanner;

public class MainMenu extends Menu {
    private final BookARoomMenu bookARoomMenu;
    private final CheckOutMenu checkOutMenu;
    private final HotelOccupancyMenu hotelOccupancyMenu;
    private final HotelOccupancyHistoryMenu hotelOccupancyHistoryMenu;

    public MainMenu(Scanner userInput, PrintStream consoleOutput, RoomRepository roomRepository, CustomerRepository customerRepository) {
        super(userInput, consoleOutput);
        this.bookARoomMenu = new BookARoomMenu(userInput, consoleOutput, roomRepository);
        this.checkOutMenu = new CheckOutMenu(userInput, consoleOutput, roomRepository);
        this.hotelOccupancyMenu = new HotelOccupancyMenu(userInput, consoleOutput, customerRepository);
        this.hotelOccupancyHistoryMenu = new HotelOccupancyHistoryMenu(userInput, consoleOutput, roomRepository, customerRepository);
    }

    public MainMenu(
            Scanner userInput,
            PrintStream consoleOutput,
            BookARoomMenu bookARoomMenu,
            CheckOutMenu checkOutMenu,
            HotelOccupancyMenu hotelOccupancyMenu,
            HotelOccupancyHistoryMenu hotelOccupancyHistoryMenu
    ) {
        super(userInput, consoleOutput);
        this.bookARoomMenu = bookARoomMenu;
        this.checkOutMenu = checkOutMenu;
        this.hotelOccupancyMenu = hotelOccupancyMenu;
        this.hotelOccupancyHistoryMenu = hotelOccupancyHistoryMenu;
    }

    @Override
    public void enter() throws Exception {
        printGreeting();

        Integer userSelection = collectUserInput(MainMenuOption.values().length);

        if (userSelection == null) {
            return;
        }

        switch (MainMenuOption.values()[userSelection - 1]) {
            case BOOK_A_ROOM -> bookARoomMenu.enter();
            case CHECK_OUT -> checkOutMenu.enter();
            case HOTEL_OCCUPANCY -> hotelOccupancyMenu.enter();
            case HOTEL_OCCUPANCY_HISTORY -> hotelOccupancyHistoryMenu.enter();
            default -> printLine("This options is currently unimplemented, we're sorry for the inconvenience.");
        }
    }

    private void printGreeting() {
        printLine("What would you like to do?");

        for (MainMenuOption mainMenuOption : MainMenuOption.values()) {
            printLine(mainMenuOption.getName());
        }
    }
}
