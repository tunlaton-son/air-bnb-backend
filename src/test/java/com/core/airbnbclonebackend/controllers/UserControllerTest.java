package com.core.airbnbclonebackend.controllers;

import com.core.airbnbclonebackend.config.security.SecurityConfiguration;
import com.core.airbnbclonebackend.controllers.v1.UserController;
import com.core.airbnbclonebackend.dto.request.user.UserRequest;
import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.repository.UserRepository;
import com.core.airbnbclonebackend.service.JwtService;
import com.core.airbnbclonebackend.service.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@Import({
        SecurityConfiguration.class,
        BCryptPasswordEncoder.class
})
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean private JwtService jwtService;

    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssuredMockMvc.mockMvc(mvc);
    }


    @Test
    public void should_create_user_success() throws Exception {
        String email = "john@gmail.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "12345678";
        User user = new User(email, name, phone,  password);

        when(userService.createUser(any())).thenReturn(user);
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("user.phone", equalTo(phone));

        verify(userService).createUser(any());
    }

    @Test
    public void should_show_error_message_for_invalid_email() throws Exception {
        String email = "johnxjacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                   .body("errors.email[0]", equalTo("Invalid Email Format"));
    }

    @Test
    public void should_show_error_message_for_email_is_duplicated() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "12345678";

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(new User(email, name, phone,  password)));
        when(userRepository.findByPhone(eq(phone))).thenReturn(Optional.empty());

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.email[0]", equalTo("This Email is taken"));
    }

    @Test
    public void should_show_error_message_for_email_length_more_than_255() throws Exception {
        String email = "johnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohnjohn@gmail.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.email[0]", equalTo("Invalid Email Format"));
    }

    @Test
    public void should_show_error_message_for_name_is_blank() throws Exception {
        String email = "john@gmail.com";
        String name = "";
        String phone = "0822133875";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.name[0]", equalTo("Name is required field"));
    }

    @Test
    public void should_show_error_message_for_name_length_more_than_255() throws Exception {
        String email = "john@gmail.com";
        String name = "john doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doejohn doe";
        String phone = "0822133875";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.name[0]", equalTo("Name must be less than or equal to 255 characters"));
    }

    @Test
    public void should_show_error_message_for_phone_digit_not_equal_to_10() {
        String email = "john@gmail.com";
        String name = "john doe";
        String phone = "082213387";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.phone[0]", equalTo("Phone number must be 10 digit"));
    }

    @Test
    public void should_show_error_message_for_phone_is_duplicated() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "12345678";

        when(userRepository.findByPhone(eq(phone))).thenReturn(Optional.of(new User(email, name, phone, password)));
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.phone[0]", equalTo("This Phone number is taken"));
    }

    @Test
    public void should_show_error_message_for_phone_is_blank() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "";
        String password = "12345678";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.phone[0]", equalTo("Phone number must be 10 digit"));
    }

    @Test
    public void should_show_error_message_for_password_is_blank() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.password[0]", equalTo("Password must be more than or equal to 8 characters"));
    }

    @Test
    public void should_show_error_message_for_password_length_more_than_255() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.password[0]", equalTo("Password must be less than or equal to 255 characters"));
    }

    @Test
    public void should_show_error_message_for_password_less_than_8_characters() throws Exception {
        String email = "johnx@jacob.com";
        String name = "john doe";
        String phone = "0822133875";
        String password = "1234";

        Map<String, Object> param = prepareRegisterParameter(email, name, phone, password);

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.password[0]", equalTo("Password must be more than or equal to 8 characters"));
    }

    @Test
    public void should_fail_login_with_wrong_email_format() throws Exception {
        String email = "johnxjacob.com";
        String password = "12345678";

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", password);
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.email[0]", equalTo("Invalid Email Format"));
    }

    @Test
    public void should_fail_login_for_blank_email() throws Exception {
        String email = "";
        String password = "12345678";

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", password);
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.email[0]", equalTo("Email is required field"));
    }

    @Test
    public void should_fail_login_for_email_does_not_exist() throws Exception {
        String email = "johnx@jacob.com";
        String password = "12345678";

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", password);
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("message", equalTo("Invalid Email or Password"));
    }

    @Test
    public void should_fail_login_for_blank_password() throws Exception {
        String email = "johnx@jacob.com";
        String password = "";

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", password);
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("errors.password[0]", equalTo("Password is required field"));
    }

    @Test
    public void should_fail_login_with_wrong_password() throws Exception {
        String email = "john@jacob.com";
        String password = "123";

        User user = new User(email, "", "", passwordEncoder.encode(password));
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", "123123");
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .prettyPeek()
                .then()
                .statusCode(422)
                .body("message", equalTo("Invalid Email or Password"));
    }

    @Test
    public void should_login_success() throws Exception {
        String email = "john@jacob.com";
        String password = "12345678";

        User user = new User(email, "","", passwordEncoder.encode(password));
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
        when(jwtService.toToken(any())).thenReturn("12345678");

        Map<String, Object> param =
                new HashMap<String, Object>() {
                    {
                        put("email", email);
                        put("password", password);
                    }
                };

        given()
                .contentType("application/json")
                .body(param)
                .when()
                .post("/api/v1/users/login")
                .then()
                .statusCode(200)
                .body("user.email", equalTo(email))
                .body("user.token", equalTo("12345678"));
        ;
    }

    private HashMap<String, Object> prepareRegisterParameter(
            final String email,
            final String name,
            final String phone,
            final String password
    ){
        return new HashMap<String, Object>() {
            {
                put("email", email);
                put("name", name);
                put("password", password);
                put("phone", phone);
            }
        };
    }
}
