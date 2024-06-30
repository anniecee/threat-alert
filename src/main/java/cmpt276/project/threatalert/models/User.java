package cmpt276.project.threatalert.models;

import jakarta.persistence.*;

@Entity
@Table(name="users") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String email;
    private String password;

    public User() {}

    public User(int uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
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

}
