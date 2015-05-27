Feature: Self Profile

  @smoke @id1743
  Scenario Outline: I can change my name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
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

  @smoke @id1744
  Scenario Outline: Verify you can access your profile information
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    Then I see user name on self profile page <Name>
    And I see user email on self profile page <Email>
    And I see user phone number on self profile page <PhoneNumber>

    Examples: 
      | Email      | Password      | Name      | PhoneNumber      |
      | user1Email | user1Password | user1Name | user1PhoneNumber |

  @smoke @id1753
  Scenario Outline: Verify correct accent color showing after sign out and sign in
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    And I set my accent color to <ColorName>
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    Then I verify my accent color in color picker is set to <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName    |
      | user1Email | user1Password | user1Name | BrightOrange |

  @regression @id1755
  Scenario Outline: Verify you can edit your profile picture by dragging a new photo
    Given My browser supports synthetic drag and drop
    Given There is 1 user where <Name> is me
    Given Myself take snapshot of current profile picture
    Given I Sign in using login <Login> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    And I click camera button
    And I see profile picture dialog
    And I drop <PictureName> to profile picture dialog
    And I confirm picture selection on profile picture dialog
    Then I do not see profile picture dialog
    Then I verify that current profile picture snapshot of Myself differs from the previous one

    Examples: 
      | Login      | Password      | Name      | PictureName              |
      | user1Email | user1Password | user1Name | userpicture_portrait.jpg |

  @torun @id1747
  Scenario Outline: Verify you can change your accent color
    Given There is 3 users where <Name> is me
    Given User me change accent color to <ColorName>
    Given Myself is connected to <Contact1>, <Contact2>
    Given <Contact1> pinged the conversation with <Name>
    Given User <Contact2> sent message <Msg1> to conversation <Name>
    When I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    Then I verify my accent color in color picker is set to <ColorName> color
    And I verify  my avatar background color is set to <ColorName> color
    #Then I verify my accent color in color picker is set to <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | SoftPink  | user2Name | user3Name |
