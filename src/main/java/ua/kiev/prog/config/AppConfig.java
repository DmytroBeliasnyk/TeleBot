package ua.kiev.prog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.kiev.prog.model.CustomAdmin;
import ua.kiev.prog.service.UserService;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public CommandLineRunner commandLineRunner(final UserService userService,
                                               final PasswordEncoder encoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                CustomAdmin admin = new CustomAdmin("admin",
                        encoder.encode("1234"));
                userService.addAdmin(admin);
            }
        };
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin")
                .setViewName("redirect:/admin/");
        registry.addViewController("/admin/")
                .setViewName("forward:/index.html");
    }
}
