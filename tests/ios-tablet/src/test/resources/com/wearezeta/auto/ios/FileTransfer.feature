Feature: File Transfer

  @C145955 @staging @torun
  Scenario Outline: Verify sending the file in an empty conversation and text after it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    And I tap file transfer menu item <ItemName>
    When I type the default message and send it
    Then I see 1 default message in the dialog
    And I see file transfer placeholder

    Examples:
      | Name      | Contact   | ItemName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |