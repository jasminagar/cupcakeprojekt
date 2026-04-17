package services;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private Database database;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        database = mock(Database.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        userService = new UserService(database);
    }

    @Test
    void login_returnsUser_whenEmailUsernameAndPasswordAreCorrect() throws Exception {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("email")).thenReturn("test@test.dk");
        when(resultSet.getString("name")).thenReturn("testuser");
        when(resultSet.getString("password")).thenReturn("1234");
        when(resultSet.getDouble("balance")).thenReturn(500.0);
        when(resultSet.getString("role")).thenReturn("user");

        User result = userService.login("test@test.dk", "testuser", "1234");

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(preparedStatement).setString(1, "test@test.dk");
        verify(preparedStatement).setString(2, "testuser");
        verify(preparedStatement).setString(3, "1234");
        verify(preparedStatement).executeQuery();
    }

    @Test
    void login_returnsNull_whenUserDoesNotExist() throws Exception {
        when(resultSet.next()).thenReturn(false);

        User result = userService.login("forkert@test.dk", "forkert", "abcd");

        assertNull(result);
        verify(preparedStatement).setString(1, "forkert@test.dk");
        verify(preparedStatement).setString(2, "forkert");
        verify(preparedStatement).setString(3, "abcd");
        verify(preparedStatement).executeQuery();
    }
}