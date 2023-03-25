package parcel.delivery.app.common.swagger.swagger;

import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.core.RolePrivilege;

import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthPolicyCustomiser implements OperationCustomizer {
    private static final String TEMPLATE = "<b>Required privilege:</b> <code>{0}</code><br/><br/>{1}";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(AuthPolicy.class)) {
            AuthPolicy authPolicy = handlerMethod.getMethodAnnotation(AuthPolicy.class);
            if (authPolicy != null && authPolicy.value() != RolePrivilege.BASIC) {
                final String modifiedDescription = MessageFormat.format(TEMPLATE,
                        authPolicy.value(), operation.getDescription());
                operation.setDescription(modifiedDescription);
            }
        }
        return operation;
    }
}
