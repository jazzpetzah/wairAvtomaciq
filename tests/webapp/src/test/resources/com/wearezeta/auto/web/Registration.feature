Feature: Registration

  @smoke @id1936
  Scenario Outline: Verify new user can be registered
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I start activation email monitoring
    And I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me without avatar
    And I see Self Picture Upload dialog
    And I force carousel mode on Self Picture Upload dialog
    And I select random picture from carousel on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my avatar on top of Contact list
    When I open self profile
    Then I see user name on self profile page <Name>
    Then I see user email on self profile page <Email>
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id2064
  Scenario Outline: Photo selection dialogue - choose picture from library
    Given There is 1 user where <Name> is me without avatar picture
    Given I switch to Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I choose <PictureName> as my self picture on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my avatar on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I see Sign In page
    When I Sign in using login <Login> and password <Password>
    Then I do not see Self Picture Upload dialog

    Examples: 
      | Login      | Password      | Name      | PictureName               |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg |

  @regression @id2065
  Scenario Outline: Photo selection dialogue - choose picture from carousel
    Given There is 1 user where <Name> is me without avatar picture
    Given I switch to Sign In page
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I force carousel mode on Self Picture Upload dialog
    And I select random picture from carousel on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my avatar on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I see Sign In page
    When I Sign in using login <Login> and password <Password>
    Then I do not see Self Picture Upload dialog

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id1991
  Scenario Outline: I want to be notified if the email address I entered during registration has already been registered
    Given There is 1 user where user1Name is me without avatar picture
    When I enter user name <Name> on Registration page
    And I enter user email <UsedEmail> on Registration page
    And I enter user password "<NewPassword>" on Registration page
    And I submit registration form
    Then I see error "Email address already taken" on Verification page
    And I verify that the email field on the registration form is marked as error
    When I enter user email <UnusedEmail> on Registration page
    Then I verify that the email field on the registration form is not marked as error
    When I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <UnusedEmail> on Verification page

    Examples: 
      | Name      | UsedEmail  | UnusedEmail | Password      |
      | user1Name | user1Email | user2Email  | user2Password |

  @smoke @id1992
  Scenario Outline: I want to see an error screen if the registration fails
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I submit registration form
    Then I verify that the email field on the registration form is marked as error
    And I see error "Sorry. This email address is forbidden." on Verification page

    Examples: 
      | Name      | Email              | Password      |
      | user1Name | nope@wearezeta.com | user1Password |

  @regression @id2229
  Scenario: Use Gmail contacts import on registration
    Given There is 1 user where user1Name is me without avatar picture
    Given I switch to Sign In page
    Given I Sign in using login user1Email and password user1Password
    Given I see Self Picture Upload dialog
    Given I force carousel mode on Self Picture Upload dialog
    Given I confirm picture selection on Self Picture Upload dialog
    When I see Contacts Upload dialog
    And I click button to import Gmail Contacts
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456
    Then I see more than 5 suggestions in people picker

  @regression @id2051
  Scenario Outline: Register using already registered but not verified yet email
    Given I enter user name <Name> on Registration page
    Given I enter user email <Email> on Registration page
    Given I enter user password "<Password>" on Registration page
    When I submit registration form
    Then I verify that an envelope icon is shown
    And I see email <Email> on Verification page
    And I open Sign In page
    When I see Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I verify that an envelope icon is shown
    And I see email <Email> on pending page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id1935
  Scenario Outline: Verify that correct error messages are shown instead of email verification screen if there are some problems with the registration
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I submit registration form
    Then I verify that the email field on the registration form is marked as error
    And I see error "Not a valid email address." on Verification page

    Examples: 
      | Email        | Password      | Name      |
      | @example.com | user1Password | user1Name |
      | example@     | user1Password | user1Name |
      | @            | user1Password | user1Name |
      | @            | 12            | user1Name |