Feature: Unique Usernames

  @C352028 @staging
  Scenario Outline: Verify autogeneration of a username for a user with latin characters only
    Given I see sign in screen
    Given I enter phone number for <Name>
    Given I enter activation code
    Given I accept terms of service
    Given I input name <Name> and commit it
    Given I accept alert if visible
    Given I tap Keep This One button
    Given I tap Not Now button on Share Contacts overlay
    # Wait until takeover screen appears
    Given I wait for 7 seconds
    When I see username <WireName> on Unique Username Takeover page
    Then I see unique username <ExpectedUniqueName> on Unique Username Takeover page
    When I tap Keep This One button on Unique Username Takeover page
    Then I see conversations list

    Examples:
      | Name      | ExpectedUniqueName   |
      | user1Name | @user1UniqueUsername |

  @C352039 @staging @fastLogin
  Scenario Outline: Verify impossibility to save incorrect username
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap settings gear button
    Given I select settings item Account
    Given I select settings item @name
    When I enter "<Empty>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    And I enter "<MinChars>" name on Unique Username page
    Then I see Save button state is Disabled on Unique Username page
    When I tap Save button on Unique Username page
    And I attempt to enter over max allowed <MaxChars> chars as name on Unique Username page
    Then I see that name length is less than <MaxChars> chars on Unique Username page
    And I type unique usernames from the data table and verify they cannot be committed on Unique Username page
      | Charset      | Chars  |
      | Cyrillic     | МоёИмя |
      | Arabic       | اسمي   |
      | Chinese      | 我的名字|
      | SpecialChars | %^&@#$ |

    Examples:
      | Name      | Empty | MinChars | MaxChars |
      | user1Name | ""    | 1        | 22       |