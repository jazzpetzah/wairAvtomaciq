Feature: Registration

  @C2768 @rc @regression @useSpecialEmail
  Scenario Outline: Automatic email verification [LANDSCAPE]
    Given I rotate UI to landscape
    Given I see sign in screen
    When I enter name <Name>
    And I enter email <Email>
    And I enter password <Password>
    And I start activation email monitoring
    And I click Create Account Button
    And I accept terms of service
    And I see confirmation page
    And I verify registration address
    And I press Choose Own Picture button
    And I press Choose Photo button
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    And I tap Share Contacts button on Share Contacts overlay
    Then I see conversations list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |