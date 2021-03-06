package mate_sebestyen.test.blogengine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mate_sebestyen.test.blogengine.api.data.converter.CategoryConverter;

import javax.persistence.*;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @JsonSerialize(converter = CategoryConverter.class)
    @OneToOne
    private Category category;

    private Tag() {
    }

    public Tag(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws TagNameException {
        if (name.length() < 3 || name.length() > 10)
            throw new TagNameException();
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public class TagNameException extends Throwable {
        TagNameException() {
            super("Tag name must be between 3 and 10 characters");
        }
    }
}
