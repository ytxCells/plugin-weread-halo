package pplay.fun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

/**
 * 章节
 */
@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "ChapterInfo",
    plural = "chapterinfos",
    singular = "chapterinfo"
)
public class ChapterInfo extends AbstractExtension {
    //metadata.name 为bookId
    private ChapterInfoSpec spec;
    @Data
    public static class ChapterInfoSpec {
        @Schema(description = "章节id")
        private Long chapterUid;
        private Long chapterIdx;
        private Long updateTime;
        private String title;
        private Integer wordCount;
        @Schema(description = "层级")
        private Integer level;
    }

}
