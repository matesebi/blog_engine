package mate_sebestyen.test.blogengine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mate_sebestyen.test.blogengine.api.data.converter.TagSetConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @JsonSerialize(converter = TagSetConverter.class)
    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<Tag> tags;

    private Category() {
    }

    public Category(String name) {
        this.name = name;
        this.tags = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }
}
