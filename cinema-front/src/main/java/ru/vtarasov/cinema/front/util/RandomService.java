package ru.vtarasov.cinema.front.util;

public interface RandomService {
    boolean nextBoolean();

    int nextInt(int bound);
    int nextInt(int origin, int bound);
}
