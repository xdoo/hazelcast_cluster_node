package de.muenchen.datagrid.node;

import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
public class Person implements Serializable {
    
    String oid;
    String vorname;
    String nachname;

    public Person() {
    }

    public Person(String oid, String vorname, String nachname) {
        this.oid = oid;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    
}
