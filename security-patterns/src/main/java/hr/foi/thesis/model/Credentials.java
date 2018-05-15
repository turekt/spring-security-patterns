package hr.foi.thesis.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Embeddable
public class Credentials implements Serializable {

    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(message = "Username can only consist of letters, numbers and underscores.", regexp = "\\w*")
    @Column(unique = true)
    private String username;
    
    @NotNull
    @Size(min = 4)
    private String password;
    private byte[] salt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
