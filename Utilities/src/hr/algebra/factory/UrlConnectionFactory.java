/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.factory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlConnectionFactory {

    public static HttpURLConnection getHttpUrlConnection(String path, int timeout, String requestMethod) 
            throws MalformedURLException, IOException {

        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        con.connect();
        return con;
    }
    
}
