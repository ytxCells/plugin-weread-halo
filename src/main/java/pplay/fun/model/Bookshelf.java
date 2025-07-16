package pplay.fun.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "Bookshelf",
    plural = "bookshelfs",
    singular = "bookshelf"
)
public class Bookshelf extends AbstractExtension {
    private BookShelfSpec spec;

    @Data
    public static class BookShelfSpec {
        @Schema(description = "书籍数量（包含导入书籍/有声书）")
        private int pureBookCount;
        @Schema(description = "书籍数量")
        private int bookCount;
        @Schema(description = "书籍进度")
        private List<BookProgress> bookProgress = Collections.emptyList();
        @Schema(description = "分组")
        private List<Archive> archive = Collections.emptyList();
        @Schema(description = "书籍列表")
        private List<Book> books = Collections.emptyList();
        @Schema(description = "微信公众号")
        private Mp mp;

        public void populateFromJson(JsonNode jsonNode) {
            if (jsonNode == null) {
                return;
            }

            if (jsonNode.has("pureBookCount")) {
                this.pureBookCount = jsonNode.get("pureBookCount").asInt();
            }

            if (jsonNode.has("bookCount")) {
                this.bookCount = jsonNode.get("bookCount").asInt();
            }

            if (jsonNode.has("bookProgress") && jsonNode.get("bookProgress").isArray()) {
                this.bookProgress = parseBookProgressList(jsonNode.get("bookProgress"));
            }

            if (jsonNode.has("archive") && jsonNode.get("archive").isArray()) {
                this.archive = parseArchiveList(jsonNode.get("archive"));
            }

            if (jsonNode.has("books") && jsonNode.get("books").isArray()) {
                this.books = parseBookList(jsonNode.get("books"));
            }

            if (jsonNode.has("mp")) {
                this.mp = parseMp(jsonNode.get("mp"));
            }
        }

        private List<BookProgress> parseBookProgressList(JsonNode progressNode) {
            List<BookProgress> progressList = new ArrayList<>();
            progressNode.forEach(item -> {
                BookProgress progress = new BookProgress();
                progress.populateFromJson(item);
                progressList.add(progress);
            });
            return progressList;
        }

        private List<Book> parseBookList(JsonNode booksNode) {
            List<Book> bookList = new ArrayList<>();
            booksNode.forEach(bookNode -> {
                Book book = new Book();
                book.populateFromJson(bookNode);
                bookList.add(book);
            });
            return bookList;
        }

        private List<Archive> parseArchiveList(JsonNode archiveNode) {
            List<Archive> archiveList = new ArrayList<>();
            archiveNode.forEach(item -> {
                Archive archive = new Archive();
                archive.populateFromJson(item);
                archiveList.add(archive);
            });
            return archiveList;
        }

        private Mp parseMp(JsonNode mpNode) {
            Mp mp = new Mp();
            mp.populateFromJson(mpNode);
            return mp;
        }
    }

    @Data
    public static class Book {
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

        public void populateFromJson(JsonNode bookNode) {
            if (bookNode == null) {
                return;
            }

            if (bookNode.has("bookId")) {
                this.bookId = bookNode.get("bookId").asText();
            }
            if (bookNode.has("title")) {
                this.title = bookNode.get("title").asText();
            }
            if (bookNode.has("author")) {
                this.author = bookNode.get("author").asText();
            }
            if (bookNode.has("cover")) {
                this.cover = bookNode.get("cover").asText();
            }
            if (bookNode.has("version")) {
                this.version = bookNode.get("version").asInt();
            }
            if (bookNode.has("format")) {
                this.format = bookNode.get("format").asText();
            }
            if (bookNode.has("type")) {
                this.type = bookNode.get("type").asInt();
            }
            if (bookNode.has("price")) {
                this.price = bookNode.get("price").asInt();
            }
            if (bookNode.has("originalPrice")) {
                this.originalPrice = bookNode.get("originalPrice").asInt();
            }
            if (bookNode.has("bookStatus")) {
                this.bookStatus = bookNode.get("bookStatus").asInt();
            }
            if (bookNode.has("payingStatus")) {
                this.payingStatus = bookNode.get("payingStatus").asInt();
            }
            if (bookNode.has("category")) {
                this.category = bookNode.get("category").asText();
            }

            if (bookNode.has("categories") && bookNode.get("categories").isArray()) {
                this.categories = parseCategories(bookNode.get("categories"));
            }
        }

        private List<Category> parseCategories(JsonNode categoriesNode) {
            List<Category> categoryList = new ArrayList<>();
            categoriesNode.forEach(catNode -> {
                Category category = new Category();
                category.populateFromJson(catNode);
                categoryList.add(category);
            });
            return categoryList;
        }

    }

    @Data
    public static class BookProgress {
        private String bookId;
        private Integer progress;

        private Integer chapterUid;
        private Integer chapterOffset;

        private Integer chapterIdx;
        private String appid;
        private String updateTime;
        private String readingTime;

        private Long synckey;

        public void populateFromJson(JsonNode progressNode) {
            if (progressNode == null) {
                return;
            }
            if (progressNode.has("bookId")) {
                this.bookId = progressNode.get("bookId").asText();
            }
            if (progressNode.has("progress")) {
                this.progress = progressNode.get("progress").asInt();
            }
            if (progressNode.has("chapterUid")) {
                this.chapterUid = progressNode.get("chapterUid").asInt();
            }
            if (progressNode.has("chapterOffset")) {
                this.chapterOffset = progressNode.get("chapterOffset").asInt();
            }
            if (progressNode.has("chapterIdx")) {
                this.chapterIdx = progressNode.get("chapterIdx").asInt();
            }
            if (progressNode.has("appId")) {
                this.appid = progressNode.get("appId").asText();
            }
            if (progressNode.has("updateTime")) {
                this.updateTime = String.valueOf(progressNode.get("updateTime").asLong());
            }
            if (progressNode.has("readingTime")) {
                this.readingTime = String.valueOf(progressNode.get("readingTime").asLong());
            }
            if (progressNode.has("synckey")) {
                this.synckey = progressNode.get("synckey").asLong();
            }
        }

    }

    @Data
    public static class Archive {
        private Integer archiveId;
        private String name;
        private List<String> bookIds;

        public void populateFromJson(JsonNode archiveNode) {
            if (archiveNode == null) {
                return;
            }

            if (archiveNode.has("archiveId")) {
                this.archiveId = archiveNode.get("archiveId").asInt();
            }
            if (archiveNode.has("name")) {
                this.name = archiveNode.get("name").asText();
            }

            if (archiveNode.has("bookIds") && archiveNode.get("bookIds").isArray()) {
                this.bookIds = new ArrayList<>();
                archiveNode.get("bookIds").forEach(idNode ->
                    this.bookIds.add(idNode.asText())
                );
            }
        }
    }

    @Data
    public static class Category {
        private Integer categoryId;
        private Integer subCategoryId;
        private Integer categoryType;
        private String title;

        public void populateFromJson(JsonNode categoryNode) {
            if (categoryNode == null) {
                return;
            }

            if (categoryNode.has("categoryId")) {
                this.categoryId = categoryNode.get("categoryId").asInt();
            }
            if (categoryNode.has("subCategoryId")) {
                this.subCategoryId = categoryNode.get("subCategoryId").asInt();
            }
            if (categoryNode.has("categoryType")) {
                this.categoryType = categoryNode.get("categoryType").asInt();
            }
            if (categoryNode.has("title")) {
                this.title = categoryNode.get("title").asText();
            }
        }
    }

    @Data
    public static class Mp {
        private Integer show;
        private Book book;

        public void populateFromJson(JsonNode mpNode) {
            if (mpNode == null) {
                return;
            }

            if (mpNode.has("show")) {
                this.show = mpNode.get("show").asInt();
            }

            if (mpNode.has("book")) {
                Book book = new Book();
                book.populateFromJson(mpNode.get("book"));
                this.book = book;
            }
        }
    }

    // 为Bookshelf类添加便捷方法
    public void populateSpecFromJson(JsonNode jsonNode) {
        if (this.spec == null) {
            this.spec = new BookShelfSpec();
        }
        this.spec.populateFromJson(jsonNode);
    }
}

