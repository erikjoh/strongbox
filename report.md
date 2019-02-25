# Report for assignment 4

This is a template for your report. You are free to modify it as needed.
It is not required to use markdown for your report either, but the report
has to be delivered in a standard, cross-platform format.

## Project

Name: Strongbox

URL: https://github.com/strongbox/strongbox

Strongbox is an Artifact Repository Manager written in Java, it allows you to store binary artifacts. It has both search and access control functionality.

## Architectural overview (optional, as one item for P+)

## Selected issue(s)

Title: HandlerMethodArgumentResolver to resolve Repository instance based on `{storageId}/{repositoryId}` path variables #528

URL: https://github.com/strongbox/strongbox/issues/528

Summary in one or two sentences:

## Onboarding experience

There are build instructions in the projects readme. Some of the links are dead which was annoying and shows a lack of care from the projects side. However we were able to build the project without much hickup anyway.

It should be noted that we have not found a way to run a specific test suite 
without rebuilding the whole project. If we simply attempt to run the tests 
multiple times with `mvn test` multiple tests will fail which didn't fail during
program compilation. This can be quite an annoyance since the whole project
takes about 10 minutes to build on a 5:th generation i5 processor.

## Requirements affected by functionality being refactored

## Existing test cases relating to refactored code

The existing test cases for the `delete` and `undelete` [TrashController](./strongbox-web-core/src/main/java/org/carlspring/strongbox/controllers/TrashController.java) 
function works by creating artificial repositories with different properties, 
performing the operations and asserting that the `delete/undelete` operation 
was successfully performed. This is done by simulating/mocking a POST request
for these specific operations. While the existing tests ensure that the operations
are are successful on artificially generated repos there are a lot of different 
cases which are not accounted for. There are for instance no tests which would 
result in the operation failing (such as attempting to delete/undelete data 
from a non-existing repo). Since the tests doesn't test the functions directly 
(they relay on mocked POST requests) a failure in another method might cause a 
test to fail. This is handled by asserting on different error messages. The 
existing TrashController tests should be considered integration tests rather 
than as unit tests.

## Test requirements 

### RepositoryMappingArgumentResolver

We implement full node-coverage (NC) for the `RepositoryMappingArgumentResolver`
class. The condensation graph below provides a overview of the structure of the 
`resolveArgument` function. In addition to this both outcomes of `supportsParameter`
are tested as well. The tests are implemented in [RepositoryMappingArgumentResolverTest](./strongbox-web-core/src/test/java/org/carlspring/strongbox/controllers/RepositoryMappingArgumentResolverTest.java).

![Condensation graph for resolveArgument](./doc-resources/Condensation_graph_resolve_argument.svg)

```
C1: storage == null
C2: repository == null
C3: !repository.isInService()
```

#### Test requirements

**TR1:** n0

**TR2:** n1

**TR3:** n2

**TR4:** n3

**TR5:** n4

**TR6:** n5

**TR7:** n6

**TR8:** n7

**TR9:** n8

**TR10:** getPathParameters()

**TR11:** supportsParameter() == true

**TR12:** supportsParameter() == false


## The refactoring carried out

(Link to) a UML diagram and its description

## Test logs

Overall results with link to a copy of the logs (before/after refactoring).

The refactoring itself is documented by the git log.

## Effort spent

For each team member, how much time was spent in

1. plenary discussions/meetings;

2. discussions within parts of the group;

3. reading documentation;

4. configuration;

5. analyzing code/output;

6. writing documentation;

7. writing code;

8. running code?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
