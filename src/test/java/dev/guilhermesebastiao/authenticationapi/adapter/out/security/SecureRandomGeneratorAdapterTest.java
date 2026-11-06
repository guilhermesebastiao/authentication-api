package dev.guilhermesebastiao.authenticationapi.adapter.out.security;

import dev.guilhermesebastiao.authenticationapi.adapter.out.security.adapter.SecureRandomGeneratorAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SecureRandomGeneratorAdapterTest {

    @Mock
    private SecureRandom secureRandom;

    @InjectMocks
    private SecureRandomGeneratorAdapter adapter;

    @Test
    @DisplayName("Should generate a value using the configured character set")
    void givenSizeAndRandomIndexes_whenGenerate_thenReturnExpectedValue() {
        given(secureRandom.nextInt(62)).willReturn(0, 26, 52, 51, 61);

        String result = adapter.generate(5);

        assertEquals("aA0Z9", result);
        then(secureRandom).should(times(5)).nextInt(62);
    }

    @Test
    @DisplayName("Should return an empty value without using SecureRandom for zero size")
    void givenZeroSize_whenGenerate_thenReturnEmptyWithoutGeneratingIndexes() {
        String result = adapter.generate(0);

        assertEquals("", result);
        then(secureRandom).should(never()).nextInt(anyInt());
    }
}
