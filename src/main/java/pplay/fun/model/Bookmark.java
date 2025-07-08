package pplay.fun.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;
import java.util.List;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "bookmark",
    plural = "bookmarks",
    singular = "Bookmark"
)
public class Bookmark extends AbstractExtension {
    private BookmarkSpec spec;

    @Data
    public static class BookmarkSpec{
        private String bookId;
        @Schema(description = "最新划线的时间戳")
        private Long synckey;
        @Schema(description = "划线")
        private List<updated> list;
    }
    @Data
    public static class updated{
        private String bookId;
        private Integer style;
        private Integer bookVersion;
        private String range;
        private String markText;
        private Integer colorStyle;
        private Integer type;
        private Integer chapterUid;
        private String createTime;
        private String bookmarkId;
    }
}
