package com.meesho.notificationservice.data.request;
import org.springframework.beans.factory.annotation.Autowired;

public class Text {

    @Autowired
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
