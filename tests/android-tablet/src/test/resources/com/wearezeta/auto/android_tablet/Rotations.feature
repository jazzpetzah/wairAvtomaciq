Feature: Rotations

  @id2185 @staging
  Scenario Outline: Self profile
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    # We try to set the self name to make sure this action is really visible and available
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page
    When I rotate UI to landscape
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page
    When I rotate UI to portrait
    And I tap my name field on Self Profile page
    And I change my name to <Name> on Self Profile page
    Then I see my name on Self Profile page

    Examples: 
      | Name      |
      | user1Name |

