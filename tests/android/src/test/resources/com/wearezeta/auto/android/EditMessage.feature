Feature: Edit Message

  @C202362 @regression @rc
  Scenario Outline: Verify I can cancel editing a message by button / I can reset my editing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I see edit message toolbar
    And I clear cursor input
    And I type the message "<EditMessage>"
    And I tap Reset button in edit message toolbar
    # C202366
    Then I see the message "<Message>" in cursor input
    When I clear cursor input
    When I type the message "<EditMessage>"
    And I tap Close button in edit message toolbar
    Then I do not see edit message toolbar
    And I see the message "<Message>" in the conversation view
    And I do not see the message "<EditMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Message | EditMessage |
      | user1Name | user2Name | YO      | Hello       |

  @C202363 @regression
  Scenario Outline: Verify I can cancel editing a message by tap on other action buttons
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Check for back button
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I see edit message toolbar
    And I tap Back button from top toolbar
    Then I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I do not see edit message toolbar
    # Check for audio call button
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I tap Audio Call button from top toolbar
    Then I see outgoing call
    When I hang up outgoing call
    Then I do not see edit message toolbar
    # Check for video call button
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I tap Video Call button from top toolbar
    Then I see outgoing call
    When I hang up outgoing call
    Then I do not see edit message toolbar
    # Check for top toolbar
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I tap conversation name from top toolbar
    And I tap Back button
    Then I do not see edit message toolbar

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | YO      |

  @C202357 @regression @rc
  Scenario Outline: Verify I can edit my message in 1:1 (from other view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given User <Contact1> sends encrypted message "<Message>" via device <ContactDevice> to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User <Contact1> edits the recent message to "<NewMessage>" from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    And I see the message "<NewMessage>" in the conversation view
    And I see the pen icon next to the name of <Contact1> in the conversation view

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage |
      | user1Name | user2Name | YO      | Device1       | Hello      |

  @C202359 @regression @rc
  Scenario Outline: Verify I see changed message if message was edited from another device (1:1) (own device sync)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User adds the following device: {"Myself": [{"name": "<Device>"}]}
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself edits the recent message to "<NewMessage>" from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    And I see the message "<NewMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Message | Device  | NewMessage |
      | user1Name | user2Name | Yo      | Device1 | Hello      |

  @C202360 @regression @rc
  Scenario Outline: Verify I see changed message if message was edited from another device (group) (own device sync)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User adds the following device: {"Myself": [{"name": "<Device>"}]}
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself edits the recent message to "<NewMessage>" from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    And I see the message "<NewMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message | Device  | GroupChatName | NewMessage |
      | user1Name | user2Name | user3Name | Yo      | Device1 | MyGroup       | Hello      |

  @C202358 @regression @rc
  Scenario Outline: Verify I can edit my message in Group (from my view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And User <Contact1> remembers the recent message from user Myself via device <ContactDevice>
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "<NewMessage>"
    And I tap Approve button in edit message toolbar
    Then I see the message "<NewMessage>" in the conversation view
    And I do not see the message "<Message>" in the conversation view
    And User <Contact1> sees the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage |
      | user1Name | user2Name | YO      | Device1       | Hello      |

  @C202364 @regression
  Scenario Outline: Verify I can edit a message multiple times in a row (my view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I see edit message toolbar
    And I clear cursor input
    And I type the message "<NewMessage>"
    And I tap Approve button in edit message toolbar
    And I long tap the Text message "<NewMessage>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I see edit message toolbar
    And I clear cursor input
    And I type the message "<NewMessage2>"
    And I tap Approve button in edit message toolbar
    Then I see the message "<NewMessage2>" in the conversation view
    And I see the pen icon next to the name of Myself in the conversation view
    And I do not see the message "<NewMessage>" in the conversation view
    And I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage | NewMessage2 |
      | user1Name | user2Name | YO      | Device1       | Hello      | OK          |

  @C206263 @regression @rc
  Scenario Outline: Verify the message is deleted everywhere when it is edited to empty or empty spaces
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    # Empty message
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I tap Approve button in edit message toolbar
    Then I do not see the message "<Message>" in the conversation view
    And I do not see the message separator of Myself in 10 seconds
    # Empty space message
    When I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "  "
    And I tap Approve button in edit message toolbar
    Then I do not see the message "<Message>" in the conversation view
    And I do not see the message separator of Myself in 10 seconds

    Examples:
      | Name      | Contact1  | Message | ContactDevice |
      | user1Name | user2Name | YO      | Device1       |

  @C202365 @regression
  Scenario Outline:  Verify I can switch to edit another message while editing a message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message1>" and send it by cursor Send button
    And I type the message "<Message2>" and send it by cursor Send button
    And I long tap the Text message "<Message1>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I tap Close button in edit message toolbar
    And I scroll to the bottom of conversation view
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "<NewMessage>"
    And I tap Approve button in edit message toolbar
    Then I see the message "<NewMessage>" in the conversation view
    And I see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact1  | Message1 | Message2 | NewMessage |
      | user1Name | user2Name | YO       | Hello    | Nice       |

  @C206259 @regression @rc
  Scenario Outline: Verify edited message stays in the same position as original message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message1>" and send it by cursor Send button without hiding keyboard
    And I type the message "<Message2>" and send it by cursor Send button without hiding keyboard
    And I type the message "<Message3>" and send it by cursor Send button
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "<NewMessage>"
    And I tap Approve button in edit message toolbar
    Then I see the most top conversation message is "<Message1>"
    And I see the most recent conversation message is "<Message3>"
    And I see the message "<NewMessage>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact1  | Message1 | Message2 | Message3 | NewMessage |
      | user1Name | user2Name | YO       | Hello    | Nice     | wow        |

  @C206273 @regression
  Scenario Outline:  Verify editing a message does not create unread dot
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact1> sends 1 encrypted messages to user Myself
    And I tap on conversation name <Contact1>
    And I scroll to the bottom of conversation view
    And I navigate back from conversation
    And I remember unread messages indicator state for conversation <Contact1>
    And User <Contact1> edits the recent message to "<NewMessage>" from user Myself via device <ContactDevice>
    Then I see unread messages indicator state is not changed for conversation <Contact1>

    Examples:
      | Name      | Contact1  | ContactDevice | NewMessage |
      | user1Name | user2Name | Device1       | ohno       |

  @C206277 @regression
  Scenario Outline: Verify edit message offline mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it by cursor Send button
    And I see the message "<Message>" in the conversation view
    And User <Contact1> remembers the recent message from user Myself via device <ContactDevice>
    And I enable Airplane mode on the device
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "<NewMessage>"
    And I tap Approve button in edit message toolbar
    And I disable Airplane mode on the device
    And I do not see No Internet bar in <InternetTimeout> seconds
    And I resend all the visible messages in conversation view
    Then User <Contact1> sees the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage | InternetTimeout |
      | user1Name | user2Name | YO      | Device1       | Hello      | 15              |
