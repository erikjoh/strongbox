package org.carlspring.strongbox.controllers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.carlspring.strongbox.configuration.Configuration;
import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.controllers.RepositoryMappingArgumentResolver;
import org.carlspring.strongbox.validation.RepositoryMappingException;
import org.carlspring.strongbox.storage.Storage;
import org.springframework.core.MethodParameter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;

/**
 * Implements unit tests for the {@code RepositoryMappingArgumentResolver}
 * class.
 *
 * @author Nicklas Hersén
 */

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

/**
 * Extends RepositoryMappingArgumentResolver with a method to set
 * the configurationManager. Useful for mocking the configurationManager
 * instance.
 */
class RepoMockExtension extends RepositoryMappingArgumentResolver
{
    /**
     * Sets the configurationManager of the base class. Provides a way
     * to mock function calls to the configuration manager.
     *
     * @param manager
     *             - New configuration manager.
     */
    public void setConfigurationManager(ConfigurationManager manager)
    {
        configurationManager = manager;
    }
}

public class RepositoryMappingArgumentResolverTest
{
    private RepositoryMappingArgumentResolver resolver;
    private final String storageID = "sId";
    private final String repoID = "rId";

    @BeforeEach
    private void setup()
    {
        resolver = new RepositoryMappingArgumentResolver();
    }

    /**
     * Ensure that supportsParameter returns false when getParameterAnnotation
     * returns null.
     */
    @Test
    public void testSupportsParameterNull()
    {
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
        TestDemoClass demoClass = new TestDemoClass();
        Class c = demoClass.getClass();

        // Return the demoAnnotation on getParameterAnnotation call
        MethodParameter mp = mock(MethodParameter.class);
        when(mp.getParameterAnnotation(any(java.lang.Class.class)))
            .thenReturn(c.getAnnotation(TestDemoAnnotation.class));

        assertTrue(resolver.supportsParameter(mp));
        verify(mp).getParameterAnnotation(any(java.lang.Class.class));
    }

    /**
     * Ensure that resolveArgument returns the expected repository on 
     * success.
     */
    @Test
    public void testResolveArgumentSuccess()
    {
        RepoMockExtension rme = new RepoMockExtension();

        Repository mockRepo = mock(Repository.class);
        when(mockRepo.isInService()).thenReturn(true);

        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getRepository(repoID))
            .thenReturn(mockRepo);

        Configuration mockConfig = mock(Configuration.class);
        when(mockConfig.getStorage(storageID))
            .thenReturn(mockStorage);
        
        ConfigurationManager cManager = mock(ConfigurationManager.class);
        when(cManager.getConfiguration()).thenReturn(mockConfig);

        // Set the configuration manager to our mocked version.
        rme.setConfigurationManager(cManager);

        NativeWebRequest nReq = generateMockNativeWebRequest();

        assertEquals(rme.resolveArgument(null, null, nReq, null), mockRepo);

        verify(mockRepo).isInService();
        verify(mockStorage).getRepository(repoID);
        verify(mockConfig).getStorage(storageID);
        verify(cManager).getConfiguration();
    }

    /**
     * Ensure that a RepositoryMappingException is thrown when a
     * specified repository isn't in service.
     */
    @Test
    public void testResolveArgumentRepoNotInService()
    {
        RepoMockExtension rme = new RepoMockExtension();
        Repository mockRepo = mock(Repository.class);
        when(mockRepo.isInService()).thenReturn(false);

        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getRepository(repoID))
            .thenReturn(mockRepo);

        Configuration mockConfig = mock(Configuration.class);
        when(mockConfig.getStorage(storageID))
            .thenReturn(mockStorage);

        ConfigurationManager cManager = mock(ConfigurationManager.class);
        when(cManager.getConfiguration()).thenReturn(mockConfig);

        rme.setConfigurationManager(cManager);
        NativeWebRequest nReq = generateMockNativeWebRequest();

        Exception thrown = assertThrows(RepositoryMappingException.class,
                                        () -> rme.resolveArgument(null, null, nReq, null));
        assertEquals("The specified repository is not in service!", thrown.getMessage());

        verify(mockRepo).isInService();
        verify(mockStorage).getRepository(repoID);
        verify(mockConfig).getStorage(storageID);
        verify(cManager).getConfiguration();
    }

    /**
     * Ensure that a RepositoryMappingException is thrown when null
     * is returned rather than a vaild repository.
     */
    @Test
    public void testResolveArgumentNullRepo()
    {
        RepoMockExtension rme = new RepoMockExtension();
        Storage mockStorage = mock(Storage.class);
        when(mockStorage.getRepository(repoID))
            .thenReturn(null);

        Configuration mockConfig = mock(Configuration.class);
        when(mockConfig.getStorage(storageID))
            .thenReturn(mockStorage);

        ConfigurationManager cManager = mock(ConfigurationManager.class);
        when(cManager.getConfiguration()).thenReturn(mockConfig);

        rme.setConfigurationManager(cManager);
        NativeWebRequest nReq = generateMockNativeWebRequest();

        Exception thrown = assertThrows(RepositoryMappingException.class,
                                        () -> rme.resolveArgument(null, null, nReq, null));

        assertEquals("The specified repositoryId does not exist!", thrown.getMessage());

        verify(mockStorage).getRepository(repoID);
        verify(mockConfig).getStorage(storageID);
        verify(cManager).getConfiguration();
    }

    /**
     * Ensure taht a RepositoryMappingException is thrown when null 
     * is returned rather than a vaild storage.
     */
    @Test
    public void testResolveArgumentNullStorage()
    {
        RepoMockExtension rme = new RepoMockExtension();
        Configuration mockConfig = mock(Configuration.class);
        when(mockConfig.getStorage(storageID))
            .thenReturn(null);

        ConfigurationManager cManager = mock(ConfigurationManager.class);
        when(cManager.getConfiguration()).thenReturn(mockConfig);

        rme.setConfigurationManager(cManager);
        NativeWebRequest nReq = generateMockNativeWebRequest();

        Exception thrown = assertThrows(RepositoryMappingException.class,
                                        () -> rme.resolveArgument(null, null, nReq, null));

        assertEquals("The specified storageId does not exist!", thrown.getMessage());

        verify(mockConfig).getStorage(storageID);
        verify(cManager).getConfiguration();
    }

    private NativeWebRequest generateMockNativeWebRequest()
    {
        NativeWebRequest nRequest = mock(NativeWebRequest.class);
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        Map<String, String> map = new HashMap<>();
        map.put("storageId", storageID);
        map.put("repositoryId", repoID);

        when(httpRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .thenReturn(map);

        when(nRequest.getNativeRequest(any(java.lang.Class.class)))
            .thenReturn(httpRequest);

        return nRequest;
    }
}
