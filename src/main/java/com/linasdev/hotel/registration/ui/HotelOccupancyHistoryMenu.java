package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Customer;
import com.linasdev.hotel.registration.entity.CustomerRepository;
import com.linasdev.hotel.registration.entity.Room;
import com.linasdev.hotel.registration.entity.RoomRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class HotelOccupancyHistoryMenu extends Menu {
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    public HotelOccupancyHistoryMenu(Scanner userInput, PrintStream consoleOutput, RoomRepository roomRepository, CustomerRepository customerRepository) {
        super(userInput, consoleOutput);
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void enter() throws SQLException {
        List<Room> rooms = roomRepository.selectAll();
        List<String> roomNames = rooms
                .stream()
                .map(Room::getName)
                .toList();

        printGreeting(roomNames);

        Integer roomNumber = collectUserInput(rooms.size() + 1);

        if (roomNumber == null) {
            return;
        }

        Room selectedRoom = null;
        List<Customer> customerHistory = null;

        if (roomNumber.equals(rooms.size() + 1)) {
            customerHistory = customerRepository.selectAllCustomerHistory();
        } else {
            selectedRoom = rooms.get(roomNumber - 1);
            customerHistory = customerRepository.selectCustomerHistory(selectedRoom.getId());
        }

        if (customerHistory.isEmpty()) {
            printFailureMessage();
            return;
        }

        if (selectedRoom != null) {
            printSuccessMessage(customerHistory, selectedRoom.getOccupied());
        } else {
            printSuccessMessage(customerHistory, rooms.size());
        }
    }

    private void printGreeting(List<String> roomNames) {
        printLine(String.format(
                "Currently these rooms are available: %s. Which room's history did you want?",
                String.join(", ", roomNames)
        ));

        for (int i = 0; i < roomNames.size(); i++) {
            printLine(String.format("\t- Enter %s to view history of room '%s'.", i + 1, roomNames.get(i)));
        }

        printLine(String.format("\t- Enter %s to view history of all rooms.", roomNames.size() + 1));
    }

    private void printSuccessMessage(List<Customer> customerHistory, boolean currentlyOccupied) {
        String heading = "";

        if (currentlyOccupied) {
            heading += "This room is currently occupied.";
            heading += System.lineSeparator();
        } else {
            heading += "This room is currently not occupied.";
            heading += System.lineSeparator();
        }

        heading += "History for this room:";

        printSuccessMessage(customerHistory, heading);
    }

    private void printSuccessMessage(List<Customer> customerHistory, int roomCount) {
        String heading = "";

        heading += String.format("This hotel has a total of %s rooms.", roomCount);
        heading += System.lineSeparator();

        heading += "History for all of these rooms:";

        printSuccessMessage(customerHistory, heading);
    }

    private void printSuccessMessage(List<Customer> customerHistory, String heading) {
        printLine(heading);

        for (Customer customer : customerHistory) {
            printLine(String.format(
                    "\t- Room '%s' was booked by '%s %s' from %s to %s.",
                    customer.getRoomName(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getBookingTime(),
                    customer.getCheckOutTime() == null ? "now" : customer.getCheckOutTime()
            ));
        }

        printLine();
    }

    private void printFailureMessage() {
        printLine("This room has never been booked.");
        printLine();
    }
}
