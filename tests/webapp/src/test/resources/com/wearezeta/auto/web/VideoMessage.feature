Feature: Video Message

  @C123927 @videomessage @regression
  Scenario Outline: Verify sender can play video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I disable ad banner
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I skip if my browser does not support inline video messages
    And I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify time for video <File> is changing in the conversation view
    And I verify seek bar is shown for video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 20MB  |

  @C123938 @videomessage @regression
  Scenario Outline: Verify user can delete video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I click to delete the latest message
    And I click confirm to delete message
    Then I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 5MB  |

  @C123926 @videomessage @staging
  Scenario Outline: Verify receiver can play video message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I see Contact list with name <Contact>
    And I open conversation with <Contact>
    And <Contact> sends <Size> sized video with name <File> via device Device1 to user <Name>
    Then I see video message <File> in the conversation view
    And I wait until video <File> is downloaded and starts to play
    When I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify seek bar is shown for video <File> in the conversation view
    And I verify time for video <File> is changing in the conversation view
    When I click pause button of video <File> in the conversation view
    Then I see play button of video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 15MB  |

  @C123939 @videomessage @staging
  Scenario Outline: Verify receivers can play video message in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And <Contact1> sends <Size> sized video with name <File> via device Device1 to group conversation <ChatName>
    Then I see video message <File> in the conversation view
    And I wait until video <File> is downloaded and starts to play
    When I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify seek bar is shown for video <File> in the conversation view
    And I verify time for video example.mp4 is changing in the conversation view
    When I click pause button of video <File> in the conversation view
    Then I see play button of video <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File        | ChatName  | Size  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | example.mp4 | GroupChat | 15MB  |

  @C123929 @videomessage @staging
  Scenario Outline: Verify sender can cancel video message upload
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And I see file transfer button in conversation input
    And I send <Size> sized video with name <File> to the current conversation
    And I see cancel upload button for video <File>
    Then I cancel video upload of video <File>
    And I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 20MB  |

  @C123928 @videomessage @staging
  Scenario Outline: Verify receiver can cancel video message download
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And <Contact1> sends <Size> sized video with name <File> via device Device1 to group conversation <ChatName>
    Then I see video message <File> in the conversation view
    When I click play button of video <File> in the conversation view
    #Then I wait until video <File> is downloaded and starts to play
    Then I cancel video download of video <File>
    And I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File        | ChatName  | Size  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | example.mp4 | GroupChat | 15MB  |