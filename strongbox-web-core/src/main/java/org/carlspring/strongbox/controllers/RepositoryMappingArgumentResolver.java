package org.carlspring.strongbox.controllers;

import org.carlspring.strongbox.configuration.ConfigurationManager;
import org.carlspring.strongbox.storage.Storage;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.validation.RepositoryMappingException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RepositoryMappingArgumentResolver
        implements HandlerMethodArgumentResolver
{

    @Inject
    protected ConfigurationManager configurationManager;

    private static Map<String, String> getPathParameters(NativeWebRequest webRequest)
    {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter)
    {
        return methodParameter.getParameterAnnotation(RepositoryMapping.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory) throws RepositoryMappingException
    {
        Map<String, String> pathParameters = getPathParameters(nativeWebRequest);

        String storageId = pathParameters.get("storageId");
        String repositoryId = pathParameters.get("repositoryId");

        Storage storage = configurationManager.getConfiguration().getStorage(storageId);
        if (storage == null)
        {
            throw new RepositoryMappingException("The specified storageId does not exist!");
        }

        Repository repository = storage.getRepository(repositoryId);
        if (repository == null)
        {
            throw new RepositoryMappingException("The specified repositoryId does not exist!");
        }

        if (!repository.isInService())
        {
            throw new RepositoryMappingException("The specified repository is not in service!");
        }

        return repository;
    }
}
