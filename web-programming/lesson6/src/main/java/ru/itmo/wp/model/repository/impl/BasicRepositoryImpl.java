package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.EntityWithIdAndCreationTime;
import ru.itmo.wp.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

abstract class BasicRepositoryImpl {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    protected EntityWithIdAndCreationTime find(long id) {
        return findBy("WHERE id=?", id);
    }

    protected void save(EntityWithIdAndCreationTime entity, String sqlFields, String sqlValues, Object... params) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    String.join("", "INSERT INTO `", tableName(), "` ", sqlFields, " VALUES ", sqlValues),
                    Statement.RETURN_GENERATED_KEYS)) {
                setParams(params, statement);
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException(String.join(" ", "Can't save", tableName()));
                } else {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                        entity.setCreationTime(find(entity.getId()).getCreationTime());
                    } else {
                        throw new RepositoryException(
                                String.join(" ", "Can't save", tableName(), "[no autogenerated fields]."));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(String.join(" ", "Can't save", tableName()), e);
        }
    }

    private void setParams(Object[] params, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Long) {
                statement.setLong(i + 1, (Long) params[i]);
            } else if (params[i] instanceof String) {
                statement.setString(i + 1, (String) params[i]);
            } else {
                throw new RepositoryException(
                        String.join(" ", "Unknown parameter:", params[i].toString())
                );
            }
        }
    }

    protected EntityWithIdAndCreationTime findBy(String sqlRequest, Object... params) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    String.join(" ", "SELECT * FROM", tableName(), sqlRequest))) {
                setParams(params, statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return toInstance(statement.getMetaData(), resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(String.join(" ", "Can't find", tableName()), e);
        }
    }

    protected List<EntityWithIdAndCreationTime> findAllBy(String sqlRequest, Object... params) {
        List<EntityWithIdAndCreationTime> entities = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    String.join(" ", "SELECT * FROM", tableName(), sqlRequest))) {
                setParams(params, statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    EntityWithIdAndCreationTime entity;
                    while ((entity = toInstance(statement.getMetaData(), resultSet)) != null) {
                        entities.add(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(String.join(" ", "Can't find", tableName()), e);
        }
        return entities;
    }

    protected int findCount() {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    String.join(" ", "SELECT COUNT(*) AS result FROM", tableName()))) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.first();
                    return resultSet.getInt("result");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find count", e);
        }
    }


    protected abstract EntityWithIdAndCreationTime toInstance(ResultSetMetaData metaData, ResultSet resultSet)
            throws SQLException;

    protected abstract String tableName();
}
