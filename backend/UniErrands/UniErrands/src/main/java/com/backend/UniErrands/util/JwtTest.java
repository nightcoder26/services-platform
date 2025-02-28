package com.backend.UniErrands.util;

public class JwtTest {
    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        
        String token = jwtUtil.generateToken("test@example.com");
        System.out.println("Generated Token: " + token);

        String email = jwtUtil.extractEmail(token);
        System.out.println("Extracted Email: " + email);

        boolean isValid = jwtUtil.validateToken(token, "test@example.com");
        System.out.println("Is Token Valid? " + isValid);
    }
}
