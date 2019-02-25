package org.carlspring.strongbox.controllers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.carlspring.strongbox.controllers.RepositoryMappingArgumentResolver;
import org.springframework.core.MethodParameter;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * Declare a new annotation returned in mock object.
 */
@Retention(RetentionPolicy.RUNTIME)
@interface TestDemoAnnotation {
    int value();
}

@TestDemoAnnotation(50)
class TestDemoClass
{
    public int k;
}

public class RepositoryMappingArgumentResolverTest
{
    /**
     * Ensure that supportsParameter returns false when getParameterAnnotation
     * returns null.
     */
    @Test
    public void testSupportsParameterNull()
    {
        RepositoryMappingArgumentResolver resolver =
            new RepositoryMappingArgumentResolver();

        MethodParameter mp = mock(MethodParameter.class);
        when(mp.getParameterAnnotation(any(java.lang.Class.class)))
            .thenReturn(null);

        assertFalse(resolver.supportsParameter(mp));
        verify(mp).getParameterAnnotation(any(java.lang.Class.class));
    }

    /**
     * Ensure that supportsParameter returns true when getParameterAnnotation 
     * returns a non-null object.
     */
    @Test
    public void testSupportsParameterNotNull()
    {
        RepositoryMappingArgumentResolver resolver =
            new RepositoryMappingArgumentResolver();

        TestDemoClass demoClass = new TestDemoClass();
        Class c = demoClass.getClass();

        // Return the demoAnnotation on getParameterAnnotation call
        MethodParameter mp = mock(MethodParameter.class);
        when(mp.getParameterAnnotation(any(java.lang.Class.class)))
            .thenReturn(c.getAnnotation(TestDemoAnnotation.class));

        assertTrue(resolver.supportsParameter(mp));
    }
}
