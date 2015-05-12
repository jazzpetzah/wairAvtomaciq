Feature: Sign In

  @smoke @id1788
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I see Sign In page
    When I enter email <Email>
    And I enter password <Password>
    And I press Sign In button
    Then I am signed in properly
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my name on top of Contact list

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id1792
  Scenario Outline: Verify sign in error appearance in case of wrong credentials
    Given There is 1 user where user1Name is me
    Given I switch to sign in page
    When I enter email <Email>
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    And a red dot is shown inside the email field on the sign in form
    And a red dot is shown inside the password field on the sign in form

    Examples: 
      | Email      | Password | Error                                      |
      | user1Email |          | WRONG EMAIL OR PASSWORD. PLEASE TRY AGAIN. |
      | user1Email | wrong    | WRONG EMAIL OR PASSWORD. PLEASE TRY AGAIN. |
