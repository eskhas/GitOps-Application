package org.person.myapp.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @Column(name="name", length = 16)
    private String name;
    @Column(name="address", length = 32)
    private String address;
    @Column(name="img_url", length = 1024)
    private String img_url;

    public Person() {
    }

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person(id=" + this.getPersonId() + ", name=" + this.getName() + ", address=" + this.getAddress()+")";
    }
}

