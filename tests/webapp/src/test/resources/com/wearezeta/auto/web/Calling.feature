Feature: Calling

  @smoke @id1860 @blender
  Scenario Outline: Send text, image and knock while in the call with same user
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> is waiting for call to accept it
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I call
    And <Contact> accepts the call
    And I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to single conversation
    Then I see random message in conversation
    And I see ping message <PING>
    And I see sent picture <PictureName> in the conversation view
    
    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | pinged | userpicture_landscape.jpg |