package pplay.fun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pplay.fun.extension.Book;
import pplay.fun.extension.BookInfo;
import pplay.fun.extension.Bookmark;
import pplay.fun.extension.ChapterInfo;
import pplay.fun.extension.NoteBook;
import pplay.fun.extension.Review;
import pplay.fun.extension.WeReadConfig;
import pplay.fun.extension.ReadingInfo;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;

/**
 * <p>Plugin main class to manage the lifecycle of the plugin.</p>
 * <p>This class must be public and have a public constructor.</p>
 * <p>Only one main class extending {@link BasePlugin} is allowed per plugin.</p>
 *
 * @author ytxCells
 * @since 1.0.0
 */
@Component
public class WeReadHaloPlugin extends BasePlugin {

    @Autowired
    private SchemeManager schemeManager;

    public WeReadHaloPlugin(PluginContext pluginContext) {
        super(pluginContext);
    }

    @Override
    public void start() {
        schemeManager.register(Book.class);
        schemeManager.register(BookInfo.class);
        schemeManager.register(Bookmark.class);
        schemeManager.register(ChapterInfo.class);
        schemeManager.register(NoteBook.class);
        schemeManager.register(ReadingInfo.class);
        schemeManager.register(Review.class);
        schemeManager.register(WeReadConfig.class);
        System.out.println("插件启动成功！");
    }

    @Override
    public void stop() {
        System.out.println("插件停止！");
    }
}
