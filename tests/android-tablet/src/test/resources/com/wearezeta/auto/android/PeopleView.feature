Feature: People View

  @id2257 @staging
  Scenario Outline: Leave group conversation in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on profile button
    And I press Right conversation button
    And I press Leave conversartion button
    And I confirm leaving
    Then I see Contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @id2258 @staging 
  Scenario Outline: Remove from group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    And I see contact list loaded with name <Contact1>
    And I see contact list loaded with name <Contact2>
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on profile button
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    And I see tablet dialog page
    Then I see message <Message> contact <Contact2> on group page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName       | Message     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |

  @id2282 @staging
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Contact <Contact1> send message to user <Name>
    Given Contact <Name> send message to user <Contact1>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    And I wait for 90 seconds
    When I swipe down contact list
    And I see People picker page
    And I tap on <Contact1> in Top People
    And I tap on tablet create conversation
    And I see tablet dialog page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id2243 @staging
  Scenario Outline: Leave group conversation in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on profile button
    And I press Right conversation button
    And I press Leave conversartion button
    And I confirm leaving
    Then I see Contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @id2244 @staging
  Scenario Outline: Remove from group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    And I see contact list loaded with name <Contact1>
    And I see contact list loaded with name <Contact2>
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on profile button
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    And I see tablet dialog page
    Then I see message <Message> contact <Contact2> on group page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName       | Message     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |

  @id2283 @staging
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Contact <Contact1> send message to user <Name>
    Given Contact <Name> send message to user <Contact1>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    And I wait for 90 seconds
    When I swipe down contact list
    And I see People picker page
    And I tap on <Contact1> in Top People
    And I tap on tablet create conversation
    And I see tablet dialog page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
