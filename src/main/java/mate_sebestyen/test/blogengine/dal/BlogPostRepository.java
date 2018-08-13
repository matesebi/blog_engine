package mate_sebestyen.test.blogengine.dal;

import mate_sebestyen.test.blogengine.model.BlogPost;
import mate_sebestyen.test.blogengine.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Collection<BlogPost> findByTagsIsContaining(Tag tag);

    Page<BlogPost> findAllByTagsContaining(Tag tag, Pageable pageable);
}
