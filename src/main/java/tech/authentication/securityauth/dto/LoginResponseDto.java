package tech.authentication.securityauth.dto;

public record LoginResponseDto(String accessToken, Long expiresIn) {

}
