Feature: Conversation

  @smoke @nonUnicode
  Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @nonUnicode
  Scenario Outline: Send Hello to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I multi tap on text input
    Then I see Hello-Hey message <Message1> in the dialog
    And I multi tap on text input again
    And I see Hello-Hey message <Message2> in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message1    | Message2        |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | YOU KNOCKED | YOU HOT KNOCKED |

  @smoke @nonUnicode
  Scenario Outline: Send Camera picture to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  #test is not implemented yet, functionality is not available in the client
  @smoke @mute @nonUnicode
  Scenario Outline: Add people to 1:1 chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page
    And I see <Contact1> user profile page
    And I press add contact button

    #And I see People picker page
    #And I input in People picker search field user name <Contact2>
    #And I see user <Contact2> found on People picker page
    #And I tap on user name found on People picker page <Contact2>
    #And I see Add to conversation button
    #And I click on Add to conversation button
    #Then I see group chat page with users <Contact1> <Contact2>
    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @nonUnicode @smoke
  Scenario Outline: Send message to group chat
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SendMessGroupChat |

  @nonUnicode @smoke
  Scenario Outline: Leave group conversation
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    And I press Leave conversation button
    And I confirm leaving
    Then I do not see <Login> on group chat info page
    And I return to group chat page
    Then I see message that I left chat

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | LeaveGroupChat |

  @nonUnicode @smoke
  Scenario Outline: Remove from group chat
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    And I select contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    Then I see  message <Message> contact <Contact2> on group page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName       | Message     |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | RemoveFromGroupChat | YOU REMOVED |

  @nonUnicode @smoke
  Scenario Outline: Accept connection request
    Given <Contact> connection request is sended to me
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login   | Password    | Name    | Contact     | WaitingMess      |
      | aqaUser | aqaPassword | aqaUser | yourContact | 1 person waiting |

  @mute @nonUnicode @smoke
  Scenario Outline: Mute conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is not muted

    Examples: 
      | Login   | Password    | Name    | Contact1    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @nonUnicode @regression
  Scenario Outline: Send Long Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type long message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @nonUnicode @regression
  Scenario Outline: Send Upper and Lower case to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type Upper/Lower case message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @unicode @regression
  Scenario Outline: Send special chars message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I input <Message> message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message                           |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | ÄäÖöÜüß simple message in english |

  @mute @nonUnicode @regression
  Scenario Outline: Send emoji message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I input <Message> message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | :) ;) :( |

  @nonUnicode @smoke
  Scenario Outline: Check contact personal info
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    Then I see <Contact> user name and email

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @unicode @staging
  Scenario Outline: Send double byte chars
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I input <Message> message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message                     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | 畑 はたけ hatake field of crops |

  @nonUnicode @staging
  Scenario Outline: Verify correct group info page information
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I see the correct participant <Contact1> and <Contact2> avatars

    Examples: 
      | Login   | Password    | Name    | Contact1          | Contact2              | ParticipantNumber | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | aqaAvatar TestContact | 3                 | GroupInfoCheck |

  @nonUnicode @staging
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    And I tap on group chat contact <Contact1>
    Then I see <Contact1> user name and email
    And I see correct background image

    Examples: 
      | Login   | Password    | Name    | Contact1          | Contact2              | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | aqaAvatar TestContact | GroupInfoCheck |
