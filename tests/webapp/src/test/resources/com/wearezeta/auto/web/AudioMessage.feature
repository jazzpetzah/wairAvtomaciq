Feature: Audio Message

  @C129782 @audiomessage @staging
  Scenario Outline: Verify you can send and play the audio file in 1:1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send audio file with length <Time> and name <File> to the current conversation
    And I wait until audio <File> is uploaded completely
    And I click play button of audio <File> in the conversation view
    Then I wait until audio <File> is downloaded and starts to play
    And I verify time for audio <File> is changing in the conversation view
    And I verify seek bar is shown for audio <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | example.wav | 00:20 |

  @C129783 @audiomessage @staging
  Scenario Outline: Verify you can delete an audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And I see file transfer button in conversation input
    And I send audio file with length <Time> and name <File> to the current conversation
    And I wait until audio <File> is uploaded completely
    And I click to delete the latest message
    And I click confirm to delete message
    Then I do not see audio message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File         | Time  |
      | user1Email | user1Password | user1Name | user2Name | example2.wav | 00:03 |

  @C129784 @audiomessage @staging
  Scenario Outline: Verify you can receive and play the audio file in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I see file transfer button in conversation input
    And <Contact1> sends audio file <File> via device Device1 to group conversation <ChatName>
    Then I wait until audio <File> is uploaded completely
    # Please uncomment if we find a way to send audio meta-data through SE-Bridge
    #And I verify time for audio <File> is <Time> in the conversation view
    When I click play button of audio <File> in the conversation view
    And I wait until audio <File> is downloaded and starts to play
    Then I verify time for audio <File> is changing in the conversation view
    And I verify seek bar is shown for audio <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | AudioMsg | example.m4a | 00:20 |