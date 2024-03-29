/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.user;

import hr.algebra.Login;
import hr.algebra.dal.Repo;
import hr.algebra.model.Article;
import hr.algebra.utils.MessageUtils;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author C
 */
public class UserHomeForm extends javax.swing.JFrame {

    private static final String READ_ARTICLES = "Read articles";
    private static final String EDIT_ARTICLES = "Edit articles";
    private static final String EDIT_CREATORS = "Edit creators";
    private static final String EDIT_CATEGORIES = "Edit categories";
    private static final String XML_DOWNLOAD = "Download XML";
    
    private final int userId;
    
    private DefaultListModel<Article> articlesModel;
    private Repo repo;
    /**
     * Creates new form ViewArticlesForm
     * @param userId
     */
    public UserHomeForm(int userId) {
        this.userId = userId;
        initComponents();
        configurePanels();        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpContent = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miLogout = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("News Bar");
        setPreferredSize(new java.awt.Dimension(1100, 800));

        jMenu1.setText("File");

        miLogout.setText("Logout");
        miLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(miLogout);

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        jMenu1.add(miExit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpContent, javax.swing.GroupLayout.DEFAULT_SIZE, 997, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpContent, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void miLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miLogoutActionPerformed
        if (MessageUtils.showConfirmDialog("Logout?", "Logout from application?") == JOptionPane.YES_OPTION) {
            Login lf = new Login();
            lf.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_miLogoutActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        if (MessageUtils.showConfirmDialog("Exit?", "Exit app?") == JOptionPane.YES_OPTION) {
            System.exit(1);
        }
    }//GEN-LAST:event_miExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miLogout;
    private javax.swing.JTabbedPane tpContent;
    // End of variables declaration//GEN-END:variables

    private void configurePanels() {
        tpContent.add(READ_ARTICLES, new ViewArticlesPanel(userId));
        tpContent.add(EDIT_ARTICLES, new EditArticlesPanel());
        tpContent.add(EDIT_CATEGORIES, new EditCategoryPanel());
        tpContent.add(EDIT_CREATORS, new EditCreatorPanel());
        tpContent.add(XML_DOWNLOAD, new XmlDownloadPanel());
    }

    
}
