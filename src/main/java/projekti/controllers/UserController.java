package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import projekti.models.User;
import projekti.repositories.TweetRepository;
import projekti.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Controller
public class UserController {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(WebRequest request, Model model) throws IOException {
//        User user = new User("john", "test", "John", "Doe");
//        model.addAttribute("user", user);
        Resource resource = resourceLoader.getResource("classpath:public/images/default.jpg");
        InputStream input = resource.getInputStream();

        File file = resource.getFile();
        System.out.println("FILELLELELELE ===> " + file);
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String password) throws IOException {
        if (userRepository.findByUsername(username) != null) {
            return "redirect:/";
        }

        Resource resource = resourceLoader.getResource("classpath:public/images/default.jpg");
        InputStream input = resource.getInputStream();

        File file = resource.getFile();
        byte[] fileContent = Files.readAllBytes(file.toPath());


        User user = new User(username, firstName, lastName, passwordEncoder.encode(password), Base64.getEncoder().encodeToString(fileContent));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfilePage(WebRequest request, Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        model.addAttribute("tweets", tweetRepository.findAll());
        return "register";
    }
}
