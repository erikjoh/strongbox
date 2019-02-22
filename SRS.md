# SRS

## 1 Introduction

<some overview>

### 1.1 Purpose

The purpose of the document is to store the requirements in a complete and consistant manner in accordance with IEEE 29148-2011.

### 1.2 Document conventions

<if any>

## 2 Product perspective

<what the function uses, what uses the function, etc>

## 3 Product functions

The handler will, given the id of the repository and the path, checks that the storage and repository is valid

## 4 Specific requirements

### Functional requirement 1.1
ID: R1

Title: Happy path

Description: Given a repository id and path if neither R2, R3 or R4 raises errors the function should return


### Functional requirement 1.2
ID: R2

Title: Storage existence

Description: Given a repository id and path which is located on a non-existant storage a exception be raised (using either spring feature @controlleradvice or @exceptionhandlershould)

DEP

### Functional requirement 1.3
ID: R3

Title: Repository existence

Description: Given a repository id and path which where the repository is invalid exception be raised (using either spring feature @controlleradvice or @exceptionhandlershould)

DEP

### Functional requirement 1.4
ID: R4

Title: Is in service

Description: Given a repository id and path which where the repository is not in service exception be raised (using either spring feature @controlleradvice or @exceptionhandlershould)

DEP

## 6 Verification

The test suite will be updated to achieve 100% code coverage for the function