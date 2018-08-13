package mate_sebestyen.test.blogengine.dal;

import mate_sebestyen.test.blogengine.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
