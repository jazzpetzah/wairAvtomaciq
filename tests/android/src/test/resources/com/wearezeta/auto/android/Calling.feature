Feature: Calling

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see dialog with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in Conversation List
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    Then Conversation List contains missed call icon

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @id1503 @regression
  Scenario Outline: Silence an incoming call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I click the ignore call button
    Then I cannot see the call bar

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @staging @id1497
  Scenario Outline: Receive call while Wire is running in the background
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I minimize the application
    And <Contact> calls me using <CallBackend>
    Then I see the call lock screen
    And I see a call from <Contact> in the call lock screen
    And I answer the call from the lock screen
    Then I see started call message for contact <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @staging @id1499
  Scenario Outline: Receive call while mobile in sleeping mode(screen locked)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I lock the device
    And <Contact> calls me using <CallBackend>
    Then I see the call lock screen
    And I see a call from <Contact> in the call lock screen
    And I answer the call from the lock screen
    Then I see started call message for contact <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @staging @id347
  Scenario Outline: Send text, image and knock while in the call with same user
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I answer the call from the overlay bar
    And I see started call message for contact <Contact>
    And I tap on text input
    And I input <Message> message
    And I send the message
    Then I see my message in the dialog
    When I press back button
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog
    When I swipe on text input
    And I press Ping button
    Then I see Hello-Hey message <SystemMessage> with <Action> in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Message                   | SystemMessage | Action |
      | user1Email | user1Password | user1Name | user2Name | autocall    | simple message in english | YOU           | PINGED |

  @id2210 @regression
  Scenario Outline: Calling bar buttons are clickable and change its state
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given <Name> has an accent color <AccentColor>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Call button
    Then I see call overlay
    And I press Mute button
    Then I see MUTE calling button is pressed
    And I press Speaker button
    Then I see SPEAKER calling button is pressed
    And I press Cancel call button
    Then I do not see call overlay

    Examples: 
      | Login      | Password      | Name      | Contact   | AccentColor |
      | user1Email | user1Password | user1Name | user2Name | StrongBlue  |

  @id2212 @staging
  Scenario Outline: Correct calling bar in different places
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact2> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When <Contact1> calls me using <CallBackend>
    And I answer the call from the overlay bar
    And I see dialog page
    Then I see calling overlay Big bar
    And I navigate back from dialog page
    And I see Contact list
    And I swipe down contact list
    And I see People picker page
    And I see calling overlay Micro bar
    And I press Clear button
    Then I see Contact list
    And I tap on my name <Name>
    And I see personal info page
    And I see calling overlay Micro bar
    And I swipe right to contact list
    And I see calling overlay Micro bar
    And I see Contact list
    And I tap on contact name <Contact2>
    And I see dialog page
    And I see calling overlay Mini bar

    Examples: 

      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | autocall  |
