package pplay.fun.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import pplay.fun.service.WeReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ApiVersion;

@ApiVersion("v1alpha1")
@RequestMapping("/weRead")
@RestController
@Slf4j
public class ApiController {
    private final WeReadService weReadService;

    public ApiController(WeReadService weReadService) {
        this.weReadService = weReadService;
    }
    @PostMapping("/synchronizationWeRead")
    @PreAuthorize("isAuthenticated()")
    public Mono<Void> synchronizationWeRead() {
        return weReadService.synchronizationWeRead(); // 传递安全上下文
    }
}
