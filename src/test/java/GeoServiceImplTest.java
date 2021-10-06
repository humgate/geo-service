import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTest {

    /**
     * Тестируем метод byIp(String IP)
     * На вход тесту подаем два аргумента:
     * argumentIP - IP адрес
     * country - страна, правильно соответствующая argumentIP
     * expected - ожидаемый результат, правильно соответсвующий country
     *
     * @param argumentIP - IP адрес
     * @param expected   - ожидаемый результат, правильно соответсвующий argumentIP
     *
     * "пакеты" из двух входных значений поставляет метод factory
     */
    @ParameterizedTest
    @MethodSource("factory")
    void testByIp(String argumentIP, Location expected) {
        //given
        GeoService geoService = new GeoServiceImpl();

        //when
        Location location = geoService.byIp(argumentIP);

        //then
        /*
         * я бы конечно добавил в Location переопределение equals
         * и здесь написал: Assertions.assertEquals(expected, location),
         * но по условиям задания, не понятно, можно ли менять исходный код,
         * потому проверяем каждое поле отдельно:
         */
        Assertions.assertEquals(expected.getCity(), location.getCity());
        Assertions.assertEquals(expected.getCountry(), location.getCountry());
        Assertions.assertEquals(expected.getStreet(), location.getStreet());
        Assertions.assertEquals(expected.getBuiling(), location.getBuiling());
    }

    /**
     * метод поставляет входные значения для testByIp(String argumentIP, Location expected)
     *
     * @return стрим аргументов для метода testByIp(String argumentIP, Location expected).
     */
    public static Stream<Arguments> factory() {
        return Stream.of(
                Arguments.of("127.0.0.1",
                        new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11",
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149",
                        new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.1.1.11",
                        new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.1.1.11",
                        new Location("New York", Country.USA, null, 0))
        );
    }

    /**
     * Тестируем метод testByUnknownIp(String argumentIP)
     * На вход тесту подаем один аргумент:
     * argumentIP - IP адрес
     *
     * @param argumentIP - IP адрес
     *
     * "пакеты" из входных значений поставляет метод factoryUnknown
     */
    @ParameterizedTest
    @MethodSource("factoryUnknown")
    void testByUnknownIp(String argumentIP) {
        //given
        GeoService geoService = new GeoServiceImpl();

        //when
        Location location = geoService.byIp(argumentIP);

        //then
        Assertions.assertNull(location);
    }

    /**
     * метод поставляет входные значения для testByUnknownIp(String argumentIP)
     *
     * @return стрим аргументов для метода testByUnknownIp(String argumentIP).
     */
    public static Stream<Arguments> factoryUnknown() {
        return Stream.of(
                Arguments.of("10.0.0.1"),
                Arguments.of("120.0.0.1"),
                Arguments.of("130.0.0.1"),
                Arguments.of("150.0.0.1"),
                Arguments.of("160.0.0.1")
        );
    }
}