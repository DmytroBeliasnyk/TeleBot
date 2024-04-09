package ua.kiev.prog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import ua.kiev.prog.dto.PageCountDTO;
import ua.kiev.prog.model.CustomUser;
import ua.kiev.prog.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class MyController {
    private final int PAGE_SIZE = 1;

    private final UserService userService;

    public MyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public List<CustomUser> showAllUsers(@RequestParam(name = "page", defaultValue = "0", required = false)
                                         int page) {
        if (page < 0) page = 0;
        return userService.find(
                PageRequest.of(page, PAGE_SIZE, Sort.Direction.ASC, "id")).getContent();
    }

    @GetMapping("pages")
    public PageCountDTO pages() {
        return PageCountDTO.of(userService.countUsers(), PAGE_SIZE);
    }
}
