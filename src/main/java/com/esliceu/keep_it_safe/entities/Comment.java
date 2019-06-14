package com.esliceu.keep_it_safe.entities;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long comment_id;

    @Column(nullable = false, name = "comment_text")
    private String comment_text;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String stringToJSON() {

        String stringUser = this.user == null ? "null" : this.user.stringToJSON();

        return  "{\"comment_id\":" + this.comment_id + "," +
                "\"comment_text\":\"" + this.comment_text + "\"," +
                "\"user\":" + stringUser + "}";
    }
}
