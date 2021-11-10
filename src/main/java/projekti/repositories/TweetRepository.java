package projekti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.models.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
