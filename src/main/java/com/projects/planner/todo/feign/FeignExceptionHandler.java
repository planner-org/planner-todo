package com.projects.planner.todo.feign;

import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

@Component
public class FeignExceptionHandler implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        return new ResponseStatusException(HttpStatus.valueOf(response.status()), readMessage(response));

    }

    private String readMessage(Response response) {

        String message = null;

        try(Reader reader = response.body().asReader(Charset.defaultCharset())) {
            message = CharStreams.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

}
