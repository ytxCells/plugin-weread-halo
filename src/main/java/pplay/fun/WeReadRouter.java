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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Component
public class WeReadRouter {
    private final TemplateNameResolver templateNameResolver;
    @Bean
    RouterFunction<ServerResponse> wereadRouterFunction() {
        return route(GET("/weread"), this::renderWeReadPage);
    }

    Mono<ServerResponse> renderWeReadPage(ServerRequest request) {
        // 或许你需要准备你需要提供给模板的默认数据，非必须
        var model = new HashMap<String, Object>();
        model.put("weread", List.of());
        return templateNameResolver.resolveTemplateNameOrDefault(request.exchange(), "weread")
            .flatMap(templateName -> ServerResponse.ok().render(templateName, model));
    }
}
