package io.example.authorization.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author : choi-ys
 * @date : 2021-02-25 오후 11:33
 * @Content : 유효성 검사 진행 과정에서 발생한 Errors객체의 응답 처리를 위한 ErrorsSerializer
 */
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    private final String OBJECT_NAME = "objectName";
    private final String FIELD = "field";
    private final String CODE = "code";
    private final String DEFAULT_MESSAGE = "defaultMessage";
    private final String REJECTED_VALUE = "rejectedValue";
    private final String ARGUMENT = "argument";

    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName("errors");
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().stream().forEach(error -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(OBJECT_NAME, error.getObjectName());
                jsonGenerator.writeStringField(FIELD, error.getField());
                jsonGenerator.writeStringField(CODE, error.getCode());
                jsonGenerator.writeStringField(DEFAULT_MESSAGE, error.getDefaultMessage());
                Object rejectedValue = error.getRejectedValue();
                if(rejectedValue != null) {
                    jsonGenerator.writeStringField(REJECTED_VALUE, rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        errors.getGlobalErrors().stream().forEach(error -> {
            try {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(OBJECT_NAME, error.getObjectName());
                jsonGenerator.writeStringField(CODE, error.getCode());
                jsonGenerator.writeStringField(DEFAULT_MESSAGE, error.getDefaultMessage());

                Object[] arguments = error.getArguments();
                if(arguments != null){
                    jsonGenerator.writeStringField(ARGUMENT, Arrays.toString(error.getArguments()));
                }
                jsonGenerator.writeEndObject();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        jsonGenerator.writeEndArray();
    }
}