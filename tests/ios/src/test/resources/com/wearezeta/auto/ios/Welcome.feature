Feature: Welcome

  @regression @id300 @C1007
  Scenario Outline: Open and close the terms of service on the welcome screen
    Given I see sign in screen
    And I press Terms of Service link
    Then I see the terms info page
    And I return to welcome page

  Examples:
    | Do not delete |
    | Do not delete |