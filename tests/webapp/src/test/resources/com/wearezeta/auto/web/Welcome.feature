Feature: Welcome

  @C1734 @regression @useSpecialEmail
  Scenario Outline: Verify possibility of reseting password
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    Given I remember current page
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
    Given I navigate to previously remembered page
    When I see Sign In page
    When I enter email "<Email>"
    And I enter password "<OldPassword>"
    And I press Sign In button
    Then I see login error "<LoginErr>"
    When I enter email "<Email>"
    And I enter password "<NewPassword>"
    And I check option to remember me
    And I press Sign In button
    Then I am signed in properly
    When I open preferences by clicking the gear button
    Then I see username <Name> in account preferences
    And I see user email <Email> in account preferences

    Examples: 
      | Email      | OldPassword   | Name      | NewPassword | LoginErr                                  |
      | user1Email | user1Password | user1Name | aqa654321#  | Please verify your details and try again. |