package com.example.toeicapp.controller;

import com.example.toeicapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthController controller = new AuthController(userRepository, passwordEncoder);

    @Test
    void register_blankUsername_showsErrorAndDoesNotSave() {
        Model model = new ExtendedModelMap();

        String view = controller.register(" ", "pass1234", "pass1234", model);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("error")).isNotNull();
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_passwordTooShort_showsErrorAndDoesNotSave() {
        Model model = new ExtendedModelMap();

        String view = controller.register("Jiro", "abc", "abc", model);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("error")).isNotNull();
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_mismatchedPasswords_showsErrorAndDoesNotSave() {
        Model model = new ExtendedModelMap();

        String view = controller.register("Jiro", "abcd1234", "different", model);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("error")).isNotNull();
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_duplicateUsername_showsErrorAndDoesNotSave() {
        when(userRepository.existsByUsername("Taro")).thenReturn(true);
        Model model = new ExtendedModelMap();

        String view = controller.register("Taro", "abcd1234", "abcd1234", model);

        assertThat(view).isEqualTo("register");
        assertThat(model.getAttribute("error")).isNotNull();
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_validInput_savesEncodedPasswordAndRedirectsToLogin() {
        when(userRepository.existsByUsername("Jiro")).thenReturn(false);
        when(passwordEncoder.encode("abcd1234")).thenReturn("ENCODED");
        Model model = new ExtendedModelMap();

        String view = controller.register("Jiro", "abcd1234", "abcd1234", model);

        assertThat(view).isEqualTo("redirect:/login?registered");
        verify(userRepository).save(argThat(u -> u.getUsername().equals("Jiro") && u.getPassword().equals("ENCODED")));
    }
}
