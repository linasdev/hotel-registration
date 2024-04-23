package com.linasdev.hotel.registration;

import com.linasdev.hotel.registration.entity.CustomerRepository;
import com.linasdev.hotel.registration.entity.Repository;
import com.linasdev.hotel.registration.entity.RoomRepository;
import com.linasdev.hotel.registration.ui.MainMenu;

import java.util.Scanner;

public class Main {
    private static final String DEFAULT_DATABASE_PATH = "database.sqlite";

    public static void main(String[] args) {
        Repository repository = null;
        int exitStatus = 0;

        try {
            repository = new Repository(args.length == 1 ? args[0] : DEFAULT_DATABASE_PATH);

            try (Scanner userInput = new Scanner(System.in)) {
                RoomRepository roomRepository = new RoomRepository(repository);
                CustomerRepository customerRepository = new CustomerRepository(repository);
                MainMenu mainMenu = new MainMenu(userInput, System.out, roomRepository, customerRepository);

                while (true) {
                    mainMenu.enter();
                }
            }
        } catch (Exception ex) {
            System.err.println("Exception occurred: " + ex.getMessage());
            ex.printStackTrace(System.err);
            exitStatus = -1;
        } finally {
            try {
                if (repository != null) {
                    repository.close();
                }
            } catch (Exception ex) {
                System.err.println("Exception occurred: " + ex.getMessage());
                ex.printStackTrace(System.err);
                exitStatus = -1;
            }

            System.exit(exitStatus);
        }
    }
}
