Feature: Autoconnect

  #@C2034 @C2035 @real @noAcceptAlert
  #Scenario Outline: Verify autoconnect users by direct match phone numbers
    #Given I see sign in screen
    #When I enter phone number for <Name>
    #And I enter activation code
    #And I accept terms of service
    #And I input name <Name> and hit Enter
    #And I press Keep This One button
    #And I tap Share Contacts button on Share Contacts overlay
    #Given I see conversations list
    #Given There are 1 user where <Name> is me
    #Given I sign in using my email or phone number
    #Given I see conversations list
    #those steps will be needed again when with every install of the app
    #the share your contacts alert will be shown again and you can accept or deny to share
    #instead of its uploading it automatically
    #When I open search UI
    #And I accept alert
    #And I click clear button
    #Then I see conversation AutoconnectUser2 in conversations list
    #And I see conversation AutoconnectUser3 in conversations list

    #Examples:
      #| Name      |
      #| user1Name |

  @C2034 @C2035 @noAcceptAlert
  Scenario Outline: Verify autoconnect users by direct match phone numbers
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given User Myself has phone numbers <PhonePrefix><APhone>,<PhonePrefix><A2Phone> in address book
    Given I see conversations list
    Then I see conversation <AName> in conversations list
    And I see conversation <A2Name> in conversations list

    Examples:
      | Name      | APhone     | PhonePrefix | A2Phone    | AName            | A2Name           |
      | user1Name | 1722036230 | +49         | 1622360109 | AutoconnectUser2 | AutoconnectUser3 |

  @torun @CTODO
  Scenario Outline: Verify direct matching - email
    Given There are 2 users where <UserA> is me
    Given I sign in using my email
    Given User <UserB> has email <UserA> in address book
    When I open search UI
    And I wait for 10 seconds
    And I input in People picker search field first 3 letters of user name <UserB>
    Then I see the conversation "<UserB>" exists in Search results

    Examples:
      | UserA     | UserB     | Message          |
      | user1Name | user2Name | No spec for that |