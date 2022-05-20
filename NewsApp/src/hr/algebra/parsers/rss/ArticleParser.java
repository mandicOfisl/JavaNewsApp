/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Article;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author C
 */
public class ArticleParser {
    
    private static final String RSS_URL = "https://www.newsbar.hr/feed";
    private static final int TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";
        
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private static final Random RANDOM = new Random();
    
    
    public static List<Article> parse() throws IOException, XMLStreamException, Exception {
        List<Article> articles = new ArrayList<>();

        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL, TIMEOUT, REQUEST_METHOD);

        XMLEventReader reader = ParserFactory.createStaxParser(con.getInputStream());

        Optional<TagType> tagType = Optional.empty();
        Article article = null;
        StartElement startElement = null;        
        
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();                   
                    tagType = TagType.from(qName);
                    break;
                case XMLStreamConstants.CHARACTERS:

                    if (tagType.isPresent()) {

                        Characters characters = event.asCharacters();
                        String data = characters.getData().trim();
                        switch (tagType.get()) {
                            case ITEM:
                                article = new Article();
                                articles.add(article);
                                break;
                            case TITLE:
                                if (article != null && !data.isEmpty()) {
                                    article.setTitle(data);
                                }
                                break;
                            case LINK:
                                if (article != null && !data.isEmpty()) {
                                    article.setLink(data);
                                }
                                break;
                            case CREATOR:
                                if (article != null && !data.isEmpty()) {
                                    article.setCreator(data);                                    
                                }
                                break;
                            case PUB_DATE:
                                if (article != null && !data.isEmpty()) {
                                    LocalDateTime publishedDate = LocalDateTime.parse(data, DATE_FORMATTER);
                                    article.setPublishedDate(publishedDate);
                                }
                                break;
                            case CATEGORY:
                                if (article != null && !data.isEmpty()) {
                                    if (article.getCategory() == null) {
                                        article.setCategory(data); 
                                    }                                                                       
                                }
                                break;
                            case DESCRIPTION:
                                if (article != null && !data.isEmpty() && startElement != null) {
                                    String[] descData = parseDescriptionData(data);
                                    article.setDescription(descData[1]);
                                    handlePicture(article, descData[0]);                                    
                                }
                                break;
                            case ENCODED:
                                if (article != null && !data.isEmpty()) {                                    
                                    article.setContent(parseContent(data));
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        return articles;
    }

    private static void handlePicture(Article article, String pictureUrl) throws IOException {
        String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
        if (ext.length() > 4) {
            ext =  EXT;
        }
        
        String pictureName = Math.abs(RANDOM.nextInt()) + ext;
        String localPicturePath = DIR + File.separator + pictureName;
        

        try {
            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
        } catch (IOException exc) {
            
        }
        
        article.setPicturePath(localPicturePath);
    }

    private static String[] parseDescriptionData(String data) {
        String [] descData = data.split("<")[1].split(">");
        String[] arr = new String[2];
        arr[0] = descData[0].split("src=")[1].split(" ")[0].split("\"")[1].split("\"")[0]; //url
        arr[1] = descData[1]; //description
        
        return arr;        
    }

    private static String parseContent(String data) throws Exception {
        try {
            StringBuilder sb = new StringBuilder();
            String[] str = data.split("<p>");
            for (int i = 1; i < str.length; i++) {
                if (str[i].length() > 0) {
                    sb.append(str[i].split("</p>")[0]);
                }                
            }
            return sb.toString();
        } catch (Exception e) {
            return "Err";
        }        
    }
   
    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        LINK("link"),
        CREATOR("creator"),        
        PUB_DATE("pubDate"),
        CATEGORY("category"),
        DESCRIPTION("description"),
        ENCODED("encoded");

        private final String name;

        TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
}
