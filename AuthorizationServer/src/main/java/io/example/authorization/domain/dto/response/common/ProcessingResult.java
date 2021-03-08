package io.example.authorization.domain.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
public class ProcessingResult<T> {

    private boolean success = true;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    public ProcessingResult(T data) {
        if(data instanceof Error){
            this.success = false;
            this.error = (Error) data;
        }else{
            this.data = data;
        }
    }
}
