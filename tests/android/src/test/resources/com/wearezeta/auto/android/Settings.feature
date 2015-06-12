Feature: Settings

  @id67 @id68 @regression
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list
    When I tap on my avatar
    And I tap options button
    And I tap settings button
    And I wait for 2 seconds
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples: 
      | Name      |
      | user1Name |

  @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list
    When I tap on my avatar
    And I tap on my name
    And I tap options button
    And I tap about button
    Then I do not see About page

    Examples: 
      | Name      |
      | user1Name |

  @id90 @id91 @id92 @regression
  Scenario Outline: Open Close About page from Settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list
    When I tap on my avatar
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples: 
      | Name      |
      | user1Name |
