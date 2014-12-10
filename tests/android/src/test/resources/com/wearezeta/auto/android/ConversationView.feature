Feature: Conversation View

  @id316 @smoke
  Scenario Outline: Send Message to contact
    Given I have 1 users and 1 contacts for 1 users
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

  @id317 @smoke
  Scenario Outline: Send Hello and Hey to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Hello-Hey message <Message> with <Action> in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message    | Action |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | YOU | PINGED |

  @id318 @smoke
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

  @id1262 @smoke
  Scenario Outline: Add people to 1:1 chat
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page
    And I see <Contact1> user profile page
    And I press add contact button
    And I see People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1> <Contact2>
    And I navigate back from group chat page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @id320 @smoke
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

  @id143 @regression
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

  @id145 @regression
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

  @id146 @unicode @regression
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

  @mute @regression @id149
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

  @id147 @unicode @regression
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

  @regression @id162
  Scenario Outline: Send picture from gallery into 1:1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Gallery" button
    And I select picture for dialog
    And I press "Confirm" button
    Then I see new photo in the dialog
    Then I see uploaded picture

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 |
  
