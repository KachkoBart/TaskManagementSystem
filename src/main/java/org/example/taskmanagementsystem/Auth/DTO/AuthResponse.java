package org.example.taskmanagementsystem.Auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Server response for authentication/registration request")
public class AuthResponse {
    @Schema(description = "JWT-token")
    private String token;
}
