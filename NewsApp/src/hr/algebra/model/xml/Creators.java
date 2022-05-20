/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model.xml;

import hr.algebra.model.Creator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C
 */

@XmlRootElement(name = "creators")
@XmlAccessorType (XmlAccessType.FIELD)
public class Creators {
    
    @XmlElement(name = "creator")
    private List<Creator> creators = null;

    public List<Creator> getCreators() {
        return creators;
    }

    public void setCreators(List<Creator> creators) {
        this.creators = creators;
    }
    
}
