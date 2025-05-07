package com.mervesaruhan.librarymanagementsystem.restResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mervesaruhan.librarymanagementsystem.model.exception.ErrorMessage;
import com.mervesaruhan.librarymanagementsystem.model.exception.BaseErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse <T> {
    private T data;
    private boolean isSuccess;
    private String message;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime responseTime;

    private BaseErrorMessage errorMessage;
    private HttpStatus httpStatus;

    public RestResponse(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        this.responseTime = LocalDateTime.now();
    }

    // constructor for error case
    public RestResponse(BaseErrorMessage errorMessage, HttpStatus httpStatus) {
        this.isSuccess = false;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
    public static <T> RestResponse<T> of(T t){
        return new RestResponse<T>(t, true);
    }

    public static <T> RestResponse<T> errorAuth(BaseErrorMessage message, HttpStatus status){
        return new RestResponse<T>(message, status);
    }

    public static <T> RestResponse<T> error(T t,String message) {
        RestResponse<T> response = new RestResponse<>(t, false);
        response.setMessage(message);
        return response;
    }

//    private ErrorMessage ex;
//    RestResponse<String> errorResponse = RestResponse.error(null, ex.getMessage());


    public static <T> RestResponse<T> empty(){
        return new RestResponse<T>(null, true);
    }

    public static <T> RestResponse<T> message(String message) {
        RestResponse<T> response = new RestResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setResponseTime(LocalDateTime.now());
        return response;
    }
}
