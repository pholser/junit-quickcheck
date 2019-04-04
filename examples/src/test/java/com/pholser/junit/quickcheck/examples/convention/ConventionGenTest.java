package com.pholser.junit.quickcheck.examples.convention;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class ConventionGenTest {
    @Property
    public void random_convention_objects_are_generated(
        Convention first,
        Convention second) {

        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }

    @Test
    public void generator_class_is_not_defined_in_services_file()
        throws Exception {

        String servicesFileName =
            "META-INF/services/com.pholser.junit.quickcheck.generator.Generator";
        Enumeration<URL> serviceFiles =
            Thread.currentThread().getContextClassLoader().getResources(servicesFileName);
        if (serviceFiles == null || !serviceFiles.hasMoreElements()) {
            return;
        }

        while (serviceFiles.hasMoreElements()) {
            URL serviceFile = serviceFiles.nextElement();

            try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(serviceFile.openStream()))) {

                assertTrue(
                    reader.lines().noneMatch(
                        ConventionGen.class.getName()::equals));
            }
        }
    }
}
