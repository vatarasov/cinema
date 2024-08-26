package ru.vtarasov.cinema.front.util;

import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Setter
@Primary
@Service
public class FixedRandomService implements RandomService{
    private boolean nextBoolean;
    private int nextIntBound;
    private int nextIntOriginBound;

    @Override
    public boolean nextBoolean() {
        return nextBoolean;
    }

    @Override
    public int nextInt(int bound) {
        return nextIntBound;
    }

    @Override
    public int nextInt(int origin, int bound) {
        return nextIntOriginBound;
    }
}
