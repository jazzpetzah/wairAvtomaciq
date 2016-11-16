Feature: Stability

  @stability
  Scenario Outline: Register new user by phone and use default picture 1
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to keep the current picture
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |

  @stability
  Scenario Outline: Register new user by phone and use default picture 2
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to keep the current picture
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |

  @stability
  Scenario Outline: Register new user by phone and use default picture 3
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to keep the current picture
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |

  @stability
  Scenario Outline: Register new user by phone and use default picture 4
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    And I select to keep the current picture
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |
      | user1Name |
