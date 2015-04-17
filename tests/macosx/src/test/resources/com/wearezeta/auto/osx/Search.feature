Feature: Search

  @regression @id1391
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    And I see Top People list in People Picker
    And I choose person from Top People
    And I press create conversation to enter conversation
    Then I see message CONNECTED TO <Contact> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id469
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I wait up to 15 seconds until <Contact1> exists in backend search results
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    When I open People Picker from contact list
    And I search for user <Contact1>
    And I select connected contact <Contact1> from search results
    And I search for user <Contact2>
    And I select connected contact <Contact2> from search results
    And I press create conversation to enter conversation
    And I see my name <Name> in Contact list
    Then I open conversation with <Contact1>,<Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
      