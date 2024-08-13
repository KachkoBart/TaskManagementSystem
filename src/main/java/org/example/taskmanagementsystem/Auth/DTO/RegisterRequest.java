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
@Schema(description = "Information about registration request")
public class RegisterRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Username should be a correct Email address.")
    @Schema(description = "Email")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password")
    private String password;
}
