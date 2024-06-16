package org.person.myapp.Services;

public class ImageFileNotFoundException  extends ImageException {

    public ImageFileNotFoundException(String message) {
        super(message);
    }

    public ImageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
