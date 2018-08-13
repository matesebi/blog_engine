package mate_sebestyen.test.blogengine.api.data;

import java.util.List;

public class BlogPostUpdate {
    public String title;
    public String content;
    public List<Long> newTagIds;
    public List<Long> deleteTagIds;
}
