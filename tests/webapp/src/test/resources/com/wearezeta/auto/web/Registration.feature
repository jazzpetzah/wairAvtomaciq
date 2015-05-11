Feature: Registration

  @smoke @id1936
  Scenario Outline: Verify new user can be registered
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password <Password> on Registration page
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

  @staging @id1992
  Scenario: I want to see an error screen if the registration fails - Something went wrong
    Given I switch to Registration page
    When I enter user name user1Name on Registration page
    And I enter user email nope@wearezeta.com on Registration page
    And I enter user password user1Password on Registration page
    And I submit registration form
    Then I see error "SORRY. THIS EMAIL ADDRESS IS FORBIDDEN." on Verification page
