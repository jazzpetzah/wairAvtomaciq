Feature: Settings

  @regression @1964
  Scenario Outline: Verify possibility of reseting password
    Given There is 1 user where <Name> is me
    Given I am signed out from ZClient
    Given I see Sign In screen
    Given I start Sign In
    When I select to Reset Password
    Then I see Forgot Password page in browser
    When I go to Forgot Password page
    And I see Forgot Password page in browser
    And I enter user email <Email> to change password
    And I open change password link from email
    And I reset password to <NewPassword>
    And I have entered login <Email>
    And I have entered password <NewPassword>
    And I press Sign In button
    Then I see name <Name> in User profile

    Examples: 
      | Email      | Password      | Name      | NewPassword |
      | user1Email | user1Password | user1Name | aqa234567   |
