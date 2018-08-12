package mate_sebestyen.test.blogengine.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException extends RuntimeException {
    NotFoundException(String what, Long id) {
        super(what + " with id: '" + id + "' not found");
    }
}
