package projekti.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekti.models.Tweet;
import projekti.repositories.TweetRepository;
import projekti.repositories.UserRepository;

@Controller
public class TweetController {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getTweets(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        model.addAttribute("tweets", tweetRepository.findAll());
        return "index";
    }

    @PostMapping("/tweets")
    public String addTweet(@RequestParam String content) {
        if (content != null && !content.trim().isEmpty()) {
            Tweet tweet = new Tweet();
            tweet.setContent(content.trim());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            tweet.setUser(userRepository.findByUsername(username));
            tweetRepository.save(tweet);
        }

        return "redirect:/";
    }
}
