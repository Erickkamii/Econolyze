package com.econolyze.dev.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    public String username;
    public String email;
    public String password;
    public String roles;
}
