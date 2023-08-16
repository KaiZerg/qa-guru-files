package com.kaizerg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonParsingTest {
    @Test
    public void testJsonParsing() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/resources/test.json");

        List<Country> countries = objectMapper.readValue(jsonFile, new TypeReference<>() {});

        assertEquals(2, countries.size());

        Country country1 = countries.get(0);
        assertEquals("USA", country1.getName());
        assertEquals("Washington, D.C.", country1.getCapital());

        Country country2 = countries.get(1);
        assertEquals("France", country2.getName());
        assertEquals("Paris", country2.getCapital());
    }
}
