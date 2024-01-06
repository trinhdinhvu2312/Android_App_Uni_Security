package StoreApp.StoreApp.model;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String userId;
    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
