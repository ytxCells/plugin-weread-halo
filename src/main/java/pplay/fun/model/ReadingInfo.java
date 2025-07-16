package pplay.fun.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "ReadingInfo",
    plural = "readinginfos",
    singular = "readinginfo"
)
@Data
public class ReadingInfo extends AbstractExtension {
    private ReadingInfoSpec spec;

    public void populateSpecFromJson(JsonNode progressItem) {
        if (this.spec == null){
            this.spec = new ReadingInfoSpec();
        }
        this.spec.populateSpecFromJson(progressItem);
    }

    @Data
    public static class ReadingInfoSpec{
        private String appid;
        private String bookId;
        private Long bookVersion;
        @Schema(description = "阅读时长，单位秒")
        private Long readingTime;
        @Schema(description = "阅读进度，百分比")
        private Integer progress;
        @Schema(description = "1 开始阅读")
        private Integer isStartReading;
        @Schema(description = "开始阅读时间")
        private Long startReadingTime;
        @Schema(description = "完成时间")
        private Long finishTime;
        public void populateSpecFromJson(JsonNode progressNode) {
            if (progressNode == null) return;
            // 提取字段值（带空值安全处理）
            this.appid = progressNode.path("appid").asText(null);
            this.bookId = progressNode.path("bookId").asText(null);
            this.bookVersion = progressNode.path("bookVersion").asLong(0);
            this.readingTime = progressNode.path("readingTime").asLong(0L);
            this.progress = progressNode.path("progress").asInt(0);
            this.isStartReading = progressNode.path("isStartReading").asInt(0);
            this.startReadingTime = progressNode.path("startReadingTime").asLong(0L);
            this.finishTime = progressNode.path("finishTime").asLong(0L);
        }
    }
}
