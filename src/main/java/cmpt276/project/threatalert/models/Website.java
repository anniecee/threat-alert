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

    private int malicious;
    private int suspicious;
    private int undetected;
    private int harmless;
    private int timeout;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "website")
    private List<Comment> comments;

    public Website() { 
    }

    public Website(String link, String threatlevel) {
        this.link = link;
        this.threatlevel = threatlevel;
        this.date = new Date();    
    }

    public Website(String link, int malicious, int suspicious, int undetected, int harmless, int timeout) {
        this.link = link;
        this.malicious = malicious;
        this.suspicious = suspicious;
        this.undetected = undetected;
        this.harmless = harmless;
        this.timeout = timeout;
        if (malicious + suspicious > 5) {
            this.threatlevel = "Warning!";
        }
        else {
            this.threatlevel = "Clean!";
        }
        this.date = new Date();
        this.comments = new ArrayList<>();
    }
    

    // public Website(String link, int malicious, int suspicious, int undetected, int harmless, int timeout,
    //         List<Comment> comments) {
    //     this.link = link;
    //     this.malicious = malicious;
    //     this.suspicious = suspicious;
    //     this.undetected = undetected;
    //     this.harmless = harmless;
    //     this.timeout = timeout;
    //     this.comments = comments;
    // }

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

    public int getMalicious() {
        return malicious;
    }

    public void setMalicious(int malicious) {
        this.malicious = malicious;
    }

    public int getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(int suspicious) {
        this.suspicious = suspicious;
    }

    public int getUndetected() {
        return undetected;
    }

    public void setUndetected(int undetected) {
        this.undetected = undetected;
    }

    public int getHarmless() {
        return harmless;
    }

    public void setHarmless(int harmless) {
        this.harmless = harmless;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }
    
}