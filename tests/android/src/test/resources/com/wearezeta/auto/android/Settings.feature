Feature: Settings

  @id67 @id68 @regression
  Scenario Outline: Open and Close settings page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on my name
    Then Settings button is unreachable

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id90 @id91 @id92 @regression
  Scenario Outline: Open Close About page from Settings page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2020 @regression
  Scenario Outline: Verify possibility of reseting password
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I press Sign in button
    And I hide keyboard
    And I press FORGOT PASSWORD button
    And I request reset password for <Email>
    Then I reset <Name> password by URL to new <NewPassword>
    And I restore the application
    And I press Sign in button
    And I have entered login <Email>
    And I have entered password <NewPassword>
    And I press Log in button
    Then Contact list appears with my name <Name>

    Examples: 
      | Email      | Password      | Name      | NewPassword | Contact   |
      | user1Email | user1Password | user1Name | qatest1234  | user2Name |
