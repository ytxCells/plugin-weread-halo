package pplay.fun.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "Book",
    plural = "books",
    singular = "book"
)
@Data
public class Book extends AbstractExtension {
    private BookSpec spec;

    public void populateSpecFromJson(JsonNode bookNode) {
        if (this.spec==null){
            this.spec = new BookSpec();
        }
        this.spec.populateSpecFromJson(bookNode);
    }

    @Data
    public static class  BookSpec {
        @Schema(description = "书本id")
        private String bookId;
        private Long bookVersion;
        private String title;
        private String author;
        @Schema(description = "封面")
        private String cover;
        public void populateSpecFromJson(JsonNode bookNode) {
            if (bookNode == null) return;
            // 提取字段值（带空值安全处理）
            this.bookId = bookNode.path("bookId").asText(null);
            this.bookVersion = bookNode.path("bookVersion").asLong(0);
            this.title = bookNode.path("title").asText("");
            this.author = bookNode.path("author").asText("");
            this.cover = bookNode.path("cover").asText("");
        }
    }
}
