/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C
 */

@XmlRootElement(name = "article")
@XmlAccessorType (XmlAccessType.FIELD)
public class Article {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
    private int id;
    private String title;
    private String link;
    private String picturePath;
    private String description;
    private String content;
    private String creator;
    private LocalDateTime publishedDate;
    private String category;
    
    public Article() {
    }

    public Article(int id, 
            String title, 
            String link, 
            String picturePath, 
            String description, 
            String content, 
            String creator, 
            LocalDateTime publishedDate, 
            String category) {
        this(title, link, picturePath, description, content, creator, publishedDate, category);
        this.id = id;
    }    
    
    public Article(String title, String link, String picturePath, String description, String content, String creator, LocalDateTime publishedDate, String category) {
        this.title = title;
        this.link = link;
        this.picturePath = picturePath;
        this.description = description;
        this.content = content;
        this.creator = creator;
        this.publishedDate = publishedDate;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category + " - " + title;
    }
    
}
