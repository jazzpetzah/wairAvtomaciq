Feature: Upgrade

  @C418 @upgrade
  Scenario Outline: Check upgrade from previous build
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    # Let the content to be delivered
    Given I wait for 10 seconds
    Given I upgrade Wire to the recent version
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    Then I see the message "<Message>" in the conversation view
    And I see 1 image in the conversation view
    When User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I wait for 10 seconds
    And I scroll to the bottom of conversation view
    Then I see 2 images in the conversation view
    When I scroll to the bottom of conversation view
    And I tap on text input
    And I type the message "<Message2>" and send it
    Then I see the message "<Message2>" in the conversation view

    Examples:
      | Contact   | Name      | Picture     | Message | Message2 |
      | user1Name | user2Name | testing.jpg | Test    | Message2 |
