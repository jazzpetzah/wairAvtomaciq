Feature: Self Profile

  @id2264 @staging
  Scenario Outline: ZClient change name in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When  I tap on my avatar
    And I see personal info page
    And I wait for 10 seconds
    And I tap on my name
    And I change <Name> to <NewName>
    Then I see my new name <NewName>

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewTestName | user2Name |

  @id2250 @staging 
  Scenario Outline: ZClient change name in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When  I tap on my avatar
    And I see personal info page
    And I wait for 10 seconds
    And I tap on my name
    And I change <Name> to <NewName>
    Then I see my new name <NewName>

    Examples: 
      | Login      | Password      | Name      | NewName     | Contact   |
      | user1Email | user1Password | user1Name | NewTestName | user2Name |

  @id2261 @staging
  Scenario Outline: Check contact personal info in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <Contact>
    And I see tablet dialog page
    And I tap on profile button
    Then I see <Contact> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2247 @staging
  Scenario Outline: Check contact personal info in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <Contact>
    And I see tablet dialog page
    And I tap on profile button
    Then I see <Contact> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2288 @staging
  Scenario Outline: Change user picture in portrait mode
    Given There is 1 user where <Name> is me
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on my avatar
    And I tap on tablet personal info screen
    And I remember my current profile picture
    And I tap change photo button
    And I press Gallery button
    And I select picture
    And I press Confirm button
    And I tap on tablet personal info screen
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id2289 @staging
  Scenario Outline: Change user picture in landscape mode
    Given There is 1 user where <Name> is me
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on my avatar
    And I tap on tablet personal info screen
    And I remember my current profile picture
    And I tap change photo button
    And I press Gallery button
    And I select picture
    And I press Confirm button
    And I tap on tablet personal info screen
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
