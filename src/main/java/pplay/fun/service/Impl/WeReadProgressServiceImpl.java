package pplay.fun.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pplay.fun.service.WeReadProgressService;
import run.halo.app.extension.ReactiveExtensionClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeReadProgressServiceImpl implements WeReadProgressService {
    private final ReactiveExtensionClient client;
}
