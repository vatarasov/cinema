package ru.vtarasov.cinema.front.util;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class RandomServiceImpl implements RandomService {
    @Override
    public boolean nextBoolean() {
        return new Random().nextBoolean();
    }

    @Override
    public int nextInt(int bound) {
        return new Random().nextInt(bound);
    }

    @Override
    public int nextInt(int origin, int bound) {
        return new Random().nextInt(origin, bound);
    }
}
