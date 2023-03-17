package parcel.delivery.app.order.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import parcel.delivery.app.common.util.WebUtil;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Component
public class ApiRestClient {
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    public ApiRestClient(ObjectMapper mapper, WebApplicationContext wac) {
        this.mapper = mapper;
        this.mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    public ResultActions get(String url) throws Exception {
        return request(HttpMethod.GET, null, null, url);
    }

    public ResultActions get(String url, String accessToken) throws Exception {
        return request(HttpMethod.GET, null, accessToken, url);
    }

    public ResultActions post(String url, Object object) throws Exception {
        return post(url, null, object);
    }

    public ResultActions post(String url, String accessToken, Object object) throws Exception {
        return request(HttpMethod.POST, object, accessToken, url);
    }

    public ResultActions request(HttpMethod httpMethod, Object req, String urlTemplate, Object... args) throws Exception {
        return request(httpMethod, req, null, urlTemplate, args);
    }

    public ResultActions put(Object req, String urlTemplate, Object... args) throws Exception {
        return request(HttpMethod.PUT, req, null, urlTemplate, args);
    }

    public ResultActions put(String urlTemplate, Object... args) throws Exception {
        return put(null, urlTemplate, args);
    }

    public ResultActions putJson(String json, String urlTemplate, Object... args) throws Exception {
        return requestJson(HttpMethod.PUT, json, null, urlTemplate, args);
    }

    public ResultActions request(HttpMethod httpMethod, Object req, String accessToken, String urlTemplate, Object... args) throws Exception {
        return requestJson(httpMethod, mapper.writeValueAsString(req), accessToken, urlTemplate, args);
    }

    public ResultActions requestJson(HttpMethod httpMethod, String json, String accessToken, String urlTemplate, Object... args) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.request(httpMethod, urlTemplate, args);
        if (accessToken != null) {
            request.header(HttpHeaders.AUTHORIZATION, WebUtil.bearerHeader(accessToken));
        }
        if (json != null) {
            request.content(json)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8);
        }
        return mvc.perform(request);
    }

    public ResultActions postJson(String jsonReq, String urlTemplate, Object... args) throws Exception {
        return requestJson(HttpMethod.POST, jsonReq, null, urlTemplate, args);
    }
}
