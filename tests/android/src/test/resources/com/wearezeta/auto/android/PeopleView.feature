Feature: People View

  @id83 @id87 @regression
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on dialog page
    And I tap on group chat contact <Contact1>
    Then I see <Contact1> user name and email
    And I see correct background image

    Examples: 
      | Login   | Password    | Name    | Contact1          | Contact2    | GroupChatName   |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | aqaContact1 | GroupInfoCheck2 |

  @id319 @regression
  Scenario Outline: I can create group chat from People picker
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact1>
    And I  long tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I  long tap on user name found on People picker page <Contact2>
    And I tap on create conversation
    Then I see group chat page with users <Contact1> <Contact2>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName           |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat2 |

  @id321 @smoke
  Scenario Outline: Leave group conversation
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I press Leave conversation button
    And I confirm leaving
    Then I see Contact list with my name <Name>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | LeaveGroupChat |

  @id322 @smoke
  Scenario Outline: Remove from group chat
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    Then I see  message <Message> contact <Contact2> on group page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName       | Message     |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | RemoveFromGroupChat | YOU REMOVED |

  @regression @id594
  Scenario Outline: Verify correct group info page information
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on dialog page
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I see the correct participant <Contact1> and <Contact2> avatars

    Examples: 
      | Login   | Password    | Name    | Contact1          | Contact2              | ParticipantNumber | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | aqaAvatar TestContact | 3                 | GroupInfoCheck |
