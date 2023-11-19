package org.example.dao;

import org.example.bookEntity.BookEntity;
import org.example.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {

    public static final BookDAO INSTANCE = new BookDAO();

    private static final String DELETE_SQL = """
            DELETE FROM book
            WHERE id = ?
            """;

    private static final String CREATE_SQL = """
            INSERT INTO book(title, year, countStr, author_id)
            VALUES (?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE book
            SET title = ?,
                year = ?,
                countStr = ?,
                author_id = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                title,
                year,
                countStr
            FROM book
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    public BookDAO() {
    }




    public List<BookEntity> findAll() {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<BookEntity> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildBook(resultSet));
            }
            return tickets;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public Optional<BookEntity> findById(int id) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            BookEntity book = null;
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }

            return Optional.ofNullable(book);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BookEntity save(BookEntity bookEntity) throws SQLException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, bookEntity.getTitle());
            preparedStatement.setString(2, bookEntity.getYear());
            preparedStatement.setInt(3, bookEntity.getCountSrt());
            preparedStatement.setInt(4, bookEntity.getAuthor_id());

            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {

                bookEntity.setId(generatedKeys.getObject("id", Integer.class));
            }
            return bookEntity;
        }
    }

    public static BookDAO getInstance() {
        return INSTANCE;
    }

    private BookEntity buildBook(ResultSet resultSet) throws SQLException {
        return new BookEntity(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("year"),
                resultSet.getInt("countStr"),
                resultSet.getInt("author_id")
        );
    }








}
