package com.linasdev.hotel.registration.ui;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.Scanner;

@RequiredArgsConstructor
public abstract class Menu {
    private final Scanner userInput;
    private final PrintStream consoleOutput;

    public abstract void enter() throws Exception;

    protected Integer collectUserInput(int totalOptions) {
        String userSelection = collectUserInput();

        try {
            int userSelectionInteger = Integer.parseInt(userSelection);

            if (userSelectionInteger < 1 || userSelectionInteger > totalOptions) {
                return null;
            }

            return userSelectionInteger;
        } catch (NumberFormatException ignored) {}

        return null;
    }

    protected String collectUserInput() {
        return userInput.nextLine();
    }

    protected void printLine() {
        consoleOutput.println();
    }

    protected void printLine(String message) {
        consoleOutput.println(message);
    }
}
