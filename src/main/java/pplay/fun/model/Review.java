package pplay.fun.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "Review",
    plural = "reviews",
    singular = "review"
)
@Data
public class Review extends AbstractExtension {
    private ReviewSpec spec;

    public void populateSpecFromJson(JsonNode jsonNode) {
        if (this.spec == null){
            this.spec = new ReviewSpec();
        }
        this.spec.populateSpecFromJson(jsonNode);
    }

    @Data
    public static class ReviewSpec{
        private String bookId;
        private Long bookVersion;
        private String reviewId;
        @Schema(description = "笔记作者ID")
        private Long userVid;
        @Schema(description = "笔记")
        private String content;
        @Schema(description = "摘要")
        private String abstracted;
        @Schema(description = "0公开笔记")
        private Integer isPrivate;
        private Integer type;
        @Schema(description = "范围")
        private String range;
        @Schema(description = "章节id")
        private Long chapterIdx;
        private Long chapterUid;
        @Schema(description = "章节标题")
        private String chapterTitle;
        @Schema(description = "创建时间")
        private Long createTime;
        public void populateSpecFromJson(JsonNode jsonNode) {
            if (jsonNode == null) return;

            // 提取字段值（带空值安全处理）
            this.bookId = jsonNode.path("bookId").asText(null);
            this.bookVersion = jsonNode.path("bookVersion").asLong(0);
            this.reviewId = jsonNode.path("reviewId").asText(null);
            this.userVid = jsonNode.path("userVid").asLong(0);
            this.content = jsonNode.path("content").asText("");
            this.abstracted = jsonNode.path("abstracted").asText("");
            this.isPrivate = jsonNode.path("isPrivate").asInt(0);
            this.type = jsonNode.path("type").asInt(0);
            this.range = jsonNode.path("range").asText("");
            this.chapterIdx = jsonNode.path("chapterIdx").asLong(0);
            this.chapterUid = jsonNode.path("chapterUid").asLong(0);
            this.chapterTitle = jsonNode.path("chapterTitle").asText("");
            this.createTime = jsonNode.path("createTime").asLong(0L);
        }
    }
}
