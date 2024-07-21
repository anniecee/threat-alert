package cmpt276.project.threatalert.models;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="websites")
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wid;

    private String threatlevel;
    private Date date;

    @Column(unique=true)
    private String link;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "website")
    private List<Comment> comments;

    public Website() { 
    }

    public Website(Date date, String link, List<Comment> comments) {
        this.date = date;
        this.link = link;
        this.comments = comments;
    }

    public Website(Date date, String link) {
        this.date = date;
        this.link = link;
    }

    public Website(String link, String threatlevel) {
        this.link = link;
        this.threatlevel = threatlevel;
        this.date = new Date();    
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThreatlevel() {
        return threatlevel;
    }

    public void setThreatlevel(String threatlevel) {
        this.threatlevel = threatlevel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComment() {
        return comments;
    }

    public void setComment(List<Comment> comment) {
        this.comments = comment;
    }
    
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }
    
}