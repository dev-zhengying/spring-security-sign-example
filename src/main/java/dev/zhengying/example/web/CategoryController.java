package dev.zhengying.example.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CategoryController
 *
 * @author <a href="mailto:pm@zhengying.dev">郑颖</a>
 * @since 2024/9/30
 */
@Slf4j
@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    @GetMapping
    public Object get(@AuthenticationPrincipal Object principal) {
        log.info("BrandController#get() called with: principal = [{}]", principal);
        return "ok";
    }

    @PostMapping
    public Object post(@AuthenticationPrincipal Object principal) {
        log.info("BrandController#post() called with: principal = [{}]", principal);
        return "ok";
    }
}
