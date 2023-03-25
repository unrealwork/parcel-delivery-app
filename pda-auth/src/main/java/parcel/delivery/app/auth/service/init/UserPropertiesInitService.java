package parcel.delivery.app.auth.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parcel.delivery.app.auth.config.properties.UserProperty;
import parcel.delivery.app.auth.config.properties.UsersProperties;
import parcel.delivery.app.auth.controller.api.request.SignUpRequest;
import parcel.delivery.app.auth.repository.UserRepository;
import parcel.delivery.app.auth.service.AuthenticationService;
import parcel.delivery.app.common.messaging.EventsEmitter;
import parcel.delivery.app.common.messaging.events.SignedUpEvent;

@Component
@RequiredArgsConstructor
@Slf4j
class UserPropertiesInitService implements Initializer {
    private final UsersProperties usersProperties;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final EventsEmitter<SignedUpEvent> eventsEmitter;


    @Override
    @Transactional
    public void init() {

        log.info("Loading predefined users {}", usersProperties.users());
        if (usersProperties.users() != null) {
            usersProperties.users()
                    .stream()
                    .filter(this::isUserNonExist)
                    .forEach(this::saveUser);
        }
    }


    private void saveUser(UserProperty userProperty) {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(userProperty.getUsername())
                .firstName(userProperty.getFirstName())
                .lastName(userProperty.getLastName())
                .password(userProperty.getPassword())
                .build();
        authenticationService.signUp(signUpRequest, userProperty.getRole());
    }

    private boolean isUserNonExist(UserProperty userProperty) {
        return !userRepository.existsByClientId(userProperty.getUsername());
    }
}
