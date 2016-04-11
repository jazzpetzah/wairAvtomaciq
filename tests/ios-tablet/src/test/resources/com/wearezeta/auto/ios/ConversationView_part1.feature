Feature: Conversation View

  @C2645 @rc @regression @id2375
  Scenario Outline: Verify sending message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2644 @regression @id2695
  Scenario Outline: Receive message from contact [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2621 @regression @id2413
  Scenario Outline: Verify sending image [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button on iPad
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2615 @regression @rc @id2407
  Scenario Outline: Verify sending image [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I press Add Picture button on iPad
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2642 @regression @id2429 @C3222
  Scenario Outline: Verify you can see Ping on the other side - 1:1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <Contact1> change name to <ContactName>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given User <Contact1> securely pings conversation <Name>
    When I wait for 3 seconds
    Then I see "<ContactName> PINGED" system message in the conversation view

    Examples:
      | Name      | Contact1  | ContactName |
      | user1Name | user2Name | OtherUser   |

  @C2640 @regression @id2427 @C3224
  Scenario Outline: Verify you can see Ping on the other side - group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see "<Contact1> PINGED" system message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | ReceivePingGroupChat |

  @C2627 @regression @id2669
  Scenario Outline: Receive a camera roll picture from user from contact list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to <ConversationType> conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @C2628 @regression @id2670
  Scenario Outline: Receive a camera roll picture from user from contact list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2647 @regression @id2737
  Scenario Outline: Send Message to contact after navigating away from chat page [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Me is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I type the default message
    And I tap on contact name <Contact2>
    And I tap on contact name <Contact1>
    And I tap on text input
    And I press Enter key in Simulator window
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2655 @regression @id2745
  Scenario Outline: Copy and paste to send the message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I see sign in screen
    When I tap I HAVE AN ACCOUNT button
    And I have entered login <Text>
    And I tap and hold on Email input
    And I click on popup SelectAll item
    And I click on popup Copy item
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I accept First Time overlay if it is visible
    And I dismiss settings warning
    And I see conversations list
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I paste and commit the text
    Then I see last message in dialog is expected message <Text>

    Examples:
      | Login      | Password      | Name      | Contact   | Text       |
      | user1Email | user1Password | user1Name | user2Name | TextToCopy |

  @C2657 @regression @id2747
  Scenario Outline: Send a text containing spaces on either end of message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "   " message and send it
    Then I see 0 default messages in the dialog
    When I type the default message
    And I type the "   " message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2676 @regression @id2987
  Scenario Outline: I can send a sketch[PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2677 @regression @id2988
  Scenario Outline: I can send a sketch[LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I tap on sketch button in cursor
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the dialog

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2658 @regression @id3193
  Scenario Outline: Verify sending ping in 1-to-1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Ping button
    Then I see "<PingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact   | PingMsg    |
      | user1Name | user2Name | YOU PINGED |

  @C2660 @regression @id3195
  Scenario Outline: Send message to group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SimpleGroup   |
