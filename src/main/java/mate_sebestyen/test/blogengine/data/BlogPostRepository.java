package mate_sebestyen.test.blogengine.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Collection<BlogPost> findByTagsIsContaining(Tag tag);
}
