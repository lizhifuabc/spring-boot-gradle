package com.tomato.gradle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * index
 *
 * @author lizhifu
 * @since 2023/8/27
 */
@RestController
@Slf4j
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "hello world";
    }
}
