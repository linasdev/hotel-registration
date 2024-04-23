package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Room;
import com.linasdev.hotel.registration.entity.RoomRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class BookARoomMenu extends Menu {
    private final RoomRepository roomRepository;

    public BookARoomMenu(Scanner userInput, PrintStream consoleOutput, RoomRepository roomRepository) {
        super(userInput, consoleOutput);
        this.roomRepository = roomRepository;
    }

    @Override
    public void enter() throws SQLException {
        List<Room> unnocupiedRooms = roomRepository.selectUnoccupiedRooms();
        List<String> roomNames = unnocupiedRooms
                .stream()
                .map(Room::getName)
                .toList();

        if (unnocupiedRooms.isEmpty()) {
            printFailureMessage();
            return;
        }

        printGreeting(roomNames);
        Integer roomNumber = collectUserInput(unnocupiedRooms.size());

        if (roomNumber == null) {
            return;
        }

        Room room = unnocupiedRooms.get(roomNumber - 1);

        printFirstNameMessage();
        String firstName = collectUserInput();

        if (firstName == null) {
            return;
        }

        printLastNameMessage();
        String lastName = collectUserInput();

        if (lastName == null) {
            return;
        }

        roomRepository.bookARoom(room.getId(), firstName, lastName, LocalDateTime.now());
        printSuccessMessage(room.getName());
    }

    private void printGreeting(List<String> roomNames) {
        printLine(String.format(
                "Currently these rooms are unnocupied: %s. Which room would you like to book?",
                String.join(", ", roomNames)
        ));

        for (int i = 0; i < roomNames.size(); i++) {
            printLine(String.format("\t- Enter %s to book room '%s'.", i + 1, roomNames.get(i)));
        }
    }

    private void printSuccessMessage(String roomName) {
        printLine(String.format(
                "You successfully booked room '%s'. Please do not forget to check out when leaving!",
                roomName
        ));
        printLine();
    }

    private void printFailureMessage() {
        printLine("Currently no rooms are available. Please come back later.");
        printLine();
    }

    private void printFirstNameMessage() {
        printLine("Enter your first name:");
    }

    private void printLastNameMessage() {
        printLine("Enter your last name:");
    }
}
