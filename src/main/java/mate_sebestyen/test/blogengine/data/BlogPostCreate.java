package mate_sebestyen.test.blogengine.data;

import java.util.List;

public class BlogPostCreate {
    public String title;
    public String content;
    public List<Long> tagIds;

    @Override
    public String toString() {
        return "BlogPostCreate{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tagIds=" + tagIds +
                '}';
    }
}