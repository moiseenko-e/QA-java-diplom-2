package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class UpdateTest {
    private User user;

    private User userUpdate;
    private Steps steps;
    private Login login;
    private Methods methods;
    private String accessToken;

    @Before
    public void setUser() {
        user = GenerateUser.random();
        userUpdate = GenerateUser.random();
        steps = new Steps();
        methods = new Methods();
        login = new Login(user);
    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией")
    @Description("Проверка изменения данных авторизованного пользователя")
    public void updateAuthUserTest() {
        User initialUser = GenerateUser.random();
        User userForUpdate = initialUser.clone();
        userForUpdate.setEmail(RandomStringUtils.randomAlphabetic(10) + "@test.ru");

        String accessToken = steps.createUser(initialUser).extract().header("Authorization");
        steps.updateUser(accessToken, userForUpdate);
        ValidatableResponse updatedUserResponse = steps.getUserData(accessToken);

        updatedUserResponse.body("user.name", equalTo(initialUser.getName())).and().body("user.email", equalTo(userForUpdate.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Изменение данных пользователя: без авторизации")
    @Description("Проверка изменения данных Не авторизованного пользователя")
    public void updateNoAuthUserTest() {
        userUpdate.setEmail(RandomStringUtils.randomAlphabetic(10) + "@test.ru");
        ValidatableResponse response = steps.updateUser("", userUpdate);
        methods.getUpdateNoAuth(response);

    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            steps.deleteUser(accessToken);
        }
    }
}
