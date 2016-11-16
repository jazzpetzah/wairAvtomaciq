Feature: Ephemeral

  @C264663 @smoke @ephemeral
  Scenario Outline: Verify I can select and unselect all types of ephemeral timers
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact>
    Then I see placeholder of conversation input is Type a message
    When I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I click on ephemeral button
    And I set the timer for ephemeral to OFF
    Then I see placeholder of conversation input is Type a message

    Examples:
      | Login1     | Password      | Name      | Contact   | Time | TimeLong   | TimeShortUnit |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | s             |
      | user1Email | user1Password | user1Name | user2Name | 15   | 15 seconds | s             |
      | user1Email | user1Password | user1Name | user2Name | 30   | 30 seconds | s             |
      | user1Email | user1Password | user1Name | user2Name | 1    | 1 minute   | m             |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 minutes  | m             |
      | user1Email | user1Password | user1Name | user2Name | 1    | 1 day      | d             |

  @C262546 @smoke @ephemeral
  Scenario Outline: Verify sending of ephemeral link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Link>
    And I send message
    Then I see link <LinkInPreview> in link preview message
    And I see a title <LinkTitle> in link preview in the conversation view
    And I see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation
    When I wait for <Time> seconds
    And I see block replaces the last message in the conversation view
    And I do not see a picture <LinkPreviewImage> from link preview
    And I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    And I do not see a title <LinkTitle> in link preview in the conversation view
    And I do not see a picture <LinkPreviewImage> from link preview
    And I see 1 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Time | TimeLong    | TimeShortUnit | Link              | LinkInPreview | LinkTitle                                                                            | LinkPreviewImage |
      | user1Email | user1Password | user1Name | user2Name | 15   | 15 seconds  | s             | https://wire.com/ | wire.com      | Wire · Modern communication, full privacy. For iOS, Android, macOS, Windows and web. | linkpreview0.png |
