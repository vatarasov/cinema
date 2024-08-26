package ru.vtarasov.cinema.back.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Slf4JBackLogger implements BackLogger {
    @Override
    public void info(String message, Object... params) {
        log.info(message, params);
    }
}
