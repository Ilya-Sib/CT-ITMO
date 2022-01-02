package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.EntityWithIdAndCreationTime;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl extends BasicRepositoryImpl implements UserRepository {
    @Override
    public User find(long id) {
        return (User) super.find(id);
    }

    @Override
    public User findByLogin(String login) {
        return (User) super.findBy("WHERE login=?", login);
    }

    @Override
    public User findByLoginAndPasswordSha(String login, String passwordSha) {
        return (User) super.findBy("WHERE login=? AND passwordSha=?", login, passwordSha);
    }

    private List<User> toUserList(List<EntityWithIdAndCreationTime> list) {
        return list.stream().map(entity -> (User) entity).collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return toUserList(super.findAllBy("ORDER BY id DESC"));
    }

    @Override
    public void save(User user, String passwordSha) {
        String sqlFields = "(`login`, `passwordSha`, `creationTime`)";
        String sqlValues = "(?, ?, NOW())";
        super.save(user, sqlFields, sqlValues, user.getLogin(), passwordSha);
    }

    @Override
    public void updateAdmin(User user, boolean admin) {
        String sql = "SET admin=? WHERE id=?";
        super.update(sql, admin, user.getId());
    }

    @Override
    protected EntityWithIdAndCreationTime toInstance(ResultSetMetaData metaData, ResultSet resultSet)
            throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        User user = new User();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    user.setId(resultSet.getLong(i));
                    break;
                case "login":
                    user.setLogin(resultSet.getString(i));
                    break;
                case "admin":
                    user.setAdmin(resultSet.getBoolean(i));
                    break;
                case "creationTime":
                    user.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return user;
    }

    @Override
    protected String tableName() {
        return "UserHW7";
    }
}
