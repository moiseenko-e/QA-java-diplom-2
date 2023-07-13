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
        ValidatableResponse response = steps.createOrder();
        Methods.createOrderCorrect(response, code, status);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией и ингредиентами")
    public void createOrderCorrectTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderUser(accessToken);
        Methods.createOrderCorrect(response, code, status);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией, без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createWithoutIngredients(accessToken);

    }

    @Test
    @DisplayName("Создание заказа: с неверным хешем ингредиентов")
    public void createOrderWithIncorrectIngredientsTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createIncorrectIngredients(accessToken);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
