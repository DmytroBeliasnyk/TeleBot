package ua.kiev.prog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kiev.prog.model.CustomAdmin;

public interface AdminRepository extends JpaRepository<CustomAdmin, Long> {
    CustomAdmin findByLogin(String login);
}
