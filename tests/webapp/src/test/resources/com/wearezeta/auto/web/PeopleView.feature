Feature: People View

  @smoke @id1691
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

  @smoke @id1692
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show participant profile button
    And I see Participant Profile Popup Page
    When I click leave group chat
    And I confirm leave group chat
    And I wait for 2 seconds
    Then I do not see Contact list with name <ChatName>
    When I open archive
    And I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       | Message  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat | YOU LEFT |

  @smoke @id1694
  Scenario Outline: Verify you can remove participants from a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show participant profile button
    And I see Participant Profile Popup Page
    When I click on participant <Contact1>
    And I remove user from group chat
    And I confirm remove from group chat
    And I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       | Message     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat | YOU REMOVED |

  @staging @id1687
  Scenario Outline: Verify you can add participants to the group conversation by searching the user directory
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show participant profile button
    And I see Participant Profile Popup Page
    When I click Add People button
    And I see Add People message
    And I confirm add to chat
    And I input user name <Contact3> in search field
    And I select <Contact3> from Popup Page search results
    And I choose to create conversation from Popup Page
    And I open conversation with <ChatName>
    Then I see <Message> action for <Contact3> in conversation
    When I add <Contact4> to group chat
    And I open conversation with <ChatName>
    Then I see <Message> action for <Contact4> in conversation
    And I click show participant profile button
    And I see Participant Profile Popup Page
    And I see <Contact3>,<Contact4> displayed on Participant Profile Page
    When I open conversation with <ChatName>
    And I click show participant profile button
    And I see Participant Profile Popup Page
    And I click Add People button
    And I confirm add to chat

    #add last verification - that no one left to add
    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | ChatName       | Message   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | AddToGroupChat | YOU ADDED |

  @smoke @id1683
  Scenario Outline: Verify the name of the group conversation can be edited
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I click show participant profile button
    And I see Participant Profile Popup Page
    And I change group conversation title to <ChatNameEdit>
    Then I see conversation title <ChatNameEdit> in Participants profile
    And I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName     | ChatNameEdit   | Message                  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | BaseChatName | EditedCahtName | RENAMED THE CONVERSATION |

  @staging @id1697
  Scenario Outline: Verify the new conversation is created on the other end from 1to1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact1>
    And I click show user profile button
    And I see User Profile Popup Page
    When I click Add People button
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    And I input user name <Contact2> in search field
    And I select <Contact2> from Popup Page search results
    And I choose to create conversation from Popup Page
    And I see Contact list with name <Contact1>,<Contact2>
    And I open conversation with <Contact1>,<Contact2>
    Then I see <Message> action for <Contact2>,<Contact1> in conversation
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Contact1> is me
    And User <Contact1> change avatar picture to default
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    And I see Contact list with name <Name>,<Contact2>
    And I open conversation with <Name>,<Contact2>
    And I see user <Name> action <Message2> for <Contact2>,<Contact1> in conversation
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Contact2> is me
    And User <Contact2> change avatar picture to default
    And I Sign in using login <Contact2> and password <Password>
    And I see my name <Contact2> in Contact list
    And I see Contact list with name <Name>,<Contact1>
    And I see user <Name> action <Message2> for <Contact2>,<Contact1> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Message                         | Message2                    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | YOU STARTED A CONVERSATION WITH | STARTED A CONVERSATION WITH |
