package org.person.myapp.DTOs;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
public class SavePerson {
    @Id
    private int personId;
    private String name;
    private String address;
    private MultipartFile image;
}