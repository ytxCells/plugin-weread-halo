package pplay.fun.extension;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "BookInfo",
    plural = "bookinfos",
    singular = "bookinfo"
)
public class BookInfo extends AbstractExtension {
    private BookInfoSpec spec;
    @Data
    public static class BookInfoSpec {
        @Schema(description = "书本id")
        private String bookId;
        private Integer bookVersion;
        private String title;
        private String author;
        private Long authorId;
        private String category;
        private Long categoryId;
        private Long subCategoryId;
        @Schema(description = "封面")
        private String cover;
        private Double price;
        @Schema(description = "简介")
        private String intro;
        @Schema(description = "出版社")
        private String publisher;
        @Schema(description = "出版时间")
        private String publishTime;
        private Long totalWords;
        private String AISummary;
    }
}
