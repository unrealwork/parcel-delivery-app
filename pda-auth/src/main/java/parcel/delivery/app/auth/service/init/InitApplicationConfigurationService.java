package parcel.delivery.app.auth.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class InitApplicationConfigurationService implements InitializingBean {
    private final List<Initializer> initializers;

    @Override
    public void afterPropertiesSet() {
        initializers.stream()
                .sorted(Comparator.comparing(Ordered::getOrder))
                .forEach(Initializer::init);
    }
}
