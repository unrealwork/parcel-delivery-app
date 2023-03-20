package parcel.delivery.app.delivery.controller.api;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import parcel.delivery.app.common.annotations.AuthPolicy;
import parcel.delivery.app.common.security.core.RolePrivilege;
import parcel.delivery.app.delivery.dto.CourierDto;
import parcel.delivery.app.delivery.service.CouriersService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouriersController {
    private final CouriersService couriersService;

    @GetMapping("/couriers")
    @AuthPolicy(RolePrivilege.VIEW_COURIERS)
    public List<CourierDto> couriers() {
        return couriersService.list();
    }
}
