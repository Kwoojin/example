package studio.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResultDto<T> {
    @JsonIgnore
    private int count;
    private T content;

    @Builder
    public ResultDto(T content) {
        this.content = content;
    }
}
