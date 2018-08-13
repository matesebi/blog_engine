package mate_sebestyen.test.blogengine.dal;

import mate_sebestyen.test.blogengine.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
