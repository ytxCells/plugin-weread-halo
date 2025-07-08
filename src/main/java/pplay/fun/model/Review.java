package pplay.fun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.util.List;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "review",
    plural = "reviews",
    singular = "review"
)
public class Review extends AbstractExtension {
    private ReviewSpec spec;
    @Data
    public static class ReviewSpec{
        private String bookId;
        @Schema(description = "最新笔记的时间戳")
        private Long synckey;
        @Schema(description = "笔记数量统计")
        private Integer totalCount;
        private List<Note> reviews;
    }
    @Data
    public static class Note{
        private String reviewId;
        private String bookId;
        @Schema(description = "笔记")
        private String content;
        private Long bookVersion;
        private String range;
        @Schema(description = "摘要")
        private String abstracted;
        private Integer type;
        private Integer chapterUid;
        private Long userVid;

        private Long createTime;
        private Long isLike;
        private Long isReposted;
        @Schema(description = "章节标题")
        private String chapterTitle;
        private Long chapterIdx;
    }
}
