package studio.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public class ResultDto<T> {
    @JsonIgnore
    private int count;
    private T content;
}
