package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user;
    private Steps steps;
    private Methods methods;
    private String accessToken;
    private int code;
    private boolean status;

    @Before
    public void setUser() {
        user = GenerateUser.random();
        steps = new Steps();
        methods = new Methods();
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Description("Проверка создания нового пользователя")
    public void createUserCorrectTest() {
        ValidatableResponse response = steps.createUser(user);
        accessToken = response.extract().path("accessToken").toString();
        methods.createUserCorrect(response, code, status);

    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Проверка создания пользователя с существующим логином")
    public void createUserDoubleTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.createUser(user);
        methods.createUserDouble(response);

    }
    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка создания пользователя без ввода имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse response = steps.createUser(user);
        methods.createUserWithoutRequiredField(response);
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка создания пользователя без ввода email")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse response = steps.createUser(user);
        methods.createUserWithoutRequiredField(response);
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить одно из обязательных полей")
    @Description("Проверка создания пользователя без ввода пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = steps.createUser(user);
        methods.createUserWithoutRequiredField(response);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
