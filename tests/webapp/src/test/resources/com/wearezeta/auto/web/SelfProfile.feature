Feature: Self Profile

  @C1728 @regression
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open self profile
    And I see user name on self profile page <Name>
    And I change username to <NewName>
    Then I see user name on self profile page <NewName>
    And I see my avatar on top of Contact list

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewUserName | user2Name |

  @C1729 @regression
  Scenario Outline: Verify you can access your profile information
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see user name on self profile page <Name>
    And I see user email on self profile page <Email>
    And I see user phone number on self profile page <PhoneNumber>

    Examples: 
      | Email      | Password      | Name      | PhoneNumber      |
      | user1Email | user1Password | user1Name | user1PhoneNumber |

  @C1731 @regression
  Scenario Outline: Verify correct accent color showing after sign out and sign in
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I set my accent color to <ColorName>
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
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
    And I drop picture <PictureName> to self profile
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
    And I upload picture <PictureName> to self profile
    Then I verify that current profile picture snapshot of Myself differs from the previous one

    Examples: 
      | Login      | Password      | Name      | PictureName              |
      | user1Email | user1Password | user1Name | userpicture_portrait.jpg |

  @C1730 @regression
  Scenario Outline: Verify you can change your accent color
    Given There is 3 users where <Name> is me
    Given User me change accent color to <ColorName>
    Given Myself is connected to <Contact1>, <Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open self profile
    Then I verify my accent color in color picker is set to <ColorName> color
    And I verify my avatar background color is set to <ColorName> color
    When User <Contact1> pinged in the conversation with <Name>
    And User <Contact2> sent message <Msg1> to conversation <Name>
    Then I verify ping icon in conversation with <Contact1> has <ColorName> color
    And I verify unread dot in conversation with <Contact2> has <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | SoftPink  | user2Name | user3Name |