/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.dal.sql.SqlRepo;

/**
 *
 * @author C
 */
public class RepoFactory {
    
    private RepoFactory(){ }
    
    public static Repo getRepo() throws Exception {
        return new SqlRepo();
    }
}
