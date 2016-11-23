Feature: Registration

  @C2322 @smoke
  Scenario Outline: Verify new user can be registered
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I accept the Terms of Use
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me without avatar
    Then I see first time experience with watermark
    When I open preferences by clicking the gear button
    Then I see name <Name> in account preferences
    And I see user email <Email> in account preferences

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |