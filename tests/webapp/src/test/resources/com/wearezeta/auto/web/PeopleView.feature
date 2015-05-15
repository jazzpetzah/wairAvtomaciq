Feature: People View

  @smoke @id1691
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I select <Contact1> from People Picker results
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
      And I click confirm leave group conversation on Group Participants popover
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

  @regression @id2268
  Scenario Outline: Verify I can see participant profile of user that you requested to connect with in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given I sent connection request to <UnknownContact>
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <UnknownContact> on Group Participants popover
    Then I see username <UnknownContact> on Group Participants popover
    And I see an avatar on Group Participants popover
    And I see Remove button on Group Participants popover
    And I see correct remove from group button tool tip on Group Participants popover
    And I do not see Mail on Group Participants popover
    And I see Pending button on Group Participants popover
    And I see correct pending button tool tip on Group Participants popover
    And I see Pending text box on Group Participants popover
    When I click Pending button on Group Participants popover
    Then I see conversation with <UnknownContact> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | KnownContact | UnknownContact | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user2Name    | user3Name      | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id2269
  Scenario Outline: Verify I can see participant profile of user who has requested to connect with you in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given <UnknownContact> sent connection request to Myself
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    Then I see correct add people button tool tip
    When I click on participant <UnknownContact> on Group Participants popover
    Then I see username <UnknownContact> on Group Participants popover
    And I see an avatar on Group Participants popover
    And I see Remove button on Group Participants popover
    And I see correct remove from group button tool tip on Group Participants popover
    And I see Mail <UnknownContactMail> on Group Participants popover
    And Would open mail client when clicking mail on Group Participants popover
    And I see Pending button on Group Participants popover
    And I see correct pending button tool tip on Group Participants popover
    And I see Pending text box on Group Participants popover
    When I click Pending button on Group Participants popover
    And I click confirm connect button on Group Participants popover
    Then I see conversation with <UnknownContact> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | KnownContact | UnknownContact | UnknownContactMail | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user2Name    | user3Name      | user3Email         | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id2270
  Scenario Outline: Verify I can see participant profile of connected user in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <KnownContact> on Group Participants popover
    Then I see username <KnownContact> on Group Participants popover
    And I see Mail <KnownContactMail> on Group Participants popover
    And I see open conversation button on Group Participants popover
    When I click open conversation from Group Participants popover
    Then I see conversation with <KnownContact> is selected in conversations list

    Examples: 
      | Login      | Password      | Name      | KnownContact | KnownContactMail | UnknownContact | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user2Name    | user2Email       | user3Name      | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id2271
  Scenario Outline: Verify I can see participant profile of user I blocked in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given Myself blocked <KnownContact>
    Given I sent connection request to <UnknownContact>
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <KnownContact> on Group Participants popover
    Then I see username <KnownContact> on Group Participants popover
    And I see an avatar on Group Participants popover
    And I see Remove button on Group Participants popover
    And I see Unblock button on Group Participants popover
    And I see correct Unblock button tool tip on Group Participants popover
    And I see Mail <KnownContactMail> on Group Participants popover
    And Would open mail client when clicking mail on Group Participants popover
    When I click Unblock button on Group Participants popover
    And I confirm Unblock from group chat on Group Participants popover
    Then I see Contact list with name <KnownContact>

    Examples: 
      | Login      | Password      | Name      | KnownContact | KnownContactMail | UnknownContact | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user2Name    | user2Email       | user3Name      | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id2271
  Scenario Outline: Verify I can see participant profile of user I blocked in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given Myself blocked <KnownContact>
    Given I sent connection request to <UnknownContact>
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <KnownContact> on Group Participants popover
    Then I see username <KnownContact> on Group Participants popover
    And I see an avatar on Group Participants popover
    And I see Remove button on Group Participants popover
    And I see Unblock button on Group Participants popover
    And I see correct Unblock button tool tip on Group Participants popover
    And I see Mail <KnownContactMail> on Group Participants popover
    And Would open mail client when clicking mail on Group Participants popover
    When I click Unblock button on Group Participants popover
    And I confirm Unblock from group chat on Group Participants popover
    Then I see Contact list with name <KnownContact>

    Examples: 
      | Login      | Password      | Name      | KnownContact | KnownContactMail | UnknownContact | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user2Name    | user2Email       | user3Name      | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id1703
  Scenario Outline: Verify I can see participant profile of myself in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given Myself sent connection request to <UnknownContact>
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant Myself on Group Participants popover
    Then I see username me on Group Participants popover
    And I see an avatar on Group Participants popover
    And I see profile button on Group Participants popover
    And I see correct profile button tool tip on Group Participants popover
    And I see Mail <Mail> on Group Participants popover
    And Would open mail client when clicking mail on Group Participants popover
    When I click profile button on Group Participants popover

    Examples: 
      | Login      | Password      | Name      | Mail       | KnownContact | KnownContactMail | UnknownContact | ChatName               | Message   |
      | user1Email | user1Password | user1Name | user1Email | user2Name    | user2Email       | user3Name      | PeoplePopoverGroupChat | YOU ADDED |

  @regression @id1551
  Scenario Outline: Verify I can ignore connection request in a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <KnownContact>
    Given <KnownContact> is connected to <UnknownContact>
    Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>
    Given <UnknownContact> sent connection request to me
    Given I Sign in using login <Login> and password <Password>
    Then I see my name on top of Contact list
    When I open conversation with <ChatName>
    When I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <UnknownContact> on Group Participants popover
    Then I see correct remove from group button tool tip on Group Participants popover
    And I see username <UnknownContact> on Group Participants popover
    And I see Mail <UnknownContactMail> on Group Participants popover
    And I see Pending button on Group Participants popover
    When I click Pending button on Group Participants popover
    When I click ignore connect button on Group Participants popover
    Then I do not see connection request from one user

      Examples:
	 | Login      | Password      | Name      | KnownContact  | UnknownContact  | UnknownContactMail | ChatName               | Message   |
	 | user1Email | user1Password | user1Name | user2Name     | user3Name       | user3Email         | PeoplePopoverGroupChat | YOU ADDED |

 @staging @id1700
   Scenario Outline: Verify users can properly leave a group conversation on the other end
      Given There are 4 users where <Name> is me
      Given Myself is connected to <KnownContact>
      Given <KnownContact> is connected to <UnknownContact>,<UnknownContact2>
      Given <KnownContact> has group chat <ChatName> with Myself,<UnknownContact>,<UnknownContact2>
      Given User <KnownContact> changes avatar picture to default
      Given <UnknownContact> sent connection request to me
      Given I Sign in using login <Login> and password <Password>
      Then I see my name on top of Contact list
      When I open conversation with <ChatName>
      When I click People button in group conversation
      Then I see Group Participants popover
      And I see Myself,<KnownContact>,<UnknownContact>,<UnknownContact2> displayed on Group Participants popover
      And I do not see Archive button at the bottom of my Contact list
      When I click Leave button on Group Participants popover
      And I click confirm leave group conversation on Group Participants popover
      Then I see Archive button at the bottom of my Contact list
      And I do not see Contact list with name <ChatName>
      When I open archive
      Then I see archive list with name <ChatName>
      When I open self profile
      And I click gear button on self profile page
      And I select Sign out menu item on self profile page
      And I Sign in using login <KnownContact> and password <KnownContactPassword>
      And I open conversation with <ChatName>
      Then I see <MessageLeft> action for <Name> in conversation
      When I click People button in group conversation
      Then I see Group Participants popover
      And I see <KnownContact>,<UnknownContact>,<UnknownContact2> displayed on Group Participants popover

      Examples:
	 | Login      | Password      | Name      | KnownContact  | KnownContactPassword | UnknownContact  | UnknownContact2 | ChatName               | MessageLeft |
	 | user1Email | user1Password | user1Name | user2Name     | user2Password        | user3Name       | user4Name       | PeoplePopoverGroupChat | LEFT        |


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
    And I choose to create group conversation from Group Participants popover
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
    And I wait until <Contact2> exists in backend search results
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
