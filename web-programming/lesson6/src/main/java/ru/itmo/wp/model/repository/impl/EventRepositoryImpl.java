package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.EventType;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.EventRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class EventRepositoryImpl extends BasicRepositoryImpl implements EventRepository {
    @Override
    public Event find(long id) {
        return (Event) super.find(id);
    }

    @Override
    public void save(Event event) {
        String sqlFields = "(`userId`, `type`, `creationTime`)";
        String sqlValues = "(?, ?, NOW())";
        super.save(event, sqlFields, sqlValues, event.getUserId(), event.getType().name());
    }

    @Override
    protected String tableName() {
        return "Event";
    }

    @Override
    protected Event toInstance(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Event event = new Event();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    event.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    event.setUserId(resultSet.getLong(i));
                    break;
                case "creationTime":
                    event.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "type":
                    event.setType(toEventType(resultSet.getString(i)));
                    break;
                default:
                    // No operations.
            }
        }

        return event;
    }

    private EventType toEventType(String eventType) {
        switch (eventType) {
            case "ENTER":
                return EventType.ENTER;
            case "LOGOUT":
                return EventType.LOGOUT;
        }
        throw new RepositoryException("Unknown event type: " + eventType);
    }
}
