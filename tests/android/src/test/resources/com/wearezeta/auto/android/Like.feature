Feature: Like

  @C226019 @C226035 @staging
  Scenario Outline: I can like message from message tool menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I long tap the Text message "<Txt>" in the conversation view
    And I tap Like button on the message bottom menu
    # C226091
    Then I see Like description with expected text "<Name>" under the Text message "<Txt>"
    And I see Like button under the Text message "<Txt>"
    When I remember the state of like button for Text message "<Txt>"
    And I long tap the Text message "<Txt>" in the conversation view
    And I tap Unlike button on the message bottom menu
    # C226035
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" under the Text message "<Txt>"

    Examples:
      | Name      | Contact   | Txt | MessageStatus |
      | user1Name | user2Name | Hi  | Delivered     |

  #TODO : Merge all those TR test into one
  @C226018 @C226020 @C226034 @C226037 @staging
  Scenario Outline: I can unlike/like message by tap on like icon & I can like text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I type the message "<Txt>" and send it
    # C226037
    When I tap the Text message "<Txt>" in the conversation view
    Then I see Like hint with expected text "Tap to like" under the Text message "<Txt>"
    # C226018
    When I remember the state of like button for Text message "<Txt>"
    Then I see Like button under the Text message "<Txt>"
    # C226020
    When I tap Like button under the Text message "<Txt>"
    Then I verify the state of like button item is changed
    # C226034
    When I remember the state of like button for Text message "<Txt>"
    And I tap Like button under the Text message "<Txt>"
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" under the Text message "<Txt>"

    Examples:
      | Name      | Contact   | Txt | MessageStatus |
      | user1Name | user2Name | Hi  | Delivered     |

  @C226036 @staging
  Scenario Outline: I can double tap on txt to like and unlike
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I double tap the Text message "<Txt>" in the conversation view
    Then I see Like button under the Text message "<Txt>"
    And I see Like description with expected text "<Name>" under the Text message "<Txt>"
    When I remember the state of like button for Text message "<Txt>"
    And I double tap the Text message "<Txt>" in the conversation view
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" under the Text message "<Txt>"

    Examples:
      | Name      | Contact   | Txt | MessageStatus |
      | user1Name | user2Name | Hi  | Delivered     |

  @C226040 @C226033 @C226043 @staging
  Scenario Outline: If message was liked by somebody - like icon is visible and liker name next to the like icon, and I could like it.
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And User <Contact> likes the recent message from user Myself via device <ContactDevice>
    # C226040
    Then I see Like description with expected text "<Contact>" under the Text message "<Message>"
    And I see Like button under the Text message "<Message>"
    When I remember the state of like button for Text message "<Message>"
    And I tap Like button under the Text message "<Message>"
    # C226033
    Then I verify the state of like button item is changed
    # C226043
    And I see Like description with expected text "<Name>, <Contact>" under the Text message "<Message>"

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Hi      | Device1       |

  @C226045 @C226048 @staging
  Scenario Outline: Likes should be reset if I edited message / also could like again
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Device>
    Given I see Conversations list with conversations
    # C226045
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
    And I tap the Text message "<Message>" in the conversation view
    And I tap Like button under the Text message "<Message>"
    Then I see Like description with expected text "<Name>" under the Text message "<Message>"
    When User Myself edits the recent message to "<NewMessage>" from user <Contact1> via device <Device>
    Then I see the message "<NewMessage>" in the conversation view
    And I see Message status with expected text "<MessageStatus>" under the Text message "<NewMessage>"
    # C226048
    When I tap the Text message "<NewMessage>" in the conversation view
    And I remember the state of like button for Text message "<NewMessage>"
    And I tap Like button under the Text message "<NewMessage>"
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" under the Text message "<NewMessage>"

    Examples:
      | Name      | Contact1  | Message | Device  | NewMessage | MessageStatus |
      | user1Name | user2Name | Yo      | Device1 | Hello      | Sent          |

  @C226049 @C226050 @staging
  Scenario Outline: Verify local delete for my/others message doesn't reappear after someone liked it (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # C226049
    When I type the message "<Message>" and send it
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    # C226050
    When User <Contact> sends encrypted message "<OtherMessage>" via device <Device> to user Myself
    And I long tap the Text message "<OtherMessage>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I do not see the message "<OtherMessage>" in the conversation view

    Examples:
      | Name      | Contact   | Message | Device  | OtherMessage |
      | user1Name | user2Name | Yo      | Device1 | OMG          |

  @C226047 @staging
  Scenario Outline: Verify receiving a like in a conversation which history was removed (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And I navigate back from conversation
    And I swipe right on a <Contact>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I see Conversations list with no conversations

    Examples:
      | Name      | Contact   | Message | Device  |
      | user1Name | user2Name | Yo      | Device1 |

  @C226047 @staging
  Scenario Outline: Verify sending like to a person who block me in a 1:1 conversation (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And User <Contact> blocks user Myself
    And I tap the Text message "<Message>" in the conversation view
    And I remember the state of like button for Text message "<Message>"
    And I tap Like button under the Text message "<Message>"
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" under the Text message "<Message>"

    Examples:
      | Name      | Contact   | Message | Device  |
      | user1Name | user2Name | Yo      | Device1 |

  @C226041 @C226581 @staging
  Scenario Outline: I see likers count instead of names (example: 5 People)
    Given There are 5 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> has group chat <Group> with Myself,<Contact2>,<Contact3>,<Contact4>
    Given User <Contact1> adds new device <D1>
    Given User <Contact2> adds new device <D2>
    Given User <Contact3> adds new device <D3>
    Given User <Contact4> adds new device <D4>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given I type the message "<Message>" and send it
    When User <Contact1> likes the recent message from group conversation <Group> via device <D1>
    And User <Contact2> likes the recent message from group conversation <Group> via device <D2>
    And User <Contact3> likes the recent message from group conversation <Group> via device <D3>
    And User <Contact4> likes the recent message from group conversation <Group> via device <D4>
    # C226041
    Then I see Like description with expected text "4 people" under the Text message "<Message>"
    # C226581
    And I see First like avatar under the Text message "<Message>"
    And I see Second like avatar under the Text message "<Message>"

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Group     | Message | D1 | D2 | D3 | D4 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | LikeGroup | Hi      | D1 | D2 | D3 | D4 |

  @C226022 @staging
  Scenario Outline: I can like a picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I see a picture in the conversation view
    And I tap the recent picture in the conversation view
    And I remember the state of like button for recent Image
    And I tap Like button under the recent Image
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" under the recent Image

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |