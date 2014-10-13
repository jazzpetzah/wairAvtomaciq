Feature: Connect to user

  #Muted till new sync engine client stabilization
  @mute @smoke @id473
  Scenario Outline: Receive invitation from user
    Given I send invitation to <Name> by <Contact>
    When I Sign in using login <Login> and password <Password>
    And I see connect invitation
    And I open conversation with One person waiting
    And I accept invitation
    Then I see Contact list with name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | yourContact |

  @regression @id616
  Scenario Outline: Conversation created on second end after user accept connection request
    Given I send invitation to <Name> by <Contact>
    When I Sign in using login <Login> and password <Password>
    And I see connect invitation
    And I open conversation with One person waiting
    And I accept invitation
    And I see Contact list with name <Contact>
    And I am signing out
    And I Sign in using login <Contact> and password <Password>
    Then I see Contact list with name <Name>
    And I open conversation with <Name>
    And I see message CONNECTED TO <Name> in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | yourContact |
