package com.example.scaffold.module.user;

import com.example.scaffold.common.api.PageResponse;
import com.example.scaffold.common.api.ResultCode;
import com.example.scaffold.common.exception.BizException;
import com.example.scaffold.common.exception.ResourceNotFoundException;
import com.example.scaffold.module.user.dto.UserCreateRequest;
import com.example.scaffold.module.user.dto.UserResponse;
import com.example.scaffold.module.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BizException(ResultCode.CONFLICT, "email already exists: " + email);
        }
        Instant now = Instant.now();
        User user = new User(request.name().trim(), email);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return UserResponse.from(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> list(int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("id").descending());
        Page<User> result = userRepository.findAll(pageable);
        return new PageResponse<>(
                result.getContent().stream().map(UserResponse::from).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = findOrThrow(id);
        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name().trim());
        }
        if (request.email() != null && !request.email().isBlank()) {
            String email = request.email().trim().toLowerCase();
            if (userRepository.existsByEmailAndIdNot(email, id)) {
                throw new BizException(ResultCode.CONFLICT, "email already exists: " + email);
            }
            user.setEmail(email);
        }
        user.setUpdatedAt(Instant.now());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = findOrThrow(id);
        userRepository.delete(user);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", id));
    }
}
