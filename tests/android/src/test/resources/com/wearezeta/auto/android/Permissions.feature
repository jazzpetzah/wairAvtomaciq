Feature: Permissions

  @C129781 @noAcceptAlert @v6Plus
  Scenario Outline: Deny permissions scenario
    Given I am on Android 6 or better
    Given I delete all contacts from Address Book
    Given There is 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I add <Contact3> into Address Book
    When I sign in using my phone number
    And I wait for 3 seconds
    Then I dismiss security alert if it is visible
    And I see Contact list with contacts
    And I tap on contact name <Contact1>
    # --- Add Picture ---
    When I tap Add picture button from cursor toolbar
    And I dismiss security alert
    And I do not see Take Photo button on Take Picture view
    And I tap Close button on Take Picture view
    Then I do not see any pictures in the conversation view
    # --- Record Video ---
    When I tap Video message button from cursor toolbar
    And I dismiss security alert
    And I dismiss security alert if it is visible
    Then I see conversation view
    # --- Record Audio ---
    When I long tap Audio message button from cursor toolbar
    And I dismiss security alert
    Then I see conversation view
    # --- Unfortunately, File Upload cannot be tested  ---
    # --- Audio call from toolbar ---
    When I tap Audio Call button from top toolbar
    And I dismiss security alert
    Then I do not see ongoing call
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    # --- Video call from toolbar ---
    When I tap Video Call button from top toolbar
    And I dismiss security alert
    And I dismiss security alert if it is visible
    Then I do not see ongoing video call
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    # --- Audio call from other user ---
    Given <Contact1> starts instance using <CallBackend>
    When <Contact1> calls me
    And I wait for 7 seconds
    And I dismiss security alert
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    Then I do not see incoming call
    And <Contact1> stops calling me
    # --- Video call from other user ---
    Given <Contact2> starts instance using <CallBackend2>
    When <Contact2> starts a video call to me
    And I wait for 7 seconds
    And I dismiss security alert
    And I dismiss security alert if it is visible
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    Then I do not see incoming call
    And <Contact2> stops calling me
    # --- Select Profile Picture ---
    When I navigate back from dialog page
    And I tap conversations list settings button
    And I tap on personal info screen
    And I dismiss security alert
    And I tap Change Photo button on Take Picture view
    And I dismiss security alert
    Then I do not see Take Photo button on Take Picture view
    And I tap Close button on Take Picture view
    # --- Verify no user if visible in invites list if contacts access is denied
    When I close Personal Info Page
    And I open Search UI
    And I do not see <Contact3> in the invites list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | user4Name | autocall    | chrome       |