package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MessageSenderImplTest {
    /**
     * Тестируем метод String MessageSenderImpl.send(Map<String, String> headers)
     * На вход тесту подаем тройку аргументов:
     * argumentIP - IP адрес
     * country - страна, правильно соответствующая argumentIP
     * expected - ожидаемый результат, правильно соответсвующий country
     *
     * @param argumentIP - IP адрес
     * @param country - страна, правильно соответствующая argumentIP
     * @param expected - ожидаемый результат, правильно соответсвующий country
     *
     * "пакеты" из трех входных значений поставляет метод factory
     */
    @ParameterizedTest
    @MethodSource("factory")
    void testSend(String argumentIP, Country country, String expected) {
        //given
        //входной параметр для MessageSenderImpl.send(Map<String, String> headers)
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, argumentIP);

        //Создаем моки для сервисов от которых зависит наш тестируемый
        GeoService geoService = Mockito.mock(GeoService.class);
        LocalizationService localizationService = Mockito.mock((LocalizationService.class));

        //Создаем экземпляр нашего тестируемого объекта
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        //when
        //реализуем мок: когда geoService на вход получает argumentIP, он должен вернуть нам country
        Mockito.when(geoService.byIp(argumentIP)).
                thenReturn(new Location(null, country,null,0));

        //реализуем мок: когда localizationService на вход получает country, он должен вернуть нам expected
        Mockito.when(localizationService.locale(country)).
                thenReturn(expected);

        //then
        //send работает правильно если он вернет expected
        Assertions.assertEquals(messageSender.send(headers), expected);
    }

    /**
     * метод поставляет входные
     * значения для testSend(String argumentIP, Country country, String expected)
     *
     * @return стрим аргументов для метода testSend. Именно здесь задается соответствие входных данных
     * и результатов теста
     */
    public static Stream<Arguments> factory() {
        return Stream.of(
                Arguments.of("172.1.1.1", Country.RUSSIA, "Добро пожаловать"),
                Arguments.of("96.1.1.1", Country.USA, "Welcome")
        );
    }
}
