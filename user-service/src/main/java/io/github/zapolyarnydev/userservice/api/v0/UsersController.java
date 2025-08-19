package io.github.zapolyarnydev.userservice.api.v0;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.userservice.dto.SelfInfoResponseDTO;
import io.github.zapolyarnydev.userservice.dto.UpdateUserRequestDTO;
import io.github.zapolyarnydev.userservice.dto.UserInfoResponseDTO;
import io.github.zapolyarnydev.userservice.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> updateUser(@Valid @RequestBody UpdateUserRequestDTO updateUserRequest,
                                                  @RequestHeader("Calmify-User-Email") String email) {
        var user = userService.findUserByEmail(email);
        userService.updateUser(user, updateUserRequest);
        var response = ApiResponse.success("User updated successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SelfInfoResponseDTO>> getUser(@RequestHeader("Calmify-User-Email") String email) {
        var user = userService.findUserByEmail(email);

        SelfInfoResponseDTO dto = userMapper.fromEntityToSelfInfo(user);
        var response = ApiResponse.success("User info received", dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ApiResponse<UserInfoResponseDTO>> getUserByHandle(@PathVariable String handle) {

        var response = ApiResponse.success(
                "User info received",
                userService.getUserInfoResponse(handle)
        );

        return ResponseEntity.ok(response);
    }
}
