/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Article;
import hr.algebra.model.Category;
import hr.algebra.model.Creator;
import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author C
 */
public interface Repo {
    
    int createUser(String name, String pass) throws Exception;
    Optional<User> getUserByName(String name) throws Exception;
    
    int createCreator(String name) throws Exception;
    List<Creator> getCreators() throws Exception;
    void updateCreator (int id, String name) throws Exception;
    void deleteCreator (int id) throws Exception;
        
    int createCategory(String name) throws Exception;
    List<Category> getCategories() throws Exception;
    void updateCategory (int id, String name) throws Exception;
    void deleteCategory(int id) throws Exception;
        
    int createArticle(Article article) throws Exception;
    void createArticles(List<Article> articles) throws Exception;    
    Optional<Article> selectArticle(int id) throws Exception;
    List<Article> selectArticles() throws Exception;
    void updateArticle(int id, Article article) throws Exception;
    void deleteArticle(int id) throws Exception;
    void deleteArticles() throws Exception;
    
    List<Article> selectFavouriteArticles(int userId) throws Exception;
    void addFavouriteArticle(int userId, int articleId) throws Exception;
    void removeFavouriteArticle(int userId, int articleId) throws Exception;
    
    void deleteAllData() throws Exception;
}
