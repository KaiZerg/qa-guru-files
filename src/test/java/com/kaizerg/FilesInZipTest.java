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
        try (ZipInputStream zis = openZipStream()) {
            verifyZipEntryContent(zis, "employee.json", inputStream -> {
                JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
                JsonObject employeeObject = jsonObject.getAsJsonObject("employee");
                Assertions.assertEquals("Vladimir", employeeObject.get("name").getAsString());
                Assertions.assertEquals(250000, employeeObject.get("salary").getAsInt());
                Assertions.assertTrue(employeeObject.get("married").getAsBoolean());
            });
        }
    }

    @Test
    @DisplayName("Проверка содержимого файла PDF в ZIP архиве")
    void testPdfInZipContainsText() throws IOException {
        try (ZipInputStream zis = openZipStream()) {
            verifyZipEntryContent(zis, "test.pdf", inputStream -> {
                PDF pdf = new PDF(inputStream);
                Assertions.assertTrue(pdf.text.contains("Monday"));
            });
        }
    }

    @Test
    @DisplayName("Проверка содержимого файла Excel в ZIP архиве")
    void testXlsInZipContainsText() throws Exception {
        try (ZipInputStream zis = openZipStream()) {
            verifyZipEntryContent(zis, "test.xlsx", inputStream -> {
                XLS xls = new XLS(inputStream);
                String cellValue = xls.excel.getSheetAt(0)
                        .getRow(0)
                        .getCell(0)
                        .getStringCellValue();

                Assertions.assertTrue(cellValue.contains("Monday"));
            });
        }
    }

    private ZipInputStream openZipStream() {
        InputStream stream = cl.getResourceAsStream("data.zip");
        return new ZipInputStream(stream);
    }

    private void verifyZipEntryContent(ZipInputStream zis, String entryName, ZipEntryVerifier verifier) throws IOException {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            final String name = entry.getName();
            if (name.contains(entryName)) {
                verifier.verifyEntry(zis);
            }
        }
    }

    interface ZipEntryVerifier {
        void verifyEntry(InputStream inputStream) throws IOException;
    }
}