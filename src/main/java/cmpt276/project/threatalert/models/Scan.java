package cmpt276.project.threatalert.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scans")
public class Scan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sid;

    private Date scanDate;
    private boolean bookmark;
    private Date bookmarkDate;
    private boolean toDelete;

    @ManyToOne
    private User user;

    @ManyToOne
    private Website website;

    public Scan() {}

    public Scan(Website website) {
        this.website = website;
        this.scanDate = new Date();
        this.bookmark = false;
        this.toDelete = false;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Date getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(Date bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    


}
