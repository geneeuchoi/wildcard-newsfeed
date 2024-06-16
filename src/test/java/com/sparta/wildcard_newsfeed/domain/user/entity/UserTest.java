package com.sparta.wildcard_newsfeed.domain.user.entity;

import com.sparta.wildcard_newsfeed.domain.user.dto.UserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("가입 테스트")
    void test1() {
        // Given
        String usercode = "testUser";
        String password = "password";
        String email = "test@example.com";

        // When
        User user = new User(usercode, password, email);

        // Then
        assertEquals(usercode, user.getUsercode());
        assertEquals(password, user.getPassword());
        assertEquals(usercode, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(UserStatusEnum.UNAUTHORIZED, user.getUserStatus());
        assertNotNull(user.getAuthUserAt());
        assertEquals(UserRoleEnum.USER, user.getUserRoleEnum());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void test2() {
        // Given
        User user = new User("testUser", "password", "test@example.com");

        // When
        user.setUserStatus(UserStatusEnum.DISABLED);

        // Then
        assertEquals(UserStatusEnum.DISABLED, user.getUserStatus());
        assertNotNull(user.getAuthUserAt());
    }


    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    void test3() {
        // Given
        User user = new User("testUser", "password", "test@example.com");
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setName("newName");
        requestDto.setEmail("new@example.com");
        requestDto.setIntroduce("newIntroduce");
        requestDto.setChangePassword("newPWD34##");

        // When
        user.update(requestDto);

        // Then
        assertEquals("newPWD34##", user.getPassword());
        assertEquals("newName", user.getName());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newIntroduce", user.getIntroduce());
    }

    @Test
    @DisplayName("사용자 상태와 상태 변경 시간 업데이트")
    void test4() {
        // Given
        User user = new User("testUser", "password", "test@example.com");

        // When
        user.updateUserStatus();

        // Then
        assertEquals(UserStatusEnum.ENABLED, user.getUserStatus());
        assertNotNull(user.getAuthUserAt());
    }

}
