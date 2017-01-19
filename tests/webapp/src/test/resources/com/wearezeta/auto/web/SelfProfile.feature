Feature: Self Profile

  @C1728 @regression
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open preferences by clicking the gear button
    And I see name <Name> in account preferences
    And I change name to <NewName>
    Then I see name <NewName> in account preferences
    When I refresh page
    And I am signed in properly
    And I open preferences by clicking the gear button
    Then I see name <NewName> in account preferences

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewUserName | user2Name |

  @C1729 @regression
  Scenario Outline: Verify you can access your profile information
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open preferences by clicking the gear button
    Then I see name <Name> in account preferences
    And I see user email <Email> in account preferences
    And I see user phone number <PhoneNumber> in account preferences

    Examples: 
      | Email      | Password      | Name      | PhoneNumber      |
      | user1Email | user1Password | user1Name | user1PhoneNumber |

  @C1731 @regression
  Scenario Outline: Verify correct accent color showing after sign out and sign in
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open preferences by clicking the gear button
    When I set my accent color to <ColorName>
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    When I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open preferences by clicking the gear button
    Then I verify my accent color in color picker is set to <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName    |
      | user1Email | user1Password | user1Name | BrightOrange |

  @C1732 @mute
  Scenario Outline: Verify you can edit your profile picture by dragging a new photo
    Given My browser supports synthetic drag and drop
    Given There is 1 user where <Name> is me
    Given Myself take snapshot of current profile picture
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open preferences by clicking the gear button
    And I drop picture <PictureName> to account preferences
    Then I verify that current profile picture snapshot of Myself differs from the previous one

    Examples: 
      | Login      | Password      | Name      | PictureName              |
      | user1Email | user1Password | user1Name | userpicture_portrait.jpg |

  @C3266 @regression
  Scenario Outline: Verify you can change your profile picture
    Given There is 1 user where <Name> is me
    Given Myself take snapshot of current profile picture
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open preferences by clicking the gear button
    # Wait for the background image to be loaded
    And I wait for 10 seconds
    And I remember the background image of the conversation list
    And I remember the profile image on the account page
    Then I verify that the background image of the conversation list has not changed
    And I verify that the profile image on the account page has not changed
    When I upload picture <PictureName> to account preferences
    Then I verify that the background image of the conversation list has changed
    And I verify that the profile image on the account page has changed
    And I verify that current profile picture snapshot of Myself differs from the previous one

    Examples: 
      | Login      | Password      | Name      | PictureName              |
      | user1Email | user1Password | user1Name | userpicture_portrait.jpg |

  @C1730 @regression
  Scenario Outline: Verify you can change your accent color
    Given There is 4 users where <Name> is me
    Given User me change accent color to <ColorName>
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I see Contact list with name <Contact1>
    And I open preferences by clicking the gear button
    Then I verify my accent color in color picker is set to <ColorName> color
    And I verify my avatar background color is set to <ColorName> color
    And I close preferences
    When I open conversation with <Contact3>
    When User <Contact1> pinged in the conversation with <Name>
    And Contact <Contact2> sends message Msg1 to user <Name>
    Then I verify ping icon in conversation with <Contact1> has <ColorName> color
    And I verify unread dot in conversation with <Contact2> has <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName | Contact1  | Contact2  | Contact3  |
      | user1Email | user1Password | user1Name | SoftPink  | user2Name | user3Name | user4Name |
