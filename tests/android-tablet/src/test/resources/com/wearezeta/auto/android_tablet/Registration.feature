Feature: Registration

  @id2286 @smoke
  Scenario Outline: Register new user using front camera in landscape mode
    Given I see welcome screen
    And I rotate UI to landscape
    And I see welcome screen
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
    And I see the Conversations list
    And I see my name on Self Profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2287 @smoke
  Scenario Outline: Register new user using front camera in portrait mode
    Given I see welcome screen
    And I rotate UI to portrait
    And I see welcome screen
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
    And I see the Conversations list
    And I see my name on Self Profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
