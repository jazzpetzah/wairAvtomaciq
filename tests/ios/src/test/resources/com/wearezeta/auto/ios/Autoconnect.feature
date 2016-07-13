Feature: Autoconnect

  @torun @C2034 @C2035 @real @noAcceptAlert
  Scenario Outline: Verify autoconnect users by phones and mail
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I accept alert
    And I click clear button
    Then I see conversation AutoconnectUser2 in conversations list
    And I see conversation AutoconnectUser3 in conversations list

    Examples:
      | Name      | AutoconnectUser2 | AutoconnectUser3 |
      | user1Name | AutoconnectUser2 | AutoconnectUser3 |