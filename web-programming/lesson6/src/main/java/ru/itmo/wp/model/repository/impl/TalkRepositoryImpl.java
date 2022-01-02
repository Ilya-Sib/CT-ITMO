package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.EntityWithIdAndCreationTime;
import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.repository.TalkRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TalkRepositoryImpl extends BasicRepositoryImpl implements TalkRepository {
    @Override
    public Talk find(long id) {
        return (Talk) super.find(id);
    }

    @Override
    public void save(Talk talk) {
        String sqlFields = "(`sourceUserId`, `targetUserId`, `text`, `creationTime`)";
        String sqlValues = "(?, ?, ?, NOW())";
        super.save(talk, sqlFields, sqlValues, talk.getSourceUserId(), talk.getTargetUserId(), talk.getText());
    }

    @Override
    protected String tableName() {
        return "Talk";
    }

    @Override
    protected Talk toInstance(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Talk talk = new Talk();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    talk.setId(resultSet.getLong(i));
                    break;
                case "sourceUserId":
                    talk.setSourceUserId(resultSet.getLong(i));
                    break;
                case "targetUserId":
                    talk.setTargetUserId(resultSet.getLong(i));
                    break;
                case "text":
                    talk.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    talk.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return talk;
    }

    private List<Talk> toTalkList(List<EntityWithIdAndCreationTime> list) {
        return list.stream().map(entity -> (Talk) entity).collect(Collectors.toList());
    }

    @Override
    public List<Talk> findAll() {
        return toTalkList(super.findAllBy("ORDER BY creationTime DESC"));
    }

    @Override
    public List<Talk> findAllById(long id) {
        return toTalkList(super.findAllBy(
                "WHERE sourceUserId=? OR targetUserId=? ORDER BY creationTime DESC",
                id, id));
    }
}
