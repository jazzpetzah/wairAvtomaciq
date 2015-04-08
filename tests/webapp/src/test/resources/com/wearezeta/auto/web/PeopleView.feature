Feature: People View

  @smoke @id1691
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait up to 15 seconds until <Contact1> exists in backend search results
    When I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    And I type <Contact2> in search field of People Picker
    And I select <Contact2> from People Picker results
    And I choose to create conversation from People Picker
    And I see my name on top of Contact list
    Then I see Contact list with name <Contact1>,<Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @smoke @id1686
  Scenario Outline: Verify you can access proﬁle information for the other participant in a 1to1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    And I see username <Contact> on Single User Profile popover
    And I see Add people button on Single User Profile popover
    And I see Block button on Single User Profile popover

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1692
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Leave button on Group Participants popover
    And I confirm leave group conversation on Group Participants popover
    And I wait for 2 seconds
    Then I do not see Contact list with name <ChatName>
    When I open archive
    And I unarchive conversation <ChatName>
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
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click on participant <Contact1> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    And I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       | Message     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat | YOU REMOVED |

  @smoke @id1687
  Scenario Outline: Verify you can add participants to the group conversation by searching the user directory
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    When I click Add People button on Group Participants popover
    And I see Add People message on Group Participants popover
    And I confirm add to chat on Group Participants popover
    And I input user name <Contact3> in search field on Group Participants popover
    And I select <Contact3> from Group Participants popover search results
    And I choose to create conversation from Group Participants popover
    And I open conversation with <ChatName>
    Then I see <Message> action for <Contact3> in conversation
    When I add <Contact4> to group chat
    And I open conversation with <ChatName>
    Then I see <Message> action for <Contact4> in conversation
    And I click People button in group conversation
    And I see Group Participants popover
    And I see <Contact3>,<Contact4> displayed on Group Participants popover
    And I close Group Participants popover
    When I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    And I click Add People button on Group Participants popover
    And I confirm add to chat on Group Participants popover

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
    And I see my name on top of Contact list
    And I open conversation with <ChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    And I change group conversation title to <ChatNameEdit> on Group Participants popover
    Then I see conversation title <ChatNameEdit> on Group Participants popover
    And I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName     | ChatNameEdit   | Message                  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | BaseChatName | EditedCahtName | RENAMED THE CONVERSATION |

  @smoke @id1697
  Scenario Outline: Verify the new conversation is created on the other end from 1to1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact1>
    And I click People button in one to one conversation
    And I see Single User Profile popover
    When I click Add People button on Single User Profile popover
    And I wait up to 15 seconds until <Contact2> exists in backend search results
    And I input user name <Contact2> in search field on Single User Profile popover
    And I select <Contact2> from Single User Profile popover search results
    And I choose to create conversation from Single User Profile popover
    And I see Contact list with name <Contact1>,<Contact2>
    And I open conversation with <Contact1>,<Contact2>
    Then I see <Message> action for <Contact2>,<Contact1> in conversation
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Contact1> is me
    And I Sign in using login <Contact1> and password <Password>
    And I see my name on top of Contact list
    And I see Contact list with name <Name>,<Contact2>
    And I open conversation with <Name>,<Contact2>
    And I see user <Name> action <Message2> for <Contact2>,<Contact1> in conversation
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And User <Contact2> is me
    And I Sign in using login <Contact2> and password <Password>
    And I see my name on top of Contact list
    And I see Contact list with name <Name>,<Contact1>
    And I see user <Name> action <Message2> for <Contact2>,<Contact1> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Message                         | Message2                    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | YOU STARTED A CONVERSATION WITH | STARTED A CONVERSATION WITH |
