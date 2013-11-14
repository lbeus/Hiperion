package org.hiperion.connector.test;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 04.04.13.
 * Time: 21:19
 * To change this template use File | Settings | File Templates.
 */
public class Account implements Serializable {
    private String firstName;
    private String lastName;

    public Account() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
