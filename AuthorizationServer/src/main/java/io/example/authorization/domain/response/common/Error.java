package io.example.authorization.domain.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
public class Error {

    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detail;
}