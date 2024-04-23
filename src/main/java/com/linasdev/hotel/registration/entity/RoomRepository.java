package com.linasdev.hotel.registration.entity;

import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RoomRepository {
    private final Repository repository;

    public List<Room> selectUnoccupiedRooms() throws SQLException {
        Statement statement = repository.createStatement();

        if (!statement.execute("SELECT * FROM room WHERE occupied = false")) {
            return null;
        }

        List<Room> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                results.add(
                        Room.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .occupied(resultSet.getBoolean("occupied"))
                                .build()
                );
            }
        }

        return results;
    }

    public List<Room> selectOccupiedRooms() throws SQLException {
        Statement statement = repository.createStatement();

        if (!statement.execute("SELECT * FROM room WHERE occupied = true")) {
            return null;
        }

        List<Room> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                results.add(
                        Room.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .occupied(resultSet.getBoolean("occupied"))
                                .build()
                );
            }
        }

        return results;
    }

    public List<Room> selectAll() throws SQLException {
        Statement statement = repository.createStatement();

        if (!statement.execute("SELECT * FROM room")) {
            return null;
        }

        List<Room> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                results.add(
                        Room.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .occupied(resultSet.getBoolean("occupied"))
                                .build()
                );
            }
        }

        return results;
    }

    public void bookARoom(Integer roomId, String firstName, String lastName, LocalDateTime bookingTime) throws SQLException {
        PreparedStatement occupyRoomStatement = repository.prepareStatement("UPDATE room SET occupied = ? WHERE id = ?");
        occupyRoomStatement.setBoolean(1, true);
        occupyRoomStatement.setInt(2, roomId);
        occupyRoomStatement.execute();

        PreparedStatement addCustomerHistoryStatement = repository.prepareStatement("INSERT INTO customer (room_id, first_name, last_name, booking_timestamp) VALUES (?, ?, ?, ?)");
        addCustomerHistoryStatement.setInt(1, roomId);
        addCustomerHistoryStatement.setString(2, firstName);
        addCustomerHistoryStatement.setString(3, lastName);
        addCustomerHistoryStatement.setTimestamp(4, Timestamp.valueOf(bookingTime));
        addCustomerHistoryStatement.execute();
    }

    public void checkOutARoom(Integer roomId, LocalDateTime checkOutTime) throws SQLException {
        PreparedStatement occupyRoomStatement = repository.prepareStatement("UPDATE room SET occupied = ? WHERE id = ?");
        occupyRoomStatement.setBoolean(1, false);
        occupyRoomStatement.setInt(2, roomId);
        occupyRoomStatement.execute();

        PreparedStatement updateCustomerHistoryStatement = repository.prepareStatement("""
                UPDATE customer SET check_out_timestamp = ? WHERE id = (
                    SELECT id FROM customer WHERE room_id = ?
                        ORDER BY booking_timestamp DESC
                        LIMIT 1
                )
                """);
        updateCustomerHistoryStatement.setTimestamp(1, Timestamp.valueOf(checkOutTime));
        updateCustomerHistoryStatement.setInt(2, roomId);
        updateCustomerHistoryStatement.execute();
    }
}
