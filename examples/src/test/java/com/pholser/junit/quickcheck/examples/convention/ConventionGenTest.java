package com.pholser.junit.quickcheck.examples.convention;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class ConventionGenTest {

    @Property
    public void random_convention_objects_are_generated(Convention convention1, Convention convention2) {
        assertNotNull(convention1);
        assertNotNull(convention2);
        assertNotSame(convention1, convention2);
    }

    @Test
    public void generator_class_is_not_defined_in_services_file() throws IOException, URISyntaxException {
        String servicesFileName = "META-INF/services/com.pholser.junit.quickcheck.generator.Generator";
        Enumeration<URL> serviceFiles = Thread.currentThread().getContextClassLoader().getResources(servicesFileName);
        if (serviceFiles == null || !serviceFiles.hasMoreElements()) {
            return;
        }
        while (serviceFiles.hasMoreElements()) {
            URL serviceFile = serviceFiles.nextElement();
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(serviceFile.toURI()))) {
                assertTrue(reader.lines().noneMatch(ConventionGen.class.getName()::equals));
            }
        }
    }


}
