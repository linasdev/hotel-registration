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
public class CustomerRepository {
    private final Repository repository;

    public List<Customer> selectCurrentCustomers() throws SQLException {
        Statement statement = repository.createStatement();

        if (!statement.execute("""                
                SELECT c.id, c.first_name, c.last_name, r.name AS room_name, c.booking_timestamp FROM customer c
                    LEFT JOIN room r ON r.id = c.room_id
                    WHERE c.check_out_timestamp IS NULL
                """)) {
            return null;
        }

        List<Customer> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                results.add(
                        Customer.builder()
                                .id(resultSet.getInt("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .roomName(resultSet.getString("room_name"))
                                .bookingTime(resultSet.getTimestamp("booking_timestamp").toLocalDateTime())
                                .build()
                );
            }
        }

        return results;
    }

    public List<Customer> selectAllCustomerHistory() throws SQLException {
        Statement statement = repository.createStatement();

        if (!statement.execute("""                
                SELECT c.id, c.first_name, c.last_name, r.name AS room_name, c.booking_timestamp, c.check_out_timestamp FROM customer c
                    LEFT JOIN room r ON r.id = c.room_id
                    ORDER BY c.booking_timestamp DESC
                """)) {
            return null;
        }

        List<Customer> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                Timestamp checkOutTimestamp = resultSet.getTimestamp("check_out_timestamp");

                results.add(
                        Customer.builder()
                                .id(resultSet.getInt("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .roomName(resultSet.getString("room_name"))
                                .bookingTime(resultSet.getTimestamp("booking_timestamp").toLocalDateTime())
                                .checkOutTime(checkOutTimestamp == null ? null : checkOutTimestamp.toLocalDateTime())
                                .build()
                );
            }
        }

        return results;
    }

    public List<Customer> selectCustomerHistory(Integer roomId) throws SQLException {
        PreparedStatement statement = repository.prepareStatement("""                
                SELECT c.id, c.first_name, c.last_name, r.name AS room_name, c.booking_timestamp, c.check_out_timestamp FROM customer c
                    LEFT JOIN room r ON r.id = c.room_id
                    WHERE c.room_id = ?
                    ORDER BY c.booking_timestamp DESC
                """);

        statement.setInt(1, roomId);

        if (!statement.execute()) {
            return null;
        }

        List<Customer> results = new ArrayList<>();

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                Timestamp checkOutTimestamp = resultSet.getTimestamp("check_out_timestamp");

                results.add(
                        Customer.builder()
                                .id(resultSet.getInt("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .roomName(resultSet.getString("room_name"))
                                .bookingTime(resultSet.getTimestamp("booking_timestamp").toLocalDateTime())
                                .checkOutTime(checkOutTimestamp == null ? null : checkOutTimestamp.toLocalDateTime())
                                .build()
                );
            }
        }

        return results;
    }
}
