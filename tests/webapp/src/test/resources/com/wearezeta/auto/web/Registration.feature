Feature: Registration

  @smoke @id1936
  Scenario Outline: Verify new user can be registered
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I start activation email monitoring
    And I submit registration form
    Then I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me without avatar
    And I see Self Picture Upload dialog
    And I force carousel mode on Self Picture Upload dialog
    And I select random picture from carousel on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my name on top of Contact list
    When I open self profile
    Then I see user name on self profile page <Name>
    Then I see user email on self profile page <Email>
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id2064
  Scenario Outline: Photo selection dialogue - choose picture from library
    Given There is 1 user where <Name> is me without avatar picture
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I choose <PictureName> as my self picture on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my name on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    When I Sign in using login <Login> and password <Password>
    Then I do not see Self Picture Upload dialog

    Examples: 
      | Login      | Password      | Name      | PictureName               |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg |

  @regression @id2065
  Scenario Outline: Photo selection dialogue - choose picture from carousel
    Given There is 1 user where <Name> is me without avatar picture
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I force carousel mode on Self Picture Upload dialog
    And I select random picture from carousel on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    Then I see my name on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    And I see Sign In page
    When I Sign in using login <Login> and password <Password>
    Then I do not see Self Picture Upload dialog

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @regression @id1991
  Scenario Outline: I want to be notified if the email address I entered during registration has already been registered
    Given There is 1 user where user1Name is me without avatar picture
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <UsedEmail> on Registration page
    And I enter user password "<NewPassword>" on Registration page
    And I submit registration form
    Then I see error "EMAIL ADDRESS ALREADY TAKEN" on Verification page
    And I verify that a red dot is shown inside the email field on the registration form
    When I enter user email <UnusedEmail> on Registration page
    Then I verify that a red dot is not shown inside the email field on the registration form
    When I submit registration form
    Then I see email <UnusedEmail> on Verification page

    Examples: 
      | Name      | UsedEmail  | UnusedEmail | Password      |
      | user1Name | user1Email | user2Email  | user2Password |

  @regression @id1992
  Scenario Outline: I want to see an error screen if the registration fails
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password "<Password>" on Registration page
    And I submit registration form
    Then I see error "SORRY. THIS EMAIL ADDRESS IS FORBIDDEN." on Verification page
    And I verify that a red dot is shown inside the email field on the registration form

    Examples: 
      | Name      | Email              | Password      |
      | user1Name | nope@wearezeta.com | user1Password |

  @staging @id2229
  Scenario: Use Gmail contacts import on registration
    Given There is 1 user where user1Name is me without avatar picture
    Given I Sign in using login user1Email and password user1Password
    Given I see Self Picture Upload dialog
    Given I choose userpicture_landscape.jpg as my self picture on Self Picture Upload dialog
    Given I confirm picture selection on Self Picture Upload dialog
    When I see Contacts Upload dialog
    And I click button to import Gmail Contacts
    And I see Google login popup
    And I sign up at Google with email smoketester.wire@gmail.com and password aqa123456
    Then I see more than 5 suggestions in people picker

  @regression @id2051
  Scenario Outline: Register using already registered but not verified yet email
    Given I switch to Registration page
    Given I enter user name <Name> on Registration page
    Given I enter user email <Email> on Registration page
    Given I enter user password "<Password>" on Registration page
    Given I submit registration form
    And I see email <Email> on Verification page
    When I Sign in using login <Email> and password <Password>
    Then I see email <Email> on Verification page

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
   