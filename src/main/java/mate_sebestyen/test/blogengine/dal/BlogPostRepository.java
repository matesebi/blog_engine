package mate_sebestyen.test.blogengine.dal;

import mate_sebestyen.test.blogengine.model.BlogPost;
import mate_sebestyen.test.blogengine.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Page<BlogPost> findAllByTagsContaining(Tag tag, Pageable pageable);
}
