Feature: Sign In

  @smoke @id1788 @torun
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I see invitation page
    Given I enter invitation code
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email <Email>
    And I enter password <Password>
    And I press Sign In button
    Then I see my name <Name> in Contact list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
