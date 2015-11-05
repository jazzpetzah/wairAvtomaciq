Feature: Self Profile

  @id205 @regression @rc @rc42
  Scenario Outline: Change user picture with gallery image
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
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

  @id325 @regression @rc
  Scenario Outline: Check contact personal info in one to one conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap conversation details button
    Then I see <Contact> user name and email

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id328 @regression @rc @rc42
  Scenario Outline: I can change my name
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with no contacts
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

  @id201 @regression @rc
  Scenario Outline: Change user picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
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

  @id4066 @staging
  Scenario Outline: Verify theme switch in self profile change its state and synced with settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    And I tap on my avatar
    And I see personal info page
    And I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    And I remember the value of "Theme" setting
    And I press back button
    And I press back button
    And I see personal info page
    When I tap Light Bulb button
    And I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    Then I verify the value of "Theme" setting is changed

    Examples:
      | Name      |
      | user1Name |