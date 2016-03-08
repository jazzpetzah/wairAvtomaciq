Feature: Upgrade

  @C1 @upgrade @torun
  Scenario Outline: Upgrade
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
#    Given I upgrade Wire to the recent version
#    And I see conversations list

#    When I tap on contact name <Contact>
#    And I type the default message and send it
#    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
