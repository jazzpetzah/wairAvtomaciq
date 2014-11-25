Feature: People View

  @regression @id61
  Scenario Outline: Check confirmation request on removing person from group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    And I select to remove user from group chat
    Then I see confirmation request about removing user

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName            |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | ConfirmRemovingChat |

  @regression @id95
  Scenario Outline: Change conversation name
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I see Contact list with name <NewName>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName          | NewName      |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | RenameGroupChat   | RANDOM       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | RenameSpecSymChat | ÄäÖöÜüß conv |

  #MEC-484
  @bug @regression @id96
  Scenario Outline: Do not accept erroneous input as group conversation name (only spaces)
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I do not see conversation <NewName> in contact list
    And I see Contact list with name <ChatName>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName         | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | EditNameErr1Chat | \\u0020 |

  #MEC-484
  @bug @regression @id96
  Scenario Outline: Do not accept erroneous input as group conversation name (leading spaces)
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I do not see conversation <NewName> in contact list
    And I see Contact list with name <ChatName>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName         | NewName                 |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | EditNameErr2Chat | Test   Leading   Spaces |

  @regression @id97
  Scenario Outline: I can navigate forth and back between participant view and personal info
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    Then I see <Contact1> name in Conversation info
    And I return to participant view from personal info
    And I see conversation name <ChatName> in conversation info

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName        |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | PartViewNavChat |

  @staging @id100
  Scenario Outline: Access proﬁle information for the other participant in a 1on1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact1>
    And I open Conversation info
    Then I see <Contact1> name in Conversation info
    And I see aqaPictureContact_osx_userinfo_2560x1600.png photo in Conversation info
    And I see add new people button
    And I see block a person button

    Examples: 
      | Login   | Password    | Name    | Contact1          |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact |

  @regression @id102
  Scenario Outline: Add user to group conversation
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I open People Picker from conversation
    And I search for user <Contact3>
    And I see user <Contact3> in search results
    And I add user <Contact3> from search results
    Then I open conversation with <ChatName>
    And I see message YOU ADDED <Contact3> in conversation
    And I open Conversation info
    And I see that conversation has 4 people

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    | ChatName           |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 | AddUserToGroupChat |

  @smoke @id103
  Scenario Outline: Create group chat from 1on1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact1>
    When I open People Picker from conversation
    And I search for user <Contact2>
    And I see user <Contact2> in search results
    And I add user <Contact2> from search results
    Then I open conversation with <Contact1>, <Contact2>
    And I see message YOU STARTED A CONVERSATION WITH <Contact2>, <Contact1> in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @regression @id186
  Scenario Outline: Display conversation info correctly
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    Then I see conversation name <Contact1>, <Contact2> in conversation info
    And I see that conversation has <Number> people
    And I see <Number> participants avatars

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Number |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | 3      |

  @regression @id188
  Scenario Outline: Group conversation name is displayed
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    And I open conversation with <NewName>
    And I open Conversation info
    Then I see conversation name <NewName> in conversation info

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName               | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | CheckDisplayedNameChat | RANDOM  |

  @smoke @id471
  Scenario Outline: Leave group conversation
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I leave conversation
    And I go to archive
    And I open conversation with <ChatName>
    Then I see message YOU LEFT in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | LeaveGroupChat |

  @smoke @id492
  Scenario Outline: Remove user from group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <ChatName>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    And I remove selected user from conversation
    Then I see message YOU REMOVED <Contact1> in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | RemoveUserChat |

  @regression @id535
  Scenario Outline: Remove then add the same participant in group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    And I remove selected user from conversation
    And I see message YOU REMOVED <Contact1> in conversation
    And I open Conversation info
    And I see that conversation has 2 people
    And I open People Picker from conversation
    And I search for user <Contact1>
    And I see user <Contact1> in search results
    And I add user <Contact1> from search results
    Then I open conversation with <ChatName>
    And I see message YOU ADDED <Contact1> in conversation
    And I open Conversation info
    And I see that conversation has 3 people

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName          |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | RemoveAddSameChat |

  @regression @id618
  Scenario Outline: Verify the new conversation is created on the other end
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I create group chat with <Contact1> and <Contact2>
    And I open conversation with <Contact1>, <Contact2>
    And I see message YOU STARTED A CONVERSATION WITH <Contact2>, <Contact1> in conversation
    When I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    Then I see Contact list with name <Login>, <Contact2>
    And I open conversation with <Login>, <Contact2>
    And I see message <Login> STARTED A CONVERSATION WITH <Contact2>, <Contact1> in conversation
    And I am signing out
    And I Sign in using login <Contact2> and password <Password>
    And I see my name <Contact2> in Contact list
    Then I see Contact list with name <Login>, <Contact1>
    And I open conversation with <Login>, <Contact1>
    And I see message <Login> STARTED A CONVERSATION WITH <Contact2>, <Contact1> in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @regression @id621
  Scenario Outline: Leave group chat - second end verification
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I open People Picker from conversation
    And I search for user <Contact3>
    And I see user <Contact3> in search results
    And I add user <Contact3> from search results
    And I open conversation with <ChatName>
    And I see message YOU ADDED <Contact3> in conversation
    And I open Conversation info
    And I see that conversation has 4 people
    And I leave conversation
    And I go to archive
    And I open conversation with <ChatName>
    And I see message YOU LEFT in conversation
    Then I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    And I open conversation with <ChatName>
    And I see message <Name> LEFT in conversation
    And I open Conversation info
    And I do not see user <Name> in Conversation info
    And I see that conversation has 3 people

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    | ChatName       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 | LeaveGroupChat |
