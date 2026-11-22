package dev.guilhermesebastiao.authenticationapi.adapter.in.dto.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingQuery {

    @Min(value = 1)
    private int page = 1;

    @Min(value = 1)
    @Max(value = 50)
    private int size = 20;

    public static PagingQuery of(int page, int size) {
        return new PagingQuery(page, size);
    }
}