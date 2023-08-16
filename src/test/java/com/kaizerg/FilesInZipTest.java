package com.kaizerg;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesInZipTest {
    ClassLoader cl = FilesInZipTest.class.getClassLoader();
    @Test
    @DisplayName("Проверка содержимого файла JSON в ZIP архиве")
    void testJsonInZipContainsText() throws IOException {

        try (InputStream stream = cl.getResourceAsStream("data.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final String name = entry.getName();
                if (name.contains("employee.json")) {
                    JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(zis)).getAsJsonObject();

                    JsonObject employeeObject = jsonObject.getAsJsonObject("employee");
                    Assertions.assertEquals("Vladimir", employeeObject.get("name").getAsString());
                    Assertions.assertEquals(250000, employeeObject.get("salary").getAsInt());
                    Assertions.assertTrue(employeeObject.get("married").getAsBoolean());
                }
            }
        }
    }
    @Test
    @DisplayName("Проверка содержимого файла PDF в ZIP архиве")
    void testPdfInZipContainsText() throws IOException {

        try (InputStream stream = cl.getResourceAsStream("data.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final String name = entry.getName();
                if (name.contains("test.pdf")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertTrue(pdf.text.contains("Monday"));
                }
            }
        }
    }
    @Test
    @DisplayName("Проверка содержимого файла Excel в ZIP архиве")
    void testXlsInZipContainsText() throws Exception {

        try (InputStream stream = cl.getResourceAsStream("data.zip");
             ZipInputStream zis = new ZipInputStream(stream)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final String name = entry.getName();
                if (name.contains("test.xlsx")) {
                    XLS xls = new XLS(zis);
                    String cellValue = xls.excel.getSheetAt(0)
                            .getRow(0)
                            .getCell(0)
                            .getStringCellValue();

                    Assertions.assertTrue(cellValue.contains("Monday"));
                }
            }
        }
    }
}

