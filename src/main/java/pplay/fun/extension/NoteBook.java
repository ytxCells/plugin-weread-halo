package pplay.fun.extension;

import lombok.Data;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@GVK(group = "weread.pplay.fun",
    version = "v1alpha1",
    kind = "NoteBook",
    plural = "notebooks",
    singular = "notebook"
)
public class NoteBook extends AbstractExtension {
    private NoteBookSpec spec;
    @Data
    public static class NoteBookSpec {
        private String bookId;
        private Long sort;
    }
}
