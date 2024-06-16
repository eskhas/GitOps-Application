package org.person.myapp.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Images")
public class Image {
    @Id
    @GeneratedValue
    private int id;

    private String url;

    @OneToOne
    private Person owner;

    public Image(String name, Person owner) {
        this.url = name;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Image [id=" + id + ", url=" + url + ", owner=" + owner + "]";
    }

}