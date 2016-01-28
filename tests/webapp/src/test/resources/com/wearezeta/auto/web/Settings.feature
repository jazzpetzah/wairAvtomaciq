Feature: Menus

  @C1706 @regression
  Scenario Outline: Check Preferences opening
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I see Settings dialog

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1773 @smoke
  Scenario Outline: Verify sound settings are saved after re-login
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I see Settings dialog
    When I select Sound Alerts setting to be None
    Then I see Sound Alerts setting is set to None
    And I click close settings page button
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    Given I see Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I see Settings dialog
    Then I see Sound Alerts setting is set to None
    When I select Sound Alerts setting to be Some
    Then I see Sound Alerts setting is set to Some
    And I click close settings page button
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    Given I see Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    When I see Settings dialog
    Then I see Sound Alerts setting is set to Some

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C12064 @mute
  Scenario Outline: Verify you can delete account
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I click gear button on self profile page
    When I select Settings menu item on self profile page
    And I see Settings dialog
    And I click delete account button on settings page
    And I see email <Email> in delete info text on settings page
    And I click cancel deletion button on settings page
    And I click delete account button on settings page
    #And I see email <Email> in delete info text on settings page
    And I wait for 30 seconds
    #And I click send button to delete account
    And I delete account of user <Name> via email
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>

    Examples:
      | Email      | Password      | Name      | Error                                     |
      | user1Email | user1Password | user1Name | Please verify your details and try again. |