Feature: Edit message

  @C246279 @regression
  Scenario Outline: Verify I can edit my message in 1:1 (from other view)
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given I sign in using my email
    Given User <Contact1> sends encrypted message "<Message>" via device <ContactDevice> to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact1>
    And User <Contact1> edits the recent message to "<NewMessage>" from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    And I see the message "<NewMessage>" in the conversation view
    And I see the pen icon next to the name of <Contact1> in the conversation view

    Examples:
      | Name      | Contact1  | Message | ContactDevice | NewMessage |
      | user1Name | user2Name | YO      | Device1       | Hello      |

  @C246280 @regression
  Scenario Outline: Verify I can edit my message in Group (from my view)
    Given There are 3 users where <Name> is me
    Given I rotate UI to portrait
    Given User adds the following device: {"<Contact1>": [{"name": "<ContactDevice>"}]}
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <GroupChatName>
    And I type the message "<Message>" in the Conversation view
    And I send the typed message by cursor Send button in the Conversation view
    And User <Contact1> remembers the recent message from group conversation <GroupChatName> via device <ContactDevice>
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I clear cursor input
    And I type the message "<NewMessage>" in the Conversation view
    And I tap Approve button in edit message toolbar
    Then I see the message "<NewMessage>" in the conversation view
    And I do not see the message "<Message>" in the conversation view
    And User <Contact1> sees the recent message from group conversation <GroupChatName> via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact1  | Contact2  | Message | ContactDevice | NewMessage | GroupChatName |
      | user1Name | user2Name | user3Name | YO      | Device1       | Hello      | EditGroup     |

  @C246281 @regression
  Scenario Outline: Verify I cannot dit message for someone else message
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message "<Message>" to user Myself
    Given I tap the conversation <Contact>
    When I long tap the Text message "<Message>" in the conversation view
    Then I do not see Edit button on the message bottom menu

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C246282 @C246283 @regression
  Scenario Outline: Verify I can cancel editing a message by button / I can reset my editing
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact1>
    And I type the message "<Message>" in the Conversation view
    And I send the typed message by cursor Send button in the Conversation view
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Edit button on the message bottom menu
    And I see edit message toolbar
    And I clear cursor input
    And I type the message "<EditMessage>" in the Conversation view
    And I tap Reset button in edit message toolbar
    # C246283
    Then I see the message "<Message>" in cursor input
    When I clear cursor input
    And I type the message "<EditMessage>" in the Conversation view
    And I tap Close button in edit message toolbar
    And I scroll to the bottom of the Conversation view
    Then I do not see edit message toolbar
    And I see the message "<Message>" in the conversation view
    And I do not see the message "<EditMessage>" in the conversation view

    Examples:
      | Name      | Contact1  | Message | EditMessage |
      | user1Name | user2Name | YO      | Hello       |