Feature: Calling

  @regression @id1831
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When Contact <Contact> calls to conversation <Name>
    And I wait for 5 seconds
    And Current call is ended
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see missed call from contact <Contact>
    And I click missed call button to call contact <Contact>
    And I see calling message for contact <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @regression @id2067
  Scenario Outline: Verify starting and ending outgoing call by same person
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    Then I see mute call, end call and speakers buttons
    And I see calling message for contact <Contact>
    When I end started call
    Then I dont see calling page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |