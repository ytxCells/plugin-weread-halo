package pplay.fun.util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.User;
import run.halo.app.core.user.service.UserService;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.ReactiveExtensionClient;
import java.util.Optional;

@Component
public class UserUtils {

    public String getUserNameSync(ReactiveExtensionClient client) {
        return getContextUser(client)
            .map(User::getMetadata)
            .map(metadata -> metadata.getName())
            .blockOptional()
            .orElseThrow(() -> new RuntimeException("无法获取用户名"));
    }
    public Mono<User> getContextUser(ReactiveExtensionClient client) {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(ctx -> {
                var name = ctx.getAuthentication().getName();
                return client.fetch(User.class, name);
            });
    }
}
