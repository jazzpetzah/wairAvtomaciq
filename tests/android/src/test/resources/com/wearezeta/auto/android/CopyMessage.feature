Feature: Copy Message

  @C119435 @regression @rc
  Scenario Outline: Verify could copy text message by copy button in top toolbar
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Copy button on the action mode bar
    Then I verify that Android clipboard content equals to "<Message>"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C119437 @regression @rc
  Scenario Outline: Verify copy button is invisible when more than 1 message are selected or image/ping is selected
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    Given User <Contact> sends encrypted message "<Message>" to user Myself
    # Wait for the content to sync up
    Given I wait for 5 seconds
    And I tap on conversation name <Contact>
    And I scroll to the bottom of conversation view
    When I long tap the Text message "<Message>" in the conversation view
    And I tap the recent picture in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap the Text message "<Message>" in the conversation view
    Then I do not see Copy button on the action mode bar

    Examples:
      | Name      | Contact   | Message | ImageName   |
      | user1Name | user2Name | Yo      | testing.jpg |

  @C119436 @regression @rc
  Scenario Outline: Verify copy multimedia link from youtube/soundcould
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    When User <Contact> sends encrypted message "<Message>" to user Myself
    Then I see Play button on Youtube container
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Copy button on the action mode bar
    Then I verify that Android clipboard content equals to "<Message>"

    Examples:
      | Name      | Contact   | Message                                     |
      | user1Name | user2Name | https://www.youtube.com/watch?v=gIQS9uUVmgk |

  @C165146 @regression
  Scenario Outline: Verify copy link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> send encrypted message "<Link>" to user Myself
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I long tap Link Preview container in the conversation view
    And  I tap Copy button on the action mode bar
    Then I verify that Android clipboard content equals to "<Link>"

    Examples:
      | Name      | Contact   | Link                                                                                               |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |