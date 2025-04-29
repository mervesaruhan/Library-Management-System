package com.mervesaruhan.librarymanagementsystem.restResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse <T> {
    private T data;
    private boolean isSuccess;
    private String message;
    private LocalDateTime responseTime;

    public RestResponse(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        this.responseTime = LocalDateTime.now();
    }

    public static <T> RestResponse<T> of(T t){
        return new RestResponse<T>(t, true);
    }

//    public static <T> RestResponse<T> error(T t){
//        return new RestResponse<T>(t, false);
//    }

    public static <T> RestResponse<T> error(T t,String message) {
        RestResponse<T> response = new RestResponse<>(t, false);
        response.setMessage(message);
        return response;
    }


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
