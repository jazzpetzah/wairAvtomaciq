Feature: Like

  @C226019 @staging
  Scenario Outline: I can like message from message tool menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I long tap the Text message "<Txt>" in the conversation view
    Then I see Like button on the message bottom menu

    Examples:
      | Name      | Contact   | Txt |
      | user1Name | user2Name | Hi  |

  @C226018 @C226020 @staging
  Scenario Outline: I can like message by tap on like icon & I can like text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I tap the Text message "<Txt>" in the conversation view
    And I remember the state of like button for Text message "<Txt>"
    Then I see Like button under the Text message "<Txt>"
    When I tap Like button under the Text message "<Txt>"
    Then I verify the state of like button item is changed

    Examples:
      | Name      | Contact   | Txt |
      | user1Name | user2Name | Hi  |

  @C226040 @C226033 @staging
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
    And I see Like description with expected text "<Name>, <Contact>" under the Text message "<Message>"

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Hi      | Device1       |