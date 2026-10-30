package com.github.birulazena.hotel_app_test_task.unit.hotel.entity;

import com.github.birulazena.hotel_app_test_task.hotel.entity.Contacts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Unit tests for Contacts")
class ContactsTest {

    @Test
    @DisplayName("Successful creation with whitespace trimming")
    void create_Success() {
        Contacts contacts = Contacts.builder()
                .phone("  +375 17 309-80-00  ")
                .email("  doubletreeminsk.info@hilton.com  ")
                .build();

        assertThat(contacts.getPhone()).isEqualTo("+375 17 309-80-00");
        assertThat(contacts.getEmail()).isEqualTo("doubletreeminsk.info@hilton.com");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    @DisplayName("Exception when phone is blank")
    void create_BlankPhone_ThrowsException(String invalidPhone) {
        assertThatThrownBy(() -> Contacts.builder()
                .phone(invalidPhone)
                .email("valid@email.com")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Phone must not be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalid-email", "test@", "@domain.com", "plainaddress", "   "})
    @DisplayName("Exception when email format is invalid")
    void create_InvalidEmail_ThrowsException(String invalidEmail) {
        assertThatThrownBy(() -> Contacts.builder()
                .phone("+375 17 309-80-00")
                .email(invalidEmail)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format");
    }

    @Test
    @DisplayName("Correct behavior of equals and hashCode")
    void equalsAndHashCode() {
        Contacts c1 = Contacts.builder().phone("+375 17 111").email("info@test.com").build();
        Contacts c2 = Contacts.builder().phone("+375 17 111").email("info@test.com").build();
        Contacts c3 = Contacts.builder().phone("+375 17 222").email("info@test.com").build();

        assertThat(c1)
                .isEqualTo(c2)
                .hasSameHashCodeAs(c2)
                .isNotEqualTo(c3);
    }
}
