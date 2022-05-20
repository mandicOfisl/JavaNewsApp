/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C
 */

@XmlRootElement(name = "category")
@XmlAccessorType (XmlAccessType.FIELD)
public class Category {
    
    private int id;
    private String name;

    public Category() {
    }

    public Category(String Name) {
        this.name = Name;
    }

    public Category(int id, String Name) {
        this.id = id;
        this.name = Name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    @Override
    public String toString() {
        return name;
    }
     
}
