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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Component
public class ApiRestClient {
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    public ApiRestClient(ObjectMapper mapper, WebApplicationContext wac) {
        this.mapper = mapper;
        this.mvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    public ResultActions get(String url) throws Exception {
        return request(url, HttpMethod.GET, null, null);
    }

    public ResultActions get(String url, String accessToken) throws Exception {
        return request(url, HttpMethod.GET, accessToken, null);
    }

    public ResultActions post(String url, Object object) throws Exception {
        return post(url, null, object);
    }

    public ResultActions post(String url, String accessToken, Object object) throws Exception {
        return request(url, HttpMethod.POST, accessToken, object);
    }

    public ResultActions request(String url, HttpMethod httpMethod, String accessToken, Object req) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.request(httpMethod, url);
        if (accessToken != null) {
            request.header(HttpHeaders.AUTHORIZATION, WebUtil.bearerHeader(accessToken));
        }
        if (req != null) {
            request.contentType(MediaType.APPLICATION_JSON);
            request.content(mapper.writeValueAsString(req));
        }
        return mvc.perform(request);
    }
}
