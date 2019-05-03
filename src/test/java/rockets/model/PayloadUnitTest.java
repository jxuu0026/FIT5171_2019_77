package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PayloadUnitTest {

    private Payload payload;
    private  String type;
    private Set<String> identity;
    private int weight;


    @BeforeEach
    public void setUp() {
        payload = new Payload(type,identity,weight);
    }


    @DisplayName("should throw exception when pass null to setType function")
    @Test
    public void shouldThrowExceptionToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> payload.setType(null));
        assertEquals("type cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty string to setLaunchSite")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLaunchSiteToEmpty(String type) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> payload.setType(type));
        assertEquals("type cannot be null or empty", exception.getMessage());
    }




}
