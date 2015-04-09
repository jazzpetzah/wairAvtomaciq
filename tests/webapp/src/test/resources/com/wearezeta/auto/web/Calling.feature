Feature: Calling

  @staging @id1860
  Scenario Outline: Send text, image and knock while in the call with same user
    Given my browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> is waiting for call to accept it
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I call
    And <Contact> accepts the call
    And I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see ping message <PING>
    And I see sent picture <PictureName> in the conversation view

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | pinged | userpicture_landscape.jpg |

  @staging
  Scenario Outline: Call a user twice in a row
    Given my browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> is waiting for call to accept it
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I call
    And <Contact> accepts the call
    And I end the call
    And I wait for 3 seconds
    And I call
    Then <Contact> accepts the call

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2013
  Scenario Outline: Missed call notification (caller)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open conversation with <Contact>
    And I call
    And I wait for 5 seconds
    And I end the call
    When I open conversation with <Contact>
    Then I see conversation with my missed call

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id2014
  Scenario Outline: Missed call notification (adressee)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When Contact <Contact> calls to conversation <Name>
    And I wait for 5 seconds
    And Current call is ended
    When I open conversation with <Contact>
    Then I see conversation with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
