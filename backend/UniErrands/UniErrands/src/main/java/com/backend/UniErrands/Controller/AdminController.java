package com.backend.UniErrands.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.backend.UniErrands.model.User; // Import User model

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.backend.UniErrands.model.Admin;
import com.backend.UniErrands.service.AdminService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        // Additional validation for user roles can be added here

        // Encrypt the password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin newAdmin = adminService.createAdmin(admin);
        return ResponseEntity.ok(newAdmin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        // Check if the admin exists before returning

        Optional<Admin> admin = adminService.getAdminById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        // Optionally, return only active admins

        return ResponseEntity.ok(adminService.getAllAdmins());
    }
}
