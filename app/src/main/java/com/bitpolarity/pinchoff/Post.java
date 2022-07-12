package com.bitpolarity.pinchoff;

public class Post {
    String Text;
    int isPasted;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public int getIsPasted() {
        return isPasted;
    }

    public void setIsPasted(int isPasted) {
        this.isPasted = isPasted;
    }
}
