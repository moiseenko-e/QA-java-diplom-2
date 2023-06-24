package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginTest {
    private User user;
    private Steps steps;
    private Login login;
    private Methods methods;
    private String accessToken;
    private int code;
    private boolean status;

    @Before
    public void setUser() {
        user = GenerateUser.random();
        steps = new Steps();
        methods = new Methods();
        login = new Login(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка успешной авторизации")
    public void authLoginTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        methods.createUserCorrect(response, code, status);

    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Проверка авторизации без ввода логина")
    public void authWithoutLoginTest() {
        user.setEmail("");
        ValidatableResponse response = steps.loginUser(login);
        methods.emailOrPasswordIncorrect(response);

    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Проверка авторизации без ввода пароля")
    public void authorizationNoPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = steps.loginUser(login);
        methods.emailOrPasswordIncorrect(response);

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
