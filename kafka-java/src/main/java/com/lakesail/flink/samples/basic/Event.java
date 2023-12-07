package com.lakesail.flink.samples.basic;

public class Event {
    private String text;

    public Event() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Event toUpperCase() {
        Event event = new Event();
        if (this.text != null) {
            event.setText(this.text.toUpperCase());
        }
        return event;
    }
}
