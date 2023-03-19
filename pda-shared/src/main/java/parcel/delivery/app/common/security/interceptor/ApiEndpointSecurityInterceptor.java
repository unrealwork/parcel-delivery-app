package parcel.delivery.app.common.security.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.stereotype.Component;
import parcel.delivery.app.common.annotations.ApiEndpoint;
import parcel.delivery.app.common.security.AuthenticationFacade;
import parcel.delivery.app.common.security.core.RolePrivilege;

import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@RequiredArgsConstructor
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ApiEndpointSecurityInterceptor implements Ordered {
    private final AuthenticationFacade auth;

    @Pointcut("@annotation(parcel.delivery.app.common.annotations.ApiEndpoint)")
    private void customSecurityAnnotation() {
    }

    @Pointcut("within(parcel.delivery.app.*.controller..*)  && @within(org.springframework.web.bind.annotation.RestController)")
    public void restControllers() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"
    )
    public void mappingAnnotations() {
    }

    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void requestMappingAnnotations() {
    }

    @Around("restControllers() && requestMappingAnnotations()")
    public Object doSomething(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature methodSignature) {
            Method method = methodSignature.getMethod();
            if (method.isAnnotationPresent(ApiEndpoint.class)) {

                ApiEndpoint annotation = method.getAnnotation(ApiEndpoint.class);
                final RolePrivilege requiredPrivilege = annotation.value();
                Collection<RolePrivilege> authPrivileges = auth.privileges();
                if (!authPrivileges.contains(requiredPrivilege)) {
                    throw new AccessDeniedException("Access denied the API endpoint without "
                            + requiredPrivilege + " privilege");
                }
            }
        }
        return pjp.proceed();
    }

    @Override
    public int getOrder() {
        return AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder();
    }
}
