Feature: Edit Message

  @C202362 @C202366 @staging
  Scenario Outline: Verify I can cancel editing a message by button / I can reset my editing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
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

  @C202363 @staging
  Scenario Outline: Verify I can cancel editing a message by tap on other action buttons
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Check for back button
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
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
    And I press Back button
    Then I do not see edit message toolbar

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | YO      |

  @C202361 @staging
  Scenario Outline: Verify I cannot edit another users message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Contact1> sends encrypted message "<Message>" to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I long tap the Text message "<Message>" in the conversation view
    Then I do not see Edit button on the message bottom menu

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | YO      |

  @C202357 @staging
  Scenario Outline: Verify I can edit my message in 1:1 (from other view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new device <ContactDevice>
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

  @C202358 @staging
  Scenario Outline: Verify I can edit my message in Group (from my view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
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

  @C202364 @staging
  Scenario Outline: Verify I can edit a message multiple times in a row (my view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
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