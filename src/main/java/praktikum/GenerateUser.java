package praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateUser {
    public static User generic() {
        return new User("test-data@yandex.ru", "123456", "Elena");

    }

    public static User random() {
        return new User(RandomStringUtils.randomAlphabetic(10) + "@test.ru", "123456", "Elena");
    }
}
