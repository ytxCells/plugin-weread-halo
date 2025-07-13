package pplay.fun.service.Impl;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import pplay.fun.service.BookshelfService;
import pplay.fun.service.BookmarkService;
import pplay.fun.service.ReviewService;
import pplay.fun.service.WeReadProgressService;
import pplay.fun.service.WeReadService;
import pplay.fun.component.WeReadApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WeReadServiceImpl implements WeReadService {
    @Resource
    private WeReadApiClient weReadApiClient;
    @Resource
    private BookmarkService bookmarkService;
    @Resource
    private BookshelfService bookShelfService;
    @Resource
    private ReviewService reviewService;
    @Resource
    private WeReadProgressService progressService;
    @Override
    public Mono<Void> synchronizationWeRead() {
        return ReactiveSecurityContextHolder.getContext() // 替换为官方API
            .switchIfEmpty(Mono.error(new AuthenticationCredentialsNotFoundException("安全上下文为空")))
            .flatMap(securityContext -> {
                Authentication auth = securityContext.getAuthentication();
                if (auth == null || !auth.isAuthenticated()) {
                    return Mono.error(new AuthenticationCredentialsNotFoundException("用户未认证"));
                }
                String username = auth.getName();
                log.info("开始同步用户数据: {}", username);

                return Mono.fromCallable(weReadApiClient::getBookshelf)
                    .flatMap(bookShelfService::save)
                    .then(Mono.fromCallable(weReadApiClient::getNotebooks))
                    .then();
            });
    }



//     @Override
//     public Mono<Void> synchronizationWeRead(){
//         return Mono.fromRunnable(() -> {
//             try {
//                 JsonNode bookshelf = weReadApiClient.getBookshelf();
//                 bookShelfService.save(bookshelf);
//                 JsonNode notebooks = weReadApiClient.getNotebooks();
//
//                 // 示例调用（后续需替换硬编码ID）
//                 String sampleBookId = "3300096645";
//                 weReadApiClient.getReadingInfo(sampleBookId);
//                 weReadApiClient.getChapterInfos(sampleBookId);
//                 weReadApiClient.getBookInfo(sampleBookId);
//                 weReadApiClient.getWebBookInfo(sampleBookId);
//                 weReadApiClient.getBookmarks(sampleBookId);
//                 weReadApiClient.getPersonalReviews(sampleBookId);
//             } catch (Exception e) {
//                 log.error("同步微信读书数据失败", e);
//                 throw new RuntimeException(e); // 转换为非受检异常
//             }
//         }).then();
//     }
 }
