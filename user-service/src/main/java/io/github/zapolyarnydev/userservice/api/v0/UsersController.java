package io.github.zapolyarnydev.userservice.api.v0;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.userservice.dto.UpdateUserRequestDTO;
import io.github.zapolyarnydev.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v0/user")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateUser(@Valid @RequestBody UpdateUserRequestDTO updateUserRequest,
                                                  @RequestHeader("Calmify-User-Email") String email) {
        var user = userService.findUser(email);
        userService.updateUser(user, updateUserRequest);
        var response = ApiResponse.success("User updated successfully", null);
        return ResponseEntity.ok(response);
    }
}
