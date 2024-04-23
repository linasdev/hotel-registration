package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Room;
import com.linasdev.hotel.registration.entity.RoomRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CheckOutMenu extends Menu {
    private final RoomRepository roomRepository;

    public CheckOutMenu(Scanner userInput, PrintStream consoleOutput, RoomRepository roomRepository) {
        super(userInput, consoleOutput);
        this.roomRepository = roomRepository;
    }

    @Override
    public void enter() throws SQLException {
        List<Room> occupiedRooms = roomRepository.selectOccupiedRooms();
        List<String> roomNames = occupiedRooms
                .stream()
                .map(Room::getName)
                .toList();

        if (occupiedRooms.isEmpty()) {
            printFailureMessage();
            return;
        }

        printGreeting(roomNames);
        Integer roomNumber = collectUserInput(occupiedRooms.size());

        if (roomNumber == null) {
            return;
        }

        Room room = occupiedRooms.get(roomNumber - 1);
        roomRepository.checkOutARoom(room.getId(), LocalDateTime.now());
        printSuccessMessage(room.getName());
    }

    private void printGreeting(List<String> roomNames) {
        printLine(String.format(
                "Currently these rooms are occupied: %s. Which room did you book?",
                String.join(", ", roomNames)
        ));

        for (int i = 0; i < roomNames.size(); i++) {
            printLine(String.format("\t- Enter %s to check out of room '%s'.", i + 1, roomNames.get(i)));
        }
    }

    private void printSuccessMessage(String roomName) {
        printLine(String.format(
                "You successfully checked out of room '%s'!",
                roomName
        ));
        printLine();
    }

    private void printFailureMessage() {
        printLine("Currently no rooms are occupied. Please book a room first.");
        printLine();
    }
}
