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
  Scenario Outline: Verify I can cancel editing a message by tap on something else
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
    And I tap on center of screen
    Then I do not see edit message toolbar
    And I see the message "<Message>" in the conversation view
    And I do not see the message "<EditMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Message | EditMessage |
      | user1Name | user2Name | YO      | Hello       |

  @C202361 @staging
  Scenario Outline: Verify I cannot edit another users message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User <Contact1> sends encrypted message "<Message>" to user Myself
    And I long tap the Text message "<Message>" in the conversation view
    Then I do not see Edit button on the message bottom menu

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | YO      |

  @C202357 @staging
  Scenario Outline: Verify I can edit my message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User <Contact1> sends encrypted message "<Message>" via device <ContactDevice> to user Myself
    And User <Contact1> edits the recent message to "<NewMessage>" from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    And I see the message "<NewMessage>" in the conversation view
    # TODO: to check the pencil icon next to the name

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage |
      | user1Name | user2Name | YO      | Device1       | Hello      |
