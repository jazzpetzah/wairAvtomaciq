Feature: Self Profile

  @C679 @id205 @regression @rc @rc42
  Scenario Outline: Change user picture with gallery image
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I tap on personal info screen
    And I remember my current profile picture
    And I tap change photo button
    And I press Gallery button
    And I press Confirm button
    And I tap on personal info screen
    Then I verify that my current profile picture is different from the previous one

    Examples:
      | Name      |
      | user1Name |

  @C689 @id325 @regression @rc
  Scenario Outline: Check contact personal info in one to one conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap conversation details button
    Then I see <Contact> user name and email

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C691 @id328 @regression @rc @rc42
  Scenario Outline: I can change my name
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I see personal info page
    And I tap on my name
    Then I see edit name field with my name
    When I clear name field
    And I change my name to <NewName>
    Then I see my new name <NewName>

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C678 @id201 @regression @rc
  Scenario Outline: Change user picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap on my avatar
    # Wait until self profile image is loaded into UI
    And I wait for 20 seconds
    And I tap on personal info screen
    And I remember my current profile picture
    And I tap change photo button
    And I take new avatar picture
    And I press Confirm button
    And I tap on personal info screen
    Then I verify that my current profile picture is different from the previous one

    Examples:
      | Name      |
      | user1Name |

  @C450 @id4069 @regression
  Scenario Outline: Verify I can switch dark/white theme from self profile
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    Then I remember the conversation view
    When I navigate back from dialog page
    And I tap on my avatar
    And I see personal info page
    And I tap Light Bulb button
    And I close Personal Info Page
    When I tap on contact name <Contact>
    And I see dialog page
    And I scroll to the bottom of conversation view
    Then I see the conversation view is changed
    When I navigate back from dialog page
    And I tap on my avatar
    And I see personal info page
    And I tap Light Bulb button
    And I close Personal Info Page
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see the conversation view is not changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |