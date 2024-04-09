package ua.kiev.prog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.kiev.prog.model.CustomAdmin;
import ua.kiev.prog.model.CustomUser;
import ua.kiev.prog.repo.AdminRepository;
import ua.kiev.prog.repo.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public UserService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public void addUser(CustomUser customUser) {
        customUser.setAdmin(userRepository.count() == 0);
        userRepository.save(customUser);
    }

    @Transactional
    public void addAdmin(CustomAdmin customAdmin) {
        adminRepository.save(customAdmin);
    }

    @Transactional(readOnly = true)
    public Page<CustomUser> find(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CustomUser> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public List<CustomUser> findNewUsers() {
        List<CustomUser> customUsers = userRepository.findNewUsers();

        customUsers.forEach((user) -> user.setNotified(true));
        userRepository.saveAll(customUsers);

        return customUsers;
    }

    @Transactional(readOnly = true)
    public CustomUser findByChatId(long id) {
        return userRepository.findByChatId(id);
    }

    @Transactional
    public CustomAdmin findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    @Transactional
    public void updateUser(CustomUser customUser) {
        userRepository.save(customUser);
    }

    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }
}

