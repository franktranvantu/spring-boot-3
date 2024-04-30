package com.franktranvantu.springboot3.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static com.franktranvantu.springboot3.exception.ServiceStatusCode.SUCCESS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> {
    int code;
    String message;
    T result;

    public static <T> ServiceResponse<T> ok(T result) {
        return ServiceResponse.<T>builder()
                .code(SUCCESS.getCode())
                .result(result)
                .build();
    }

    public static ServiceResponse ok() {
        return ServiceResponse.<Void>builder()
                .code(SUCCESS.getCode())
                .build();
    }
}
