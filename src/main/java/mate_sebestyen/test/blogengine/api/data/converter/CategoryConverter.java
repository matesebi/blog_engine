package mate_sebestyen.test.blogengine.api.data.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import mate_sebestyen.test.blogengine.model.Category;

public class CategoryConverter implements Converter<Category, Long> {

    @Override
    public Long convert(Category value) {
        return value.getId();
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(Category.class, null);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructSimpleType(Long.class, null);
    }
}
