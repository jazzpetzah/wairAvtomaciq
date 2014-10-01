Feature: Participants View

  #Muted till new sync engine client stabilization
  @mute @regression @id95
  Scenario Outline: Change conversation name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I see Contact list with name <NewName>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | NewName      |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | RANDOM       |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | ÄäÖöÜüß conv |

  #Muted till new sync engine client stabilization
  @mute @regression @id186
  Scenario Outline: Display conversation info correctly
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    Then I see conversation name <Contact1>, <Contact2> in conversation info
    And I see that conversation has <Number> people
    And I see <Number> participants avatars

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Number |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | 3      |

  @regression @id61
  Scenario Outline: Check confirmation request on removing person from group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    When I open conversation with <ChatName>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    And I select to remove user from group chat
    Then I see confirmation request about removing user

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName            |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | ConfirmRemovingChat |

  @staging @id97
  Scenario Outline: I can navigate forth and back between participant view and personal info
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    And I choose user <Contact1> in Conversation info
    Then I see user <Contact1> personal info
    And I return to participant view from personal info
    And I see conversation name <Contact1>, <Contact2> in conversation info

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @regression @id95
  Scenario Outline: Edit name of group conversation
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    When I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I see Contact list with name <NewName>
    And I see message YOU RENAMED THE CONVERSATION in conversation
    And I see conversation name <NewName> in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName     | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | EditNameChat | RANDOM  |

  @regression @id96
  Scenario Outline: Do not accept erroneous input as group conversation name (only spaces)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I do not see conversation <NewName> in contact list
    And I see Contact list with name <Contact1>, <Contact2>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | \\u0020 |

  @regression @id96
  Scenario Outline: Do not accept erroneous input as group conversation name (leading spaces)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    When I open conversation with <Contact1>, <Contact2>
    And I open Conversation info
    And I set name <NewName> for conversation
    Then I see Contact list with name <NewName>
    And I see message YOU RENAMED THE CONVERSATION in conversation
    And I see conversation name Test Leading Spaces in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | NewName                 |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | Test   Leading   Spaces |

  @staging @id100
  Scenario Outline: Access proﬁle information for the other participant in a 1on1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    When I open conversation with <Contact1>
    And I open Conversation info
    Then I see <Contact1> name in Conversation info
    And I see <Contact1> email in Conversation info
    And I see aqaPictureContact600_800.jpg photo in Conversation info
    And I see add new people button
    And I see block a person button

    Examples: 
      | Login   | Password    | Name    | Contact1          |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact |

  @staging @id535
  Scenario Outline: Remove then add the same participant in group chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    And I open conversation with <Contact1>, <Contact2>
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
    Then I open conversation with <Contact1>, <Contact2>
    And I see message YOU ADDED <Contact1> in conversation
    And I open Conversation info
    And I see that conversation has 3 people

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @staging @id188
  Scenario Outline: Group conversation name is displayed
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I open conversation with <ChatName>
    And I open Conversation info
    And I set name <NewName> for conversation
    And I open conversation with <NewName>
    And I open Conversation info
    Then I see conversation name <NewName> in conversation info

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName               | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | CheckDisplayedNameChat | RANDOM  |

  @staging @id621
  Scenario Outline: Leave group chat - second end verification
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    And I create group chat with <Contact1> and <Contact2>
    And I open conversation with <Contact1>, <Contact2>
    When I open People Picker from conversation
    And I search for user <Contact3>
    And I see user <Contact3> in search results
    And I add user <Contact3> from search results
    And I open conversation with <Contact1>, <Contact2>, <Contact3>
    And I see message YOU ADDED <Contact3> in conversation
    And I open Conversation info
    And I see that conversation has 4 people
    And I set name <NewName> for conversation
    And I leave conversation
    And I see message YOU LEFT in conversation
    Then I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see Contact list with name <Contact1>
    And I open conversation with <NewName>
    And I see message <Name> LEFT in conversation
    And I open Conversation info
    And I do not see user <Name> in Conversation info
    And I see that conversation has 3 people

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    | NewName |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 | RANDOM  |
