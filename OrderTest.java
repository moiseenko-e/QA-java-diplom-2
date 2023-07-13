package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private Steps steps;
    private User user;
    private Methods methods;
    private Login login;

    private String accessToken;
    private int code;
    private boolean status;

    @Before
    public void setOrder() {
        user = GenerateUser.random();
        steps = new Steps();
        methods = new Methods();
        login = new Login(user);
    }

    @Test
    @DisplayName("Создание заказа: без авторизации")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse response = steps.checkCreateOrder();
        Methods.checkResponseToCreateOrderNoAuthUser(response); // ожидаем 401 , получаем 200 - тест падает - ок
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией и ингредиентами")
    public void createOrderCorrectTest() {
        steps.checkCreateUser(user);
        ValidatableResponse response = steps.checkLoginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.checkCreateOrderUser(accessToken);
        Methods.checkResponseToCreateOrderCorrect(response);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией, без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        steps.checkCreateUser(user);
        ValidatableResponse response = steps.checkLoginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.checkCreateWithoutIngredients(accessToken);

    }

    @Test
    @DisplayName("Создание заказа: с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        steps.checkCreateUser(user);
        ValidatableResponse response = steps.checkLoginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.checkCreateIncorrectIngredients(accessToken);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.checkDeleteUser(accessToken);
        }
    }
}
