package com.example.lab.repository.dbrepo;

import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.domain.validate.Validator;
import com.example.lab.domain.validate.FriendshipValidator;
import com.example.lab.repository.Repo;

import java.net.Inet4Address;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendDbRepo implements Repo<Tuple<Integer,Integer>, Friendship> {
        private String url;
        private String username;
        private String password;
        private Validator<Friendship> validator;

    public FriendDbRepo(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
        @Override
        public Optional<Friendship> findOne(Tuple<Integer,Integer> aLong) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships WHERE id1 = ? AND id2 = ?")){
            statement.setInt(1, aLong.getLeft());
            statement.setInt(2, aLong.getRight());
            ResultSet result = statement.executeQuery();
            result.next();
            Integer idd = result.getInt("id1");
            Integer idd2 = result.getInt("id2");
            String datee = String.valueOf(result.getDate("date"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(datee, formatter);

            boolean status = result.getBoolean("pending");
            Friendship f = new Friendship(aLong, dateTime, status);
            return  Optional.of(f);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

        @Override
        public Iterable<Friendship> findAll() {
        Set<Friendship> friends = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id1 = resultSet.getInt("id1");
                int id2 = resultSet.getInt("id2");
                String date = String.valueOf(resultSet.getDate("date"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateTime = LocalDate.parse(date, formatter);

                boolean pending = resultSet.getBoolean("pending");
                Friendship f = new Friendship(new Tuple(id1,id2), dateTime, pending);

                friends.add(f);
            }
            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String sql = "insert into friendships (id1, id2, date, pending) values (?, ?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, entity.getId().getLeft());
            ps.setInt(2, entity.getId().getRight());

            ps.setString(3, String.valueOf(entity.getDate()));
            ps.setBoolean(4, entity.getPending());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Tuple<Integer, Integer> aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships WHERE id1 = ? AND id2 = ?")){
            statement.setInt(1, aLong.getLeft());
            statement.setInt(2, aLong.getRight());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("UPDATE friendships SET pending = ? WHERE id1 = ? AND id2 = ?")){
            ps.setBoolean(1, entity.getPending());
            ps.setInt(2, entity.getId().getLeft());
            ps.setInt(3, entity.getId().getRight());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    }

