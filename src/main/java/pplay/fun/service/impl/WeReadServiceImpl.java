package pplay.fun.service.impl;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import pplay.fun.service.BookService;
import pplay.fun.service.BookmarkService;
import pplay.fun.service.ReviewService;
import pplay.fun.service.ReadingInfoService;
import pplay.fun.service.WeReadService;
import pplay.fun.component.WeReadApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WeReadServiceImpl implements WeReadService {
    @Resource
    private WeReadApiClient weReadApiClient;
    @Resource
    private BookService bookService;
    @Resource
    private BookmarkService bookmarkService;
    @Resource
    private ReviewService reviewService;
    @Resource
    private ReadingInfoService progressService;
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
                // 步骤1: 获取并保存书架数据和阅读进度
                Mono<Void> saveBooksAndProgress = Mono.fromCallable(weReadApiClient::getBookshelf)
                    .flatMap(this::saveBookAndProgress);

                // 步骤2: 新增 - 获取并保存笔记本数据
                Mono<Void> saveNotebooks = Mono.fromCallable(weReadApiClient::getNotebooks)
                    .flatMap(this::saveNotebooks);
                // 合并两个操作
                return saveBooksAndProgress.then(saveNotebooks).then();
            });
    }

    private Mono<Void> saveNotebooks(JsonNode notebooksNode) {
        // 1. 获取notebooks数组
        JsonNode notebooksArray = notebooksNode.path("books"); // 使用path避免空指针
        Flux<Void> saveNotebooksFlux = Flux.empty();

        if (notebooksArray != null && notebooksArray.isArray()) {
            saveNotebooksFlux = Flux.fromIterable(notebooksArray)
                .flatMap(notebook -> {
                    String bookId = notebook.path("bookId").asText();
                    if (bookId == null || bookId.isEmpty()) {
                        log.warn("笔记本缺少bookId字段: {}", notebook);
                        return Mono.empty();
                    }
                    log.info("开始同步书籍的笔记数据: bookId={}", bookId);
                    // 2. 获取并保存书签（划线）
                    Mono<Void> saveBookmarks = Mono.fromCallable(() -> weReadApiClient.getBookmarks(bookId))
                        .flatMap(bookmarksNode -> {
                            JsonNode bookmarksArray = bookmarksNode.path("updated");
                            Flux<Void> saveBookmarksFlux = Flux.empty();
                            if (bookmarksArray != null && bookmarksArray.isArray()) {
                                saveBookmarksFlux = Flux.fromIterable(bookmarksArray)
                                    .flatMap(bookmark -> bookmarkService.save(bookmark));
                            }
                            return saveBookmarksFlux.then();
                        })
                        .onErrorResume(e -> {
                            log.error("获取书签数据失败: bookId={}", bookId, e);
                            return Mono.empty();
                        });
                    // 3. 获取并保存笔记
                    Mono<Void> saveReviews = Mono.fromCallable(() -> weReadApiClient.getPersonalReviews(bookId))
                        .flatMap(reviewsNode -> {
                            JsonNode reviewsArray = reviewsNode.path("reviews");
                            Flux<Void> saveReviewsFlux = Flux.empty();
                            if (reviewsArray != null && reviewsArray.isArray()) {
                                saveReviewsFlux = Flux.fromIterable(reviewsArray)
                                    .flatMap(review -> reviewService.save(review));
                            }

                            return saveReviewsFlux.then();
                        })
                        .onErrorResume(e -> {
                            log.error("获取笔记数据失败: bookId={}", bookId, e);
                            return Mono.empty();
                        });

                    // 4. 并行执行书签和笔记保存
                    return saveBookmarks.then(saveReviews);
                }, 3); // 限制并发数为3，避免API调用过于频繁
        }

        return saveNotebooksFlux.then();
    }


    private Mono<Void> saveBookAndProgress(JsonNode jsonNode) {
        // 1. 获取books数组并遍历保存
        JsonNode booksNode = jsonNode.get("books");
        Flux<Void> saveBooksFlux = Flux.empty();
        if (booksNode != null && booksNode.isArray()) {
            saveBooksFlux = Flux.fromIterable(booksNode)
                .flatMap(bookNode -> bookService.save(bookNode));
        }
        // 2. 获取bookProgress数组并遍历保存
        JsonNode progressNode = jsonNode.get("bookProgress");
        Flux<Void> saveProgressFlux = Flux.empty();
        if (progressNode != null && progressNode.isArray()) {
            saveProgressFlux = Flux.fromIterable(progressNode)
                .flatMap(progressItem -> progressService.save(progressItem));
        }
        // 3. 合并两个异步流并等待完成
        return Flux.merge(saveBooksFlux, saveProgressFlux).then();
    }


    public Mono<Void> synchronizationWeRead11(){
        return Mono.fromRunnable(() -> {
            try {
                JsonNode bookshelf = weReadApiClient.getBookshelf();
                //bookShelfService.save(bookshelf);
                //有笔记的书籍清单
                JsonNode notebooks = weReadApiClient.getNotebooks();

                // 示例调用（后续需替换硬编码ID）
                String sampleBookId = "3300096645";
                //阅读状态
                JsonNode readingInfo = weReadApiClient.getReadingInfo(sampleBookId);
                //书籍章节信息
                JsonNode chapterInfos = weReadApiClient.getChapterInfos(sampleBookId);
                //书籍详情
                JsonNode bookInfo = weReadApiClient.getBookInfo(sampleBookId);
                JsonNode webBookInfo = weReadApiClient.getWebBookInfo(sampleBookId);
                //划线
                JsonNode bookmarks = weReadApiClient.getBookmarks(sampleBookId);
                //笔记
                JsonNode personalReviews = weReadApiClient.getPersonalReviews(sampleBookId);
                System.out.println("end 结束");
            } catch (Exception e) {
                log.error("同步微信读书数据失败", e);
                throw new RuntimeException(e); // 转换为非受检异常
            }
        }).then();
    }
 }
