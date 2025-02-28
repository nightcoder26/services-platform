package com.backend.UniErrands.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.UniErrands.repository.AdminRepository;
import com.backend.UniErrands.model.Admin;
import java.util.Optional;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
     private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }


public List<Admin> getAllAdmins() {
    return adminRepository.findAll();
}
}
