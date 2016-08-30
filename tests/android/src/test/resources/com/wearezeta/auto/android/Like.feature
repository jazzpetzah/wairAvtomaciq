Feature: Like

  @C226019 @staging
  Scenario Outline: I can like message from message tool menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email Contact phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I long tap the Text message "<Txt>" in the conversation view
    Then I see Like button on the message bottom menu

    Examples:
      | Name      | Contact   | Txt |
      | user1Name | user2Name | Hi  |

  @C226018 @staging
  Scenario Outline: I can like message by tap on like icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I tap the Text message "<Txt>" in the conversation view
    Then I see Like button under the Text message "<Txt>"

    Examples:
      | Name      | Contact   | Txt |
      | user1Name | user2Name | Hi  |