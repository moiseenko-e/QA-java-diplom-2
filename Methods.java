package praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class Methods {


    @Step("Проверка ответа при успешном создании пользователя")
    public void checkResponseToCreatingValidUser(ValidatableResponse response, int code, Boolean status) {
        response.assertThat().statusCode(200).body("success", is(true));
    }

    @Step("Проверка ответа при создании пользователя без заполнения обязательного поля")
    public void checkResponseToCreateUserWithoutRequiredField(ValidatableResponse response) {
        response.statusCode(403).and().assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка ответа при создании существующего пользователя")
    public void checkResponseToCreateUserDouble(ValidatableResponse response) {
        response.statusCode(403).and().assertThat().body("message", equalTo("User already exists"));
    }

    @Step("Проверка ответа при вводе некорректных email или password")
    public void checkResponseToCreateUserWithEmailOrPasswordIncorrect(ValidatableResponse response) {
        response.statusCode(401).and().assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка ответа при получения заказа неавторизованным пользователем")
    public void checkResponseToGetOrderNoAuthUser(ValidatableResponse response) {
        response.statusCode(401).and().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ответа при создании заказа неавторизованным пользователем")
    public static void checkResponseToCreateOrderNoAuthUser(ValidatableResponse response) {
        response.statusCode(401).and().assertThat().body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка ответа при создании заказа")
    public static void checkResponseToCreateOrderCorrect(ValidatableResponse response) {
        response.assertThat().statusCode(200).body("success", is(true));
    }


}

