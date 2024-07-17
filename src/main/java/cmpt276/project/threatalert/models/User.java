package cmpt276.project.threatalert.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String email;
    private String password;
    private String type; //"regular" for regular user, "admin" for admin user

    @OneToMany(mappedBy = "user")
    private List<Website> history;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User() {}

    //constructor for regular user
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.type = "regular";
        this.history = new ArrayList<>();
    }

    //constructor for non-regular user (only admin for now)
    public User(String email, String password, String type) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.history = new ArrayList<>();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Website> getHistory() {
        return history;
    }

    public void setHistory(List<Website> history) {
        this.history = history;
    }

    public void addHistory(Website website) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(website);
    }

}
