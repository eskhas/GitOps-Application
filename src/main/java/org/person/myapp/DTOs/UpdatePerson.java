package org.person.myapp.DTOs;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@Data
public class UpdatePerson {
        @Id
        private int personId;
        private String name;
        private String address;
        private String img;
}
