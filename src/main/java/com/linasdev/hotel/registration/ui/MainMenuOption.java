package com.linasdev.hotel.registration.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainMenuOption {
    BOOK_A_ROOM("\t- Enter 1 to book a room."),
    CHECK_OUT("\t- Enter 2 to check out."),
    HOTEL_OCCUPANCY("\t- Enter 3 to view hotel occupancy."),
    HOTEL_OCCUPANCY_HISTORY("\t- Enter 4 to view hotel occupancy history.");

    private final String name;
}
