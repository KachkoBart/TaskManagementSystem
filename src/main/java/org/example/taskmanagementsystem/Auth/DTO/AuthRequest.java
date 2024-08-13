package org.example.taskmanagementsystem.Auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication request")
public class AuthRequest {
    @NotBlank
    @Email(message = "Username should be correct Email address.")
    @Schema(description = "Email")
    private String email;

    @NotBlank
    @Schema(description = "Password")
    private String password;
}
