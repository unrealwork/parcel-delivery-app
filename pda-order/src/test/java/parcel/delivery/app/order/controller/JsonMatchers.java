package parcel.delivery.app.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Component
@RequiredArgsConstructor
public class JsonMatchers {
    private final ObjectMapper objectMapper;


    public <T> ResultMatcher isJsonObject(T obj, Class<T> clz) {
        return new JsonObjectResultMatcher<T>(obj, content -> objectMapper.readValue(content, clz));
    }


    public <T> ResultMatcher isJsonArray(List<T> list, TypeReference<List<T>> typeReference) {
        return new JsonObjectResultMatcher<>(list, content -> objectMapper.readValue(content, typeReference));
    }


    @RequiredArgsConstructor
    private static class JsonObjectResultMatcher<T> implements ResultMatcher {
        private final T expected;
        private final JsonStringDeserializer<T> deserializer;

        @Override
        public void match(MvcResult result) throws UnsupportedEncodingException {
            try {

                String content = result.getResponse()
                        .getContentAsString();
                if (!StringUtils.hasText(content)) {
                    Assertions.fail("Response should not be blank");
                }
                T obj = deserializer.deserialize(content);
                assertThat(expected, equalTo(obj));
            } catch (JsonProcessingException e) {
                Assertions.fail("Invalid JSON: " + e.getMessage());
            }
        }
    }


    @FunctionalInterface
    private interface JsonStringDeserializer<T> {
        T deserialize(String content) throws JsonProcessingException;
    }

}
