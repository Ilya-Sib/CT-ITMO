package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.EntityWithIdAndCreationTime;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.ArticleRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ArticleRepositoryImpl extends BasicRepositoryImpl implements ArticleRepository {
    @Override
    public Article find(long id) {
        return (Article) super.find(id);
    }

    private List<Article> toArticleList(List<EntityWithIdAndCreationTime> list) {
        return list.stream().map(entity -> (Article) entity).collect(Collectors.toList());
    }

    @Override
    public List<Article> findAll() {
        return toArticleList(super.findAllBy("ORDER BY creationTime DESC"));
    }

    @Override
    public void save(Article article) {
        String sqlFields = "(`userId`, `title`, `text`, `hidden`, `creationTime`)";
        String sqlValues = "(?, ?, ?, ?, NOW())";
        super.save(article, sqlFields, sqlValues, article.getUserId(),
                article.getTitle(), article.getText(), article.isHidden());
    }

    @Override
    public List<Article> findAllNotHidden() {
        String sql = "WHERE hidden=? ORDER BY creationTime DESC";
        return toArticleList(super.findAllBy(sql, false));
    }

    @Override
    public List<Article> findAllByUser(User user) {
        String sql = "WHERE userId=? ORDER BY creationTime DESC";
        return toArticleList(super.findAllBy(sql, user.getId()));
    }

    @Override
    public void updateHidden(Article article, boolean hidden) {
        String sql = "SET hidden=? WHERE id=?";
        super.update(sql, hidden, article.getId());
    }

    @Override
    protected EntityWithIdAndCreationTime toInstance(ResultSetMetaData metaData, ResultSet resultSet)
            throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    article.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    article.setUserId(resultSet.getLong(i));
                    break;
                case "title":
                    article.setTitle(resultSet.getString(i));
                    break;
                case "text":
                    article.setText(resultSet.getString(i));
                    break;
                case "hidden":
                    article.setHidden(resultSet.getBoolean(i));
                    break;
                case "creationTime":
                    article.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return article;
    }

    @Override
    protected String tableName() {
        return "Article";
    }
}
