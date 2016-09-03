Feature: Like

  @C226471 @staging
  Scenario Outline: Verify you can like someone's message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | like me  |

  @C226472 @staging
  Scenario Outline: Verify you can like someone's message in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <Message1> via device Device1 to group conversation <ChatName>
    Then I see text message <Message1>
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GC1      | like me  |

  @C226427 @staging
  Scenario Outline: Verify liking someone's link preview in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Link> via device Device1 to user me
    Then I see link <LinkInPreview> in link preview message
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   | Link     | LinkInPreview    |
      | user1Email | user1Password | user1Name | user2Name | wire.com | https://wire.com |

  @C226428 @staging
  Scenario Outline: Verify liking someone's picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When User <Contact> sends image <ImageName> to single user conversation <Name>
    And I see sent picture <ImageName> in the conversation view
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | userpicture_portrait.jpg |

  @C226429 @staging
  Scenario Outline: Verify liking someone's audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And <Contact> sends audio file <File> via device Device1 to user Myself
    Then I wait until audio <File> is uploaded completely
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | File        |
      | user1Email | user1Password | user1Name | user2Name | example.m4a |

  @C226430 @staging
  Scenario Outline: Verify liking someone's video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And <Contact> sends <Size> sized video with name <File> via device Device1 to user me
    Then I see video message <File> in the conversation view
    And I wait until video <File> is uploaded completely
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C226430.mp4 | 15MB  |

  @C226431 @staging @WEBAPP-3040
  Scenario Outline: Verify liking someone's Soundcloud, youtube, vimeo and spotify
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    When Contact <Contact> sends message <Youtubelink> via device Device1 to user me
    Then I see embedded youtube message <Youtubelink>
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message
    When Contact <Contact> sends message <Soundcloudlink> via device Device1 to user me
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message
    When Contact <Contact> sends message <Vimeolink> via device Device1 to user me
    Then I see text message <Vimeolink>
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message
    When Contact <Contact> sends message <Spotifylink> via device Device1 to user me
    Then I see text message <Spotifylink>
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | Youtubelink                                 | Soundcloudlink                                                      | Vimeolink                 | Spotifylink                                           |
      | user1Email | user1Password | user1Name | user2Name | https://www.youtube.com/watch?v=ncHd3sxpEbo | https://soundcloud.com/nour-moukhtar/ludwig-van-beethoven-fur-elise | https://vimeo.com/7265982 | https://play.spotify.com/album/7buEcyw6fJF3WPgr06BomH |

  @C226433 @staging
  Scenario Outline: Verify liking someone's shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    When <Contact> sends <Size> sized file with name <File> via device Device1 to user <Name>
    When I wait until placeholder for file <File> disappears
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size | Type |
      | user1Email | user1Password | user1Name | user2Name | C87933.txt | 15MB | TXT  |

  @C226434 @staging
  Scenario Outline: Verify liking gif from GIPHY
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I am signed in properly
    When I open conversation with <Contact>
    When I write message <Message>
    And I click GIF button
    Then I see Giphy popup
    And I see gif image in Giphy popup
    When I click send button in Giphy popup
    Then I see sent gif in the conversation view
    And I verify the second last text message equals to <ExpectedMessage>
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | Message | ExpectedMessage     |
      | user1Email | user1Password | user1Name | user2Name | cat     | cat • via giphy.com |

  @C226435 @staging
  Scenario Outline: Verify liking someone's location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And User <Contact> sends location <LocationName> with <Latitude> and <Longitude> to user Myself via device Device1
    Then I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
# No likes
    And I do not see likes below the latest message
# Only liked by me
    When I click to like the latest message without other likes
    And I do not see likes below the latest message
    Then I see the latest message is only liked by me
# Liked by others and me
    When User <Contact> likes the recent message from user <Name> via device Device1
    And I see likes below the latest message
    And I see the latest message is liked by others and me
# Only liked by others
    When I click to unlike the latest message with other likes
    Then I see likes below the latest message
    And I see the latest message is only liked by others
# Everything unliked
    When User <Contact> unlikes the recent message from user <Name> via device Device1
    And I do not see likes below the latest message
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Latitude | Longitude | LocationName |
      | user1Email | user1Password | user1Name | user2Name | 12.94    | 54.29     | Stralsund    |

  @C234612 @staging
  Scenario Outline: Verify locally deleted message can be liked by others
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <Message1> via device Device1 to user me
    Then I see text message <Message1>
    And I see 2 messages in conversation
    And I do not see likes below the latest message
    When I click context menu of the latest message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    Then I do not see text message <Message1>
    And I see 1 messages in conversation
    When User <Contact> likes the recent message from user <Name> via device Device1
    Then I do not see likes below the latest message
    And I see 1 messages in conversation
    And I verify browser log does not have errors

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | like me  |
