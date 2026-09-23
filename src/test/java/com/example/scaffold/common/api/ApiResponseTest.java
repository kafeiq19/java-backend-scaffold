package com.example.scaffold.common.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void okWrapsData() {
        ApiResponse<String> response = ApiResponse.ok("hello");
        assertThat(response.code()).isZero();
        assertThat(response.message()).isEqualTo("success");
        assertThat(response.data()).isEqualTo("hello");
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void errorUsesResultCode() {
        ApiResponse<Void> response = ApiResponse.error(ResultCode.NOT_FOUND, "user not found: 1");
        assertThat(response.code()).isEqualTo(404);
        assertThat(response.message()).isEqualTo("user not found: 1");
        assertThat(response.data()).isNull();
    }
}
