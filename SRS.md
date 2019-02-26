# SRS

## 1 Introduction

Strongbox is an Artifact Repository Manager written in Java, one of its open issues (https://github.com/strongbox/strongbox/issues/528) requests an repository mapping argument resolver to check the validity of a repository. This project aims to solve that issue by refactoring the code.

### 1.1 Purpose

The purpose of the document is to store the requirements in a complete and consistant manner in accordance with IEEE 29148-2011.

### 1.2 Document conventions

References to code within the project are referred not by their full path but using /carlspring/ as root to increase readability (per de facto project standard).

## 2 Product perspective

The new repository mapping argument resolver is used by functions within the carlspring/strongbox/controllers/TrashController to ensure the validity of repositories. The use of the argument resolver could be expanded as the operation is done in other parts of the project. However this use is beyond the scope of the issue.

The inclusion of a new exception will affect carlspring/strongbox/controllers/DefaultExceptionHandler.java and adding the handler will affect carlspring/strongbox/config/WebConfig.java.

## 3 Product functions

The handler will, given the id of the repository and the path, checks that the storage and repository is valid. This is done fetching the storageID and repositoryID by HTTP servlet requests and then checking the return values and using carlspring/strongbox/storage/repository/Repository.isInService().

## 4 Specific requirements

### Functional requirement 1.1
ID: R1

Title: Happy path

Description: Given a repository id and path if neither R2, R3 or R4 raises errors resolveArgument should return the repository

DEP: R2, R3, R4


### Functional requirement 1.2
ID: R2

Title: Storage existence

Description: Given a repository id and path which is located on a non-existant storage an RepositoryMappingException should be raised by resolveArgument

DEP:

### Functional requirement 1.3
ID: R3

Title: Repository existence

Description: Given a repository id and path which where the repository is invalid RepositoryMappingException should be raised by resolveArgument

DEP: R2

### Functional requirement 1.4
ID: R4

Title: Is in service

Description: Given a repository id and path which where the repository is not in service an RepositoryMappingException should be raised by resolveArgument

DEP: R2, R3


## 6 Verification

The test suite should be updated to achieve 100% code coverage for the added and refactored functions.