package mate_sebestyen.test.blogengine.api.data.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import mate_sebestyen.test.blogengine.model.Tag;

import java.util.HashSet;
import java.util.Set;

public class TagSetConverter implements Converter<Set<Tag>, Set<Long>> {

    @Override
    public Set<Long> convert(Set<Tag> value) {
        Set<Long> ids = new HashSet<>();
        value.forEach(tag -> ids.add(tag.getId()));
        return ids;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructCollectionType(Set.class, Tag.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructCollectionType(Set.class, Long.class);
    }
}
