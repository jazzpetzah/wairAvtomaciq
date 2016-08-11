Feature: Autoconnect

  @C2034 @C2035 @real @noAcceptAlert
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    #those steps will be needed again when with every install of the app
    #the share your contacts alert will be shown again and you can accept or deny to share
    #instead of its uploading it automatically
    #When I open search UI
    #And I accept alert
    #And I click clear button
    Then I see conversation AutoconnectUser2 in conversations list
    And I see conversation AutoconnectUser3 in conversations list

    Examples:
      | Name      |
      | user1Name |