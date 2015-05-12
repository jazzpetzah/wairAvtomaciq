Feature: Welcome

  @staging @id1777
  Scenario Outline: Verify possibility of reseting password
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I click Change Password button
    Then I see Password Change Request page
    When I enter email <Email> on Password Change Request page
    And Myself starts listening for password change confirmation
    And I click Change Password button on Password Change Request page
    Then I see Password Change Request Succeeded page
    When I open Password Change link from the received email
    Then I see Password Change page
    When I enter password <NewPassword> on Password Change page
    And I click Change Password button on Password Change page
    Then I see Password Change Succeeded page
    Given I switch to sign in page
    When I enter email <Email>
    And I enter password <OldPassword>
    And I press Sign In button
    Then I see login error "<LoginErr>"
    Given I switch to sign in page
    When I enter email <Email>
    And I enter password <NewPassword>
    And I press Sign In button
    And I see Contacts Upload dialog

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                   |
      | user1Email | user1Password | user1Name | aqa654321#  | WRONG EMAIL OR PASSWORD. PLEASE TRY AGAIN. |
