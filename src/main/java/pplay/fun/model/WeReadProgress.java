package pplay.fun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "progresses",
    plural = "progress",
    singular = "progresses"
)
public class WeReadProgress extends AbstractExtension {
    private WeReadProgressSpec spec;
    @Data
    public static class WeReadProgressSpec {
        private String BookId;
        private Book book;
        private Integer canFreeRead;
        private Long timestamp;
    }
    @Data
    public static class Book{
        private String appId;
        private Long bookVersion;
        private String reviewId;
        private Integer chapterUid;
        private Integer chapterOffset;
        private Integer chapterIdx;
        private Long updateTime;
        private Long synckey;
        @Schema(description = "总结")
        private String summary;
        private Long repairOffsetTime;
        @Schema(description = "阅读时长，单位秒")
        private Long readingTime;
        @Schema(description = "阅读进度，百分比")
        private Integer progress;
        private Integer isStartReading;
        private Long ttsTime;
        private Long startReadingTime;
        private String installId;
        private Long recordReadingTime;

    }
}
