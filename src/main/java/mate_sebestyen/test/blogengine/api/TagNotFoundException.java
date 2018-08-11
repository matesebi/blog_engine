package mate_sebestyen.test.blogengine.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class TagNotFoundException extends RuntimeException {
    TagNotFoundException(Long id) {
        super("Blog post with id: " + id + " not found");
    }
}
