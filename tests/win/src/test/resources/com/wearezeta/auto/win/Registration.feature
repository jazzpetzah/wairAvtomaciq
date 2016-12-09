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
    Then I see watermark
    And User <Name> is Me without avatar
    And I see take over screen
    And I see name <Name> on take over screen
    And I see unique username starts with <Name> on take over screen
    And I see ChooseYourOwn button on take over screen
    And I see TakeThisOne button on take over screen
    When I click ChooseYourOwn button on take over screen
    Then I see name <Name> in account preferences
    And I see unique username starts with <Name> in account preferences
    And I see user email <Email> in account preferences

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |