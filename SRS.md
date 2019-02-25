# SRS

## 1 Introduction

<some overview>

### 1.1 Purpose

The purpose of the document is to store the requirements in a complete and consistant manner in accordance with IEEE 29148-2011.

### 1.2 Document conventions

References to code within the project are referred not by their full path but using /carlspring/ as root to increase readability (per de facto project standard).

## 2 Product perspective

The repository mapping argument resolver is used by functions within the carlspring/strongbox/controllers/TrashController.java to ensure the validity of repositories.

## 3 Product functions

The handler will, given the id of the repository and the path, checks that the storage and repository is valid. This is done fetching the storageID and repositoryID by HTTP servlet requests and then checking the return values and using carlspring/strongbox/storage/repository/Repository.isInService().

## 4 Specific requirements

### Functional requirement 1.1
ID: R1

Title: Happy path

Description: Given a repository id and path if neither R2, R3 or R4 raises errors the function should return the repository

DEP: R2, R3, R4


### Functional requirement 1.2
ID: R2

Title: Storage existence

Description: Given a repository id and path which is located on a non-existant storage an RepositoryMappingException should be raised

DEP:

### Functional requirement 1.3
ID: R3

Title: Repository existence

Description: Given a repository id and path which where the repository is invalid RepositoryMappingException should be raised

DEP: R2

### Functional requirement 1.4
ID: R4

Title: Is in service

Description: Given a repository id and path which where the repository is not in service an 
RepositoryMappingException should be raised

DEP: R2, R3


## 6 Verification

The test suite should be updated to achieve 100% code coverage for the added and refactored functions