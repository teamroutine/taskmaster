package fi.haagahelia.taskmaster.taskmaster.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterUserDto {
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;

    public RegisterUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterUserDto() {

    }

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

}
