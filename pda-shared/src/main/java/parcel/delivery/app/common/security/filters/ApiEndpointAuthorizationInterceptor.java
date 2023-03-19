package parcel.delivery.app.common.security.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiEndpointAuthorizationInterceptor implements HandlerInterceptor {
    private final AuthenticationFacade authenticationFacade;
    private final Map<HandlerMethod, Optional<RolePrivilege>> handlerPrivilges = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Optional<RolePrivilege> rolePrivilege = handlerPrivilges.computeIfAbsent(handlerMethod, this::privilegeForHandler);
            if (rolePrivilege.isPresent()) {
                Collection<RolePrivilege> authPrivileges = authenticationFacade.privileges();
                RolePrivilege requiredPrivilege = rolePrivilege.get();
                if (!authPrivileges.contains(requiredPrivilege)) {
                    throw new AccessDeniedException("Access denied the API endpoint without "
                            + requiredPrivilege + " privilege");
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    Optional<RolePrivilege> privilegeForHandler(HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(AuthPolicy.class)) {
            AuthPolicy annotation = method.getAnnotation(AuthPolicy.class);
            return Optional.ofNullable(annotation.value());
        }
        return Optional.empty();
    }


}
