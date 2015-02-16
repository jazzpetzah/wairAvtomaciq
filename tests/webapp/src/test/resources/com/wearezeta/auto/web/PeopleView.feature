Feature: People View

  @staging @id1691
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    And I wait for 30 seconds
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from Contact List
    And I wait up to 15 seconds until <Contact1> exists in backend search results
    And I search for <Contact1> in People Picker
    And I select <Contact1> from People Picker results
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    And I search for <Contact2> in People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I see Contact list with name <Name>
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @smoke @id1686
  Scenario Outline: Verify you can access proﬁle information for the other participant in a 1to1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And I click show user profile button
    Then I see User Profile Popup Page
    And I see on User Profile Popup Page User username <Contact>
    And I see Add people button on User Profile Popup Page
    And I see Block button on User Profile Popup Page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
      
  @staging @id1692
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show user profile button
    And I see User Profile Popup Page
    When I click leave group chat
    And I confirm leave group chat
    And I wait for 2 seconds
    Then I do not see Contact list with name <ChatName>
    When I open archive
    And I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       | Message    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat | you left   |
      
      
  @staging @id1694
  Scenario Outline: Verify you can remove participants from a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show user profile button
    And I see User Profile Popup Page
    When I click on participant <Contact1>
    And I remove user from group chat
    And I confirm remove from group chat
    And I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       | Message       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat | you removed   |
