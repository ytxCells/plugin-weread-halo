package pplay.fun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.util.List;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "bookshelf",
    plural = "bookshelfs",
    singular = "bookshelf"
)
public class BookShelf extends AbstractExtension {
    private BookShelfSpec spec;
    @Data
    public static class BookShelfSpec {
        @Schema(description = "用户ID", required = true)
        private String userId;
        private Long synckey;
        @Schema(description = "书籍数量（包含导入书籍）")
        private int pureBookCount;
        @Schema(description = "书籍数量")
        private int bookCount;
        @Schema(description = "书籍进度")
        private List<BookProgress> bookProgress;
        @Schema(description = "分组")
        private List<Archive> archive;
        @Schema(description = "书籍列表")
        private List<Book> books;
        @Schema(description = "微信公众号")
        private Mp mp;
    }

    @Data
    public static class  Book{
        private String bookId;
        private String title;
        private String author;
        private String cover;
        private Integer version;
        private String format;
        private Integer type;
        private Integer price;
        private Integer originalPrice;
        private Integer bookStatus;
        private Integer payingStatus;
        private String category;
        private List<Category> categories;

    }
    @Data
    public static class BookProgress{
        private String bookId;
        private Integer progress;

        private Integer chapterUid;
        private Integer chapterOffset;

        private Integer chapterIdx;
        private String appid;
        private String updateTime;
        private String readingTime;

        private Long synckey;

    }
    @Data
    public static class Archive{
        private Integer archiveId;
        private String name;
        private List<String> bookIds;
    }
    @Data
    public static class Category{
        private Integer categoryId;
        private Integer subCategoryId;
        private Integer categoryType;
        private String title;
    }
    @Data
    public static class Mp{
        private Integer show;
        private Book book;
    }
}
