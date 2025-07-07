package pplay.fun.service.Impl;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import pplay.fun.service.WeReadService;
import pplay.fun.component.WeReadApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeReadServiceImpl implements WeReadService {
    @Resource
    private WeReadApiClient weReadApiClient;
    @Override
    public void synchronizationWeRead(){
        try {
            // 示例：调用 API 方法
            JsonNode bookshelf = weReadApiClient.getBookshelf();
            if (bookshelf != null && bookshelf.has("bookCount")) {
                int bookCount = bookshelf.get("bookCount").asInt();
                log.info("书架书籍数量: {}", bookCount);
            } else {
                log.warn("无法获取书籍数量，bookshelf 为 null 或缺少 'bookCount' 字段");
            }
            JsonNode notebooks = weReadApiClient.getNotebooks();
            // 后续可添加其他 API 调用逻辑
            //获取阅读状态信息
            JsonNode readingInfo = weReadApiClient.getReadingInfo("932426");
            //获取书籍章节信息
            JsonNode chapterInfos = weReadApiClient.getChapterInfos("932426");
            //书籍详情
            JsonNode bookInfo = weReadApiClient.getBookInfo("34631845");
            JsonNode webBookInfo = weReadApiClient.getWebBookInfo("34631845");
            //划线
            JsonNode bookmarks = weReadApiClient.getBookmarks("34631845");
            //笔记
            JsonNode personalReviews = weReadApiClient.getPersonalReviews("34631845");

            System.out.println(111);
        } catch (Exception e) {
            log.error("同步微信读书数据失败", e);
        }
    }
    public void addWeRead(String cookie){

    }
}
