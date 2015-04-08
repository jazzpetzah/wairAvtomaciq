Feature: Menus

  @smoke @id1648
  Scenario Outline: Check Preferences opening
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Email> and password <Password>
    And I see my name on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I see Settings dialog

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |