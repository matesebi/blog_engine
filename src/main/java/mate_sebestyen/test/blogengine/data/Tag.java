package mate_sebestyen.test.blogengine.data;

import javax.persistence.*;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    private Category category;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws TagNameException {
        if (name.length() < 3 || name.length() > 10)
            throw new TagNameException("Tag name must be between 3 and 10 characters");
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    private class TagNameException extends Throwable {
        TagNameException(String s) {
            super(s);
        }
    }
}