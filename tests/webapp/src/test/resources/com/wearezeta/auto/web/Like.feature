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
    Given Myself is connected to <Contact1>, <Contact2>
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
    Given I am signed in properly
    When I open conversation with <Contact>
    When User <Contact> sends image <ImageName> to single user conversation Myself
    And I see sent picture <ImageName> in the conversation view
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

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
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

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
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | C226430.mp4 | 15MB  |

  @C226431 @staging
  Scenario Outline: Verify liking someone's Soundcloud, youtube, vimeo and spotify
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C226433 @staging
  Scenario Outline: Verify liking someone's shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C226434 @staging
  Scenario Outline: Verify liking someone's gif from GIPHY
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

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
    And I do not see likes below the latest message
    When I click to like the latest message without other likes
    And I wait for 5 seconds
    And I see likes below the latest message
    And I fail the test

    Examples:
      | Login      | Password      | Name      | Contact   | Latitude | Longitude | LocationName |
      | user1Email | user1Password | user1Name | user2Name | 12.94    | 54.29     | Stralsund    |

