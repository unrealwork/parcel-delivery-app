package parcel.delivery.app.delivery.controller.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Courier API",
        description = "Contains endpoints related to couriers in delivery service"
)
public class CouriersController {
    private final CouriersService couriersService;

    @Operation(summary = "List of couriers ",
            description = """
                    Retrieves list of couriers with their statuses availability statuses
                    """
    )
    @GetMapping("/couriers")
    @AuthPolicy(RolePrivilege.VIEW_COURIERS)
    public List<CourierDto> couriers() {
        return couriersService.list();
    }
}
