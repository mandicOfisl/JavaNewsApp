/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repo;
import hr.algebra.model.Article;
import hr.algebra.model.Category;
import hr.algebra.model.Creator;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author C
 */
public class SqlRepo implements Repo {
    
    private static final String ID_USER = "IdUser"; 
    private static final String NAME = "Name"; 
    private static final String PASS = "Pass";
    private static final String IS_ADMIN = "IsAdmin";
    private static final String ID_CREATOR = "IdCreator";    
    private static final String ID_CATEGORY = "IdCategory";    
    private static final String ID_ARTICLE = "IdArticle";
    private static final String TITLE = "Title";
    private static final String LINK = "Link";
    private static final String DESCRIPTION = "Description";
    private static final String PICTURE_PATH = "PicturePath";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String CREATOR = "Creator";
    private static final String CATEGORY = "Category";
    private static final String CONTENT = "Content";
    
    private static final String CREATE_USER = "{ CALL createUser(?, ?, ?) }";
    private static final String GET_USER_BY_NAME = "{ CALL getUserByName(?) }";
    private static final String CREATE_CREATOR = "{ CALL createCreator(?, ?) }";
    private static final String GET_CREATORS = "{ CALL getCreators }";
    private static final String CREATE_CATEGORY = "{ CALL createCategory(?, ?) }";
    private static final String GET_CATEGORIES = "{ CALL getCategories }";
    private static final String CREATE_ARTICLE = "{ CALL createArticle(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String UPDATE_ARTICLE = "{ CALL updateArticle(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String DELETE_ARTICLE = "{ CALL deleteArticle(?) }";
    private static final String SELECT_ARTICLE = "{ CALL selectArticle(?) }";
    private static final String SELECT_ARTICLES = "{ CALL selectArticles }";
    private static final String DELETE_ARTICLES = "{ CALL deleteArticles }";
    private static final String DELETE_ALL_DATA = "{ CALL deleteAllData }";
    private static final String DELETE_CREATOR = "{ CALL deleteCreator(?) }";
    private static final String DELETE_CATEGORY = "{ CALL deleteCategory(?) }";
    private static final String UPDATE_CREATOR = "{ CALL updateCreator(?, ?) }";
    private static final String UPDATE_CATEGORY = "{ CALL updateCategory(?, ?) }";
    private static final String SELECT_FAVOURITE_ARTICLES = "{ CALL selectFavouriteArticles(?) }";
    private static final String ADD_FAVOURITE_ARTICLE = "{ CALL addFavouriteArticle(?, ?) }";
    private static final String REMOVE_FAVOURITE_ARTICLE = "{ CALL removeFavouriteArticle(?, ?) }";

    @Override
    public int createUser(String name, String pass) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {

            stmt.setString(1, name);
            stmt.setString(2, pass);

            stmt.registerOutParameter(3, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(3);
        }
    }

    @Override
    public Optional<User> getUserByName(String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_USER_BY_NAME)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(NAME),
                            rs.getString(PASS),
                            rs.getBoolean(IS_ADMIN)));
                }
            }
        }
        return Optional.empty();
    }    
    
    @Override
    public int createCreator(String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_CREATOR)) {

            stmt.setString(1, name);

            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }

    @Override
    public List<Creator> getCreators() throws Exception {
        List<Creator> creators = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_CREATORS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                creators.add(new Creator(
                        rs.getInt(ID_CREATOR),
                        rs.getString(NAME)));
            }
        }

        return creators;
    }

    @Override
    public int createCategory(String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_CATEGORY)) {

            stmt.setString(1, name);

            stmt.registerOutParameter(2, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(2);
        }
    }

    @Override
    public List<Category> getCategories() throws Exception {
        List<Category> categories = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(GET_CATEGORIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt(ID_CATEGORY),
                        rs.getString(NAME)));
            }
        }

        return categories;
    }

    @Override
    public int createArticle(Article article) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getLink());
            stmt.setString(3, article.getPicturePath());
            stmt.setString(4, article.getDescription());
            stmt.setString(5, article.getContent());
            
            int creatorID = -1;
            for (Creator creator : getCreators()) {
                if (creator.getName().equals(article.getCreator())) {
                    creatorID = creator.getId();
                    break;
                }
            }
            if (creatorID != -1) {
                stmt.setInt(6, creatorID);
            } else {
                stmt.setInt(6, createCreator(article.getCreator()));
            }  
            
            stmt.setString(7, article.getPublishedDate().format(Article.DATE_FORMATTER));
            
            int categoryID = -1;
            for (Category cat : getCategories()) {
                if (cat.getName().equals(article.getCategory())) {
                    categoryID = cat.getId();
                    break;
                }
            }
            if (categoryID != -1) {
                stmt.setInt(8, categoryID);
            } else {
                stmt.setInt(8, createCategory(article.getCategory()));
            }
            
            stmt.registerOutParameter(9, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(9);
        }
    }
    
    @Override
    public void createArticles(List<Article> articles) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {

            for (Article article : articles) {
                stmt.setString(1, article.getTitle());
                stmt.setString(2, article.getLink());
                stmt.setString(3, article.getPicturePath());
                stmt.setString(4, article.getDescription());
                stmt.setString(5, article.getContent());
                
                
                int creatorID = -1;
                for (Creator creator : getCreators()) {
                    if (creator.getName().equals(article.getCreator())) {
                        creatorID = creator.getId();
                        break;
                    }
                }
                if (creatorID != -1) {
                    stmt.setInt(6, creatorID);
                } else {
                    stmt.setInt(6, createCreator(article.getCreator()));
                }
                
                stmt.setString(7, article.getPublishedDate().format(Article.DATE_FORMATTER));
                
                
                int categoryID = -1;
                for (Category cat : getCategories()) {
                    if (cat.getName().equals(article.getCategory())) {
                        categoryID = cat.getId();
                        break;
                    }
                }
                if (categoryID != -1) {
                    stmt.setInt(8, categoryID);
                } else {
                    stmt.setInt(8, createCategory(article.getCategory()));
                }                
                
                stmt.registerOutParameter(9, Types.INTEGER);
                
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateArticle(int id, Article article) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ARTICLE)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, article.getTitle());
            stmt.setString(3, article.getLink());
            stmt.setString(4, article.getPicturePath());
            stmt.setString(5, article.getDescription());
            stmt.setString(6, article.getContent());
            
            int creatorID = -1;
            for (Creator creator : getCreators()) {
                if (creator.getName().equals(article.getCreator())) {
                    creatorID = creator.getId();
                    break;
                }
            }
            if (creatorID != -1) {
                stmt.setInt(7, creatorID);
            } else {
                stmt.setInt(7, createCreator(article.getCreator()));
            }
            
            stmt.setString(8, article.getPublishedDate().format(Article.DATE_FORMATTER));
            
            int categoryID = -1;
            for (Category cat : getCategories()) {
                if (cat.getName().equals(article.getCategory())) {
                    categoryID = cat.getId();
                    break;
                }
            }
            if (categoryID != -1) {
                stmt.setInt(9, categoryID);
            } else {
                stmt.setInt(9, createCategory(article.getCategory()));
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Article> selectArticle(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTICLE)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Article(
                            rs.getInt(ID_ARTICLE),
                            rs.getString(TITLE),
                            rs.getString(LINK),
                            rs.getString(PICTURE_PATH),
                            rs.getString(DESCRIPTION),
                            rs.getString(CONTENT),
                            rs.getString(CREATOR),
                            LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Article.DATE_FORMATTER),
                            rs.getString(CATEGORY)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Article> selectArticles() throws Exception {
        List<Article> articles = new ArrayList<>();
        
        DataSource dataSource = DataSourceSingleton.getInstance();
        
        
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ARTICLES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt(ID_ARTICLE),
                        rs.getString(TITLE),
                        rs.getString(LINK),
                        rs.getString(PICTURE_PATH),
                        rs.getString(DESCRIPTION),
                        rs.getString(CONTENT),
                        rs.getString(CREATOR),
                        LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Article.DATE_FORMATTER),
                        rs.getString(CATEGORY)));
            }
        }

        return articles;
    }

    @Override
    public void deleteArticle(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ARTICLE)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteArticles() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ARTICLES)) {
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAllData() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ALL_DATA)){
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteCreator(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_CREATOR)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteCategory(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_CATEGORY)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public void updateCreator(int id, String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_CREATOR)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, name);

            stmt.executeUpdate();
        }
    }

    @Override
    public void updateCategory(int id, String name) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_CATEGORY)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, name);

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Article> selectFavouriteArticles(int userId) throws Exception {
        List<Article> articles = new ArrayList<>();
        
        DataSource dataSource = DataSourceSingleton.getInstance();        
        
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_FAVOURITE_ARTICLES)){
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    articles.add(new Article(
                            rs.getInt(ID_ARTICLE),
                            rs.getString(TITLE),
                            rs.getString(LINK),
                            rs.getString(PICTURE_PATH),
                            rs.getString(DESCRIPTION),
                            rs.getString(CONTENT),
                            rs.getString(CREATOR),
                            LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Article.DATE_FORMATTER),
                            rs.getString(CATEGORY)));
                }
            }
        }

        return articles;
    }

    @Override
    public void addFavouriteArticle(int userId, int articleId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADD_FAVOURITE_ARTICLE)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, articleId);

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeFavouriteArticle(int userId, int articleId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REMOVE_FAVOURITE_ARTICLE)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, articleId);

            stmt.executeUpdate();
        }
    }
    
}