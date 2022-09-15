public class UserGenerator {
    public static User getDefault() {
        return new User("Tora1234@yandex.ru", "qwerty", "Tora");
    }

    public static User getUserWithEmptyEmail() {
        return new User("", "qwerty", "Tora");
    }

    public static User getUserWithEmptyPassword() {
        return new User("Tora1234@yandex.ru", "", "Tora");
    }

    public static User getUserWithEmptyName() {
        return new User("Tora1234@yandex.ru", "qwerty", "");
    }
}