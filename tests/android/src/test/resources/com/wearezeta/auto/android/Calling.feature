Feature: Calling

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see dialog with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in Conversation List
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    Then Conversation List contains missed call icon

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @staging @id1503
  Scenario Outline: Silence an incoming call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I click the ignore call button
    Then I cannot see the call bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |
	 
   @staging @id1497 
   Scenario Outline: Receive call while Wire is running in the background
   	Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I minimize the application
    And <Contact> calls me using <CallBackend>
    Then I see the call lock screen
    And I see a call from <Contact> in the call lock screen
    And I answer the call from the lock screen
    Then I see started call message for contact <Contact>
    
    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |