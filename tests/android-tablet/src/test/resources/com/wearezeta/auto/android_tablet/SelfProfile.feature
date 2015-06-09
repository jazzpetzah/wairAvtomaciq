Feature: Self Profile

  @id2264 @smoke
  Scenario Outline: ZClient change name in portrait mode
    Given There is 1 user where <Name> is me
    And I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples: 
      | Name      | NewName     |
      | user1Name | NewTestName |

  @id2250 @smoke
  Scenario Outline: ZClient change name in landscape mode
    Given There is 1 user where <Name> is me
    And I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples: 
      | Name      | NewName     |
      | user1Name | NewTestName |

  @id2261 @smoke
  Scenario Outline: Check contact personal info in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    And I see the user email <ContactEmail> on Single user popover

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @id2247 @smoke
  Scenario Outline: Check contact personal info in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    And I see the user email <ContactEmail> on Single user popover

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

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
