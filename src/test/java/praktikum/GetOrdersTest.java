package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersTest {
    private User user;
    private Steps steps;
    private Methods methods;
    private String accessToken;
    private Login login;

    @Before
    public void setUser() {
        user = GenerateUser.random();
        steps = new Steps();
        methods = new Methods();
        login = new Login(user);
    }

    @Test
    @DisplayName("Тест получения заказов конкретного пользователя без авторизации")
    public void getUserNoAuthOrdersTest() {
        ValidatableResponse response = steps.getUserNoAuthOrders();
        methods.createOrderNoAuth(response);
    }

    @Test
    @DisplayName("Тест получения заказов конкретного пользователя с авторизацией")
    public void getUserAuthOrdersTest() {
        steps.createUser(user);
        ValidatableResponse response = steps.loginUser(login);
        accessToken = response.extract().path("accessToken").toString();
        steps.createOrderUser(accessToken);
        steps.getUserAuthOrders(accessToken);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
