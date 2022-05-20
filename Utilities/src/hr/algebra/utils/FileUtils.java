/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class FileUtils {

    private static final String NO_IMAGE = "no_image.png";
    private static final String UPLOAD = "Upload";
    private static final String SAVE = "Save";

    public static File uploadFile(String description, String...extensions) {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        chooser.setDialogTitle(UPLOAD);
        chooser.setApproveButtonText(UPLOAD);
        chooser.setApproveButtonToolTipText(UPLOAD);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
            return Arrays.asList(extensions).contains(extension.toLowerCase()) ? selectedFile : null;            
        }
        return null;
    }

    public static File saveFile(String description, String...extensions){
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        chooser.setDialogTitle(SAVE);
        chooser.setApproveButtonText(SAVE);
        chooser.setApproveButtonToolTipText(SAVE);
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();            
        } else {
            return null;
        }
    }
    
    public static void copyFromUrl(String source, String destination) throws MalformedURLException, IOException {
        String dir = destination.substring(0, destination.lastIndexOf((File.separator)));
        if (!Files.exists(Paths.get(dir))) {
            Files.createDirectories(Paths.get(dir));
        }
        URL url = new URL(source); 
        try(InputStream is = url.openStream()) {
            Files.copy(is, Paths.get(destination));
        }
    }

    public static void copy(String source, String destination) throws MalformedURLException, IOException {
        String dir = destination.substring(0, destination.lastIndexOf((File.separator)));
        if (!Files.exists(Paths.get(dir))) {
            Files.createDirectories(Paths.get(dir));
        }
        Files.copy(Paths.get(source), Paths.get(destination));
    }
    
    public static void deleteAllPictures(String destination) throws IOException {        
        File dir = new File(destination);        
        for (File file : dir.listFiles()){
            if (!file.isDirectory()) {
                if (!file.getName().equals(NO_IMAGE)) {
                    file.delete();
                }                
            }
        }
    }
    
    public static void deleteFile(String picturePath) throws IOException {
        File picture = new File(picturePath);
        picture.delete();
    }
    
    public static File setFileExtension(File f, String newExt) {
        int i = f.getName().lastIndexOf('.');
        String name = i > -1 ? f.getName().substring(0, i + 1) : f.getName() + ".";
        return new File(f.getParent(), name + newExt);
    }
}