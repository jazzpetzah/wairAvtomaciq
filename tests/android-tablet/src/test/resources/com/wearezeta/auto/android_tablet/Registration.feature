Feature: Registration

  @C752 @regression @rc @rc44 @useSpecialEmail
  Scenario Outline: Register new user using front camera in landscape mode
    Given I see welcome screen
    Given I rotate UI to landscape
    When I tap Register button
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I verify my registration via email
    When I see the Unsplash Picture page
    And I tap Choose My Own button on Unsplash Picture page
    And I select Camera picture source on Unsplash Picture page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see the conversations list with no conversations

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C753 @regression @rc @useSpecialEmail
  Scenario Outline: Register new user using front camera in portrait mode
    Given I see welcome screen
    Given I rotate UI to portrait
    When I tap Register button
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I verify my registration via email
    When I see the Unsplash Picture page
    And I tap Choose My Own button on Unsplash Picture page
    And I select Camera picture source on Unsplash Picture page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see the conversations list with no conversations

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C479 @regression @useSpecialEmail
  Scenario Outline: (AN-2965) Verify automatic email verification is performed (portrait)
    Given I see welcome screen
    Given I rotate UI to portrait
    When I tap Register button
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I wait for 5 seconds
    When I lock the device
    And I verify my registration via email
    And I unlock the device
    Then I see the Unsplash Picture page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C512 @regression @useSpecialEmail
  Scenario Outline: (AN-2965) Verify automatic email verification is performed (landscape)
    Given I see welcome screen
    Given I rotate UI to landscape
    When I tap Register button
    And I enter the registration name "<Name>"
    And I enter the registration email "<Email>"
    And I enter the registration password "<Password>"
    And I start listening for registration email
    And I submit the registration data
    Then I see the Confirmation page
    And I see the entered email on the Confirmation page
    And I wait for 5 seconds
    When I lock the device
    And I verify my registration via email
    And I unlock the device
    Then I see the Unsplash Picture page

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
