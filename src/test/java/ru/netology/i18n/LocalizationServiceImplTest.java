package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import java.util.stream.Stream;

public class LocalizationServiceImplTest {
    @ParameterizedTest
    @MethodSource("factory")
    void testLocale(Country country, String expected) {
        //given
        LocalizationService localizationService = new LocalizationServiceImpl();

        //when
        String result = localizationService.locale(country);

        //then
        Assertions.assertEquals(expected, result);
    }

    /**
     * метод поставляет входные значения для testLocale(Country country, String expected)
     *
     * @return стрим аргументов для метода testLocale(Country country, String expected)
     */
    public static Stream<Arguments> factory() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.BRAZIL, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome"),
                Arguments.of(Country.USA, "Welcome")
        );
    }
}
