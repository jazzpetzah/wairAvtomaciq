Feature: Calling

  @torun @id1860
  Scenario Outline: Send text while in the call with same user
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> is waiting for call to accept it
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I call
    And I allow access to the microphone
    And <Contact> accepts the call
    And I write random message
    And I send message
    Then I see random message in conversation
    
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |