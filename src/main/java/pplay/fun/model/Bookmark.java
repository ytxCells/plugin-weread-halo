package pplay.fun.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "Bookmark",
    plural = "bookmarks",
    singular = "bookmark"
)
@Data
public class Bookmark extends AbstractExtension {
    private BookmarkSpec spec;

    public void populateSpecFromJson(JsonNode jsonNode) {
        if (this.spec == null){
            this.spec = new BookmarkSpec();
        }
        this.spec.populateSpecFromJson(jsonNode);
    }

    @Data
    public static class BookmarkSpec{
        @Schema(description = "书本id")
        private String bookId;
        private Long bookVersion;
        @Schema(description = "划线ID")
        private String bookmarkId;
        @Schema(description = "章节id")
        private Integer chapterUid;
        @Schema(description = "划线内容")
        private String markText;
        private String range;
        private Integer style;
        private Integer type;
        private String createTime;

        public void populateSpecFromJson(JsonNode bookmarkNode) {
            if (bookmarkNode == null) return;
            // 提取字段值（带空值安全处理）
            this.bookId = bookmarkNode.path("bookId").asText(null);
            this.bookVersion = bookmarkNode.path("bookVersion").asLong(0);
            this.bookmarkId = bookmarkNode.path("bookmarkId").asText(null);
            this.chapterUid = bookmarkNode.path("chapterUid").asInt(0);
            this.markText = bookmarkNode.path("markText").asText("");
            this.range = bookmarkNode.path("range").asText("");
            this.style = bookmarkNode.path("style").asInt(0);
            this.type = bookmarkNode.path("type").asInt(0);
            this.createTime = bookmarkNode.path("createTime").asText("");
        }
    }
}
