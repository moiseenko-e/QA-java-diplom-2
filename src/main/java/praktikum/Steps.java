package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class Steps extends BaseDate{
    private static final String CREATE_USER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login ";
    private static final String USER_ENDPOINT = "/api/auth/user";
    private static final String CREATE_ORDER_ENDPOINT = "/api/orders";
    private static final String INGREDIENTS_ENDPOINT = "/api/ingredients";
    private static final String GET_ORDER_ENDPOINT = "/api/orders";

    @Step("Создание нового пользователя")
    public ValidatableResponse createUser(User user) {
        return given().log().all().spec(getSpec()).body(user).when().post(CREATE_USER_ENDPOINT).then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(Login loginUser) {
        return given().log().all().spec(getSpec()).body(loginUser).when().post(LOGIN_USER_ENDPOINT).then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().delete(USER_ENDPOINT).then().log().all();

    }

    @Step("Изменение пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).body(user).when().patch(USER_ENDPOINT).then().log().all();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse getUserData(String accessToken) {
        return given().log().all().spec(getSpec()).header("Authorization", accessToken).when().get(USER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа Не авторизованным пользователем")
    public ValidatableResponse createOrder() {
        return given().spec(getSpec()).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa74\"]\n}").post(CREATE_ORDER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrderUser(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa6f\",\"61c0c5a71d1f82001bdaaa74\"]\n}").post(CREATE_ORDER_ENDPOINT).then().log().all();
    }

    @Step("Создание заказа авторизованным пользователем с неверным хешем ингредиентов")
    public ValidatableResponse createIncorrectIngredients(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().body("{\n\"ingredients\": [\"1110c5a71d1f82001bdaaa6d\",\"1110c5a71d1f82001bdaaa6f\",\"1110c5a71d1f82001bdaaa74\"]\n}").post(CREATE_ORDER_ENDPOINT).then().assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Создание заказа авторизованным пользователем без ингредиентов")
    public ValidatableResponse createWithoutIngredients(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().post(CREATE_ORDER_ENDPOINT).then().log().all().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Получение данных об ингредиентах")
    public ValidatableResponse getIngredients() {
        return given().spec(getSpec()).when().get(INGREDIENTS_ENDPOINT).then().log().all();
    }

    @Step("Получение заказов авторизованным пользователем")
    public ValidatableResponse getUserAuthOrders(String accessToken) {
        return given().spec(getSpec()).header("Authorization", accessToken).when().get(GET_ORDER_ENDPOINT).then().log().all().assertThat().statusCode(200).body("success", is(true));
    }

    @Step("Получение заказов Не авторизованным пользователем")
    public ValidatableResponse getUserNoAuthOrders() {
        return given().spec(getSpec()).when().get(GET_ORDER_ENDPOINT).then().log().all();
    }
}
