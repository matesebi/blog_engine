package mate_sebestyen.test.blogengine.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class InvalidFieldsException extends RuntimeException {
    InvalidFieldsException(String field) {
        super("The following field is null or empty: '" + field + "'");
    }
}
