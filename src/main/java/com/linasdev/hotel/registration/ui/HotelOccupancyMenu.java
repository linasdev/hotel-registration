package com.linasdev.hotel.registration.ui;

import com.linasdev.hotel.registration.entity.Customer;
import com.linasdev.hotel.registration.entity.CustomerRepository;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class HotelOccupancyMenu extends Menu {
    private final CustomerRepository customerRepository;

    public HotelOccupancyMenu(Scanner userInput, PrintStream consoleOutput, CustomerRepository customerRepository) {
        super(userInput, consoleOutput);
        this.customerRepository = customerRepository;
    }

    @Override
    public void enter() throws SQLException {
        List<Customer> currentCustomers = customerRepository.selectCurrentCustomers();

        if (currentCustomers.isEmpty()) {
            printFailureMessage();
            return;
        }

        printGreeting(currentCustomers);
    }

    private void printGreeting(List<Customer> currentCustomers) {
        printLine("These are the currently booked rooms and their guest information:");
        for (Customer customer : currentCustomers) {
            printLine(String.format(
                    "\t- Room '%s' is occupied by '%s %s' since %s.",
                    customer.getRoomName(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getBookingTime()
            ));
        }
    }

    private void printFailureMessage() {
        printLine("Currently no rooms are booked.");
        printLine();
    }
}
