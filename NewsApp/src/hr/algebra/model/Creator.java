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

@XmlRootElement(name = "creator")
@XmlAccessorType (XmlAccessType.FIELD)
public class Creator {
    
    private int id;
    private String name;

    public Creator() {
    }

    public Creator(String name) {
        this.name = name;
    }

    public Creator(int id, String name) {
        this(name);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() { return name; }
       
}
