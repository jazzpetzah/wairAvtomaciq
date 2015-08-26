Feature: Registration

  @id2286 @smoke
  Scenario Outline: Register new user using front camera in landscape mode
    Given I see welcome screen
    Given I rotate UI to landscape
    Given I see welcome screen
    When I tap Register button
    And I see the Registration form
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"  
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I verify my registration via email
    And I see the Take Registration Picture page
    And I tap Camera button on the Take Registration Picture page
    And I tap Take Picture button on the Take Registration Picture page
    And I confirm my picture on the Take Registration Picture page
    And I see the conversations list with no conversations
    And I see my name on Self Profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2287 @smoke
  Scenario Outline: Register new user using front camera in portrait mode
    Given I see welcome screen
    Given I rotate UI to portrait
    Given I see welcome screen
    When I tap Register button
    And I see the Registration form
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"  
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I verify my registration via email
    And I see the Take Registration Picture page
    And I tap Camera button on the Take Registration Picture page
    And I tap Take Picture button on the Take Registration Picture page
    And I confirm my picture on the Take Registration Picture page
    And I see the conversations list with no conversations
    And I see my name on Self Profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2821 @staging
  Scenario Outline: Verify automatic email verification is performed (portrait)
    Given I see welcome screen
    Given I rotate UI to portrait
    Given I see welcome screen
    When I tap Register button
    And I see the Registration form
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"  
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    When I lock the device
    And I verify my registration via email
    And I unlock the device
    And I wait for 10 seconds
    Then I see the Take Registration Picture page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id3105 @staging
  Scenario Outline: Verify automatic email verification is performed (landscape)
    Given I see welcome screen
    Given I rotate UI to landscape
    Given I see welcome screen
    When I tap Register button
    And I see the Registration form
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"  
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    When I lock the device
    And I verify my registration via email
    And I unlock the device
    And I wait for 10 seconds
    Then I see the Take Registration Picture page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |