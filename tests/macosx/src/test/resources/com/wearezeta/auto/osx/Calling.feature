Feature: Calling

  @staging @id951
  Scenario Outline: Send text, image and knock in other convo while in the call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When <Contact1> calls me
    And I see incoming call from <Contact1>
    And I accept call
    And I see ongoing call with <Contact1>
    And I open conversation with <Contact2>
    When I write random message
    And I send message
    Then I see random message in conversation
    When I ping user
    Then I see message YOU PINGED in conversation
    When I send picture testing.jpg
    Then I see picture in conversation
    When I open conversation with <Contact1>
    Then I see ongoing call with <Contact1>
    And <Contact1> dismisses call

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

      
  @staging @id952
  Scenario Outline: Receive call while Wire is running in background
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I minimize Wire
    When <Contact1> calls me
    And I see incoming call popup from <Contact1>
    And I answer call from popup
    And I see ongoing call with <Contact1>
    And <Contact1> dismisses call

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

      