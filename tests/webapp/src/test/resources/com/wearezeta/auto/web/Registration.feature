Feature: Registration

  @smoke @id1936
  Scenario Outline: Verify new user can be registered
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password <Password> on Registration page
    And I start activation email monitoring
    And I submit registration form
    Then I see email <Email> on Verification page
    When I activate user by URL
    And User <Name> is Me
    # This has to be done automatically at some time
    And I Sign in using login <Email> and password <Password>
    Then I see my name on top of Contact list
    When I open self profile
    Then I see user name on self profile page <Name>
    Then I see user email on self profile page <Email>
	
    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  # Moved to staging because of https://wearezeta.atlassian.net/browse/WEBAPP-1020
  @staging @id2064
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
    Then I see my name on top of Contact list

    Examples: 
      | Login      | Password      | Name      | PictureName               |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg |

  # Moved to staging because of https://wearezeta.atlassian.net/browse/WEBAPP-1020
  @staging @id2065
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
    Then I see my name on top of Contact list

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
   