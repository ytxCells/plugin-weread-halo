package pplay.fun;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.theme.TemplateNameResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Component
public class WeReadRouter {
    private final TemplateNameResolver templateNameResolver;
    // @Bean
    // RouterFunction<ServerResponse> wereadRouterFunction() {
    //     return route(GET("/weread"), this::renderWeReadPage);
    // }
    //
    // Mono<ServerResponse> renderWeReadPage(ServerRequest request) {
    //     // 或许你需要准备你需要提供给模板的默认数据，非必须
    //     var model = new HashMap<String, Object>();
    //     model.put("weread", List.of());
    //     return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "weread")
    //         .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    // }
    @Bean
    RouterFunction<ServerResponse> wereadRouterFunction() {
        return route(GET("/weread"), this::renderWeReadPage)
            .andRoute(GET("/weread/{bookId}"), this::renderBookDetailPage);
    }

    // 书籍列表页面
    Mono<ServerResponse> renderWeReadPage(ServerRequest request) {
        // 模拟书籍数据
        List<Map<String, Object>> books = List.of(
            createBook("3300096645", "三体", "刘慈欣", "https://example.com/covers/santi.jpg"),
            createBook("3300096646", "活着", "余华", "https://example.com/covers/huozhe.jpg")
        );

        var model = new HashMap<String, Object>();
        model.put("title", "阅读");
        model.put("books", books);

        return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "weread")
            .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    }

    // 书籍详情页面
    Mono<ServerResponse> renderBookDetailPage(ServerRequest request) {
        String bookId = request.pathVariable("bookId");

        // 模拟书籍详情数据
        Map<String, Object> bookDetail = Map.of(
            "bookId", bookId,
            "title", "三体",
            "author", "刘慈欣",
            "cover", "https://example.com/covers/santi.jpg",
            "progress", 65,
            "readingTime", "15小时20分钟",
            "reviews", List.of(
                Map.of("content", "黑暗森林法则令人震撼", "chapter", "第15章"),
                Map.of("content", "宇宙社会学设定新颖", "chapter", "第8章")
            )
        );

        var model = new HashMap<String, Object>();
        model.put("title", "书籍详情");
        model.put("book", bookDetail);

        return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "book-detail")
            .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    }

    private Map<String, Object> createBook(String id, String title, String author, String cover) {
        return Map.of(
            "id", id,
            "title", title,
            "author", author,
            "cover", cover,
            "progress", (int)(Math.random() * 100)
        );
    }
}
