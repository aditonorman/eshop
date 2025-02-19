# Module 1

## Reflection 1

I implemented the Edit and Delete Product features using Spring Boot while following clean code principles such as meaningful naming, modularity, and single responsibility. Secure coding practices were applied by validating inputs and preventing direct access to sensitive operations. One improvement could be implementing better error handling for cases where a product ID is not found. Additionally, using a database instead of an in-memory list would enhance data persistence and security.

## Reflection 2

Code coverage is an essential metric in software testing that helps measure how much of the source code is exercised by tests. While achieving 100% code coverage is an ideal goal, it does not necessarily mean the code is free from bugs or errors. Code coverage only indicates which parts of the code have been executed during testing, but it does not guarantee that all edge cases and potential failures are accounted for. For example, a test may cover a particular method but fail to check for unexpected inputs or exceptions. Therefore, code coverage should be used in combination with other testing techniques, such as boundary testing, integration testing, and exploratory testing, to ensure robust software quality.

When creating a new functional test suite for verifying the number of items in the product list, duplicating the setup procedures and instance variables from prior test suites can lead to code redundancy and reduced maintainability. If multiple test suites have similar setups, maintaining and updating them becomes more challenging. If a change is required, such as modifying the test environment or updating an instance variable, all test suites need to be changed separately, increasing the risk of inconsistencies and errors.

One of the potential clean code issues in this scenario is code duplication, which violates the DRY (Don't Repeat Yourself) principle. Repetitive code makes it harder to maintain and increases the likelihood of bugs due to inconsistencies. Another issue is poor modularization, as similar test setup logic should ideally be extracted into reusable components rather than being repeated across multiple classes. Additionally, lack of proper abstraction can make the test suite harder to read and understand, reducing overall code clarity.

To improve code cleanliness, the common setup procedures and instance variables should be refactored into a shared base class or a utility class that can be reused across multiple test suites. Using inheritance, the new test suite can extend an existing base test class that provides the common setup logic, thereby reducing duplication. Alternatively, composition and helper methods can be used to encapsulate repetitive logic into separate methods that can be called by different test suites. This approach enhances modularity, improves maintainability, and ensures that future modifications are applied consistently across all test cases.

By following these improvements, the new functional test suite will maintain high code quality, making it easier to manage and extend in the future.



# Module 2

## Reflection

1. ![App Screenshot](images/img.png)
2. ![App Screenshot](images/img_1.png)

Yes, my current implementation meets the definition of Continuous Integration (CI) and Continuous Deployment (CD). My CI process is well-covered by multiple workflows that automate code checkout, building, testing, and various code analyses (such as with SonarQube, SonarCloud, and Scorecard), ensuring that every commit is validated against quality standards. Additionally, since my project is deployed automatically on Koyeb whenever I push my code, the CD aspect is fulfilled, ensuring that new changes are continuously integrated into the live environment with minimal manual intervention.