package org.person.myapp.Services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("image")
public class ImageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "/path/to/your/static/folder/in/your-app/static/images";
//    private String location = "/home/ubuntu/Pictures";
}
