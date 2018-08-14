package mate_sebestyen.test.blogengine.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mate_sebestyen.test.blogengine.api.data.converter.TagSetConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String content;

    @JsonSerialize(converter = TagSetConverter.class)
    @ManyToMany
    private Set<Tag> tags;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    private BlogPost() {
    }

    public BlogPost(String title, String content) {
        this.title = title;
        this.content = content;
        this.tags = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreation() {
        return creation;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
