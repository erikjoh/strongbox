# SRS

## 1 Introduction

Strongbox is an Artifact Repository Manager written in Java, one of its open issues (https://github.com/strongbox/strongbox/issues/528) requests an repository mapping argument resolver to check the validity of a repository. This project aims to solve that issue by refactoring the code.

### 1.1 Purpose

The purpose of the document is to store the requirements in a complete and consistant manner in accordance with IEEE 29148-2011.

### 1.2 Document conventions

References to code within the project are referred not by their full path but using /carlspring/ as root to increase readability (per de facto project standard).

## 2 Product perspective

The issue calls for the new implementation repository mapping to be used by carlspring/strongbox/controllers/TrashController to ensure the validity of repositories and replace duplicated code. However as validity checks of repositories are common the use can be expanded.

By using the new repository mapping the following controllers are affected by the refactor:
```
BaseArtifactController.java
BrowseController.java
RepositoryMappingArgumentResolver.java
TrashController.java
ArtifactCoordinateValidatorsManagementController.java
HttpConnectionPoolConfigurationManagementController.java
StoragesConfigurationController.java
NpmArtifactController.java
NugetArtifactController.java
```

The inclusion of a new exception will affect carlspring/strongbox/controllers/DefaultExceptionHandler.java and adding the handler will affect carlspring/strongbox/config/WebConfig.java.

## 3 Product functions

The handler will, given the id of the repository and the path, checks that the storage and repository is valid. This is done fetching the storageID and repositoryID by HTTP servlet requests and then checking the return values and using carlspring/strongbox/storage/repository/Repository.isInService().

## 4 Specific requirements

### Functional requirement 1.1
ID: R1

Title: Happy path

Description: Given a storage and respository id for which the storage and repository exists (reference to R2 and R3) and the repository is in service (reference to R4) should return the repository otherwise a RepositoryMappingException exception should be raised which is covered by requirement R2, R3 and R4.

DEP: R2, R3, R4


### Functional requirement 1.2
ID: R2

Title: Storage existence check

Description: Given a storage id for a storage which does not exist a RepositoryMappingException exception should be raised by the resolveArgument method (part of the HandlerMethodArgumentResolver interface). Otherwise, the procedure should continue. 

DEP:

### Functional requirement 1.3
ID: R3

Title: Repository existence check

Description: Given a storage instance (depends on R2) and a repository id for a repository which does not exist a RepositoryMappingException exception should be raised by the resolveArgument method (part of the HandlerMethodArgumentResolver interface). Otherwise, the procedure should continue. 

DEP: R2

### Functional requirement 1.4
ID: R4

Title: Is in service check

Description: Given a repository instance (depends on R3) a RepositoryMappingException exception should be raised if the repository is not in service (checked by repository.isInService) by the resolveArgument method (part of the HandlerMethodArgumentResolver interface). Otherwise, the procedure should continue.

DEP: R2, R3


## 6 Verification

The test suite should be updated to achieve 100% code coverage for the added and refactored functions. More specifically, in the report (report.md) the requirements are traced to requirements.
