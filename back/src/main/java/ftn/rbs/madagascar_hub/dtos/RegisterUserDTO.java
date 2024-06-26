package ftn.rbs.madagascar_hub.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class RegisterUserDTO {
    @NotEmpty(message="is required")
	@Pattern(regexp = "^([a-zA-Zčćđžš ]*)$", message="format is not valid")
    private String name;

    @NotEmpty(message="is required")
	@Pattern(regexp = "^([a-zA-Zčćđžš ]*)$", message="format is not valid")
    private String surname;

    @NotEmpty(message="is required")
    @Pattern(regexp = "^([0-9a-z]{3,}$)")
    private String username;
    
    @NotEmpty(message="is required")
	@Email(message="format is not valid")
    private String email;

    @NotEmpty(message="is required")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
    private String password;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String name, String surname, String username, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
