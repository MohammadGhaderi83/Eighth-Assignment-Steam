package Shared;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String password;
    private String dateOfBirth;

    public User(String id, String username, String password, String dateOfBirth) {
        this.id = String.valueOf(UUID.randomUUID());
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String username, String password, String dateOfBirth) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return hashedPassword(hashedPassword(password));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String hashedPassword(String password){
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public String DateFormat(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.getDateOfBirth());
    }
    public static Boolean checkPassword(String password, String hashedPass){
        return BCrypt.checkpw(password, hashedPass);
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + hashedPassword(password) + '\'' +
                ", dateOfBirth=" + DateFormat() +
                '}';
    }
}

