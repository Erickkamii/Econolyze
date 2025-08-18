package com.econolyze.dev.dto;

import com.econolyze.dev.util.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Informe o nome de usuário")
    public String username;
    @Email
    @NotBlank(message="Informe o email")
    public String email;
    @StrongPassword

    public String password;
    public String roles;
}
