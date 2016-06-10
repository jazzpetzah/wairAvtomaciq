Feature: Permissions

  @C129781 @noAcceptAlert @permissionsTest
  Scenario Outline: Deny permissions scenario
    Given I am on Android 6 or better
    Given I delete all contacts from Address Book
    Given There is 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I add <Contact3> into Address Book
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact2> starts instance using <CallBackend2>
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
    When <Contact1> calls me
    And I wait for 7 seconds
    And I swipe to accept the call
    And I dismiss security alert
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    Then I do not see incoming call
    And <Contact1> stops calling me
    # --- Video call from other user ---
    When <Contact2> starts a video call to me
    And I wait for 7 seconds
    And I swipe to accept the call
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
    Then I do not see <Contact3> in the invites list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | CallBackend | CallBackend2 |
      | user1Name | user2Name | user3Name | user4Name | autocall    | chrome       |

  @C136785 @noAcceptAlert @permissionsTest
  Scenario Outline: Verify you can successfully register a new user and log in automatically denying all permission requests
    Given I see welcome screen
    When I input a new phone number for user <Name>
    And I input the verification code
    And I input my name
    # deny access to contacts
    And I dismiss security alert
    And I select to choose my own picture
    And I select Camera as picture source
    # deny access to camera
    And I dismiss security alert
    # Workaround for AN-4119
    And I press Back button
    And I select to keep the current picture
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C136786 @noAcceptAlert @permissionsTest @useSpecialEmail
  Scenario Outline: Verify you can successfully log in and add email by denying all the permission requests
    Given There is 1 user with phone number only where <Name> is me
    Given I see welcome screen
    When I sign in using my phone number
    # deny access to contacts
    # Workaround an issue when login screen covers security alert
    And I dismiss security alert if it is visible
    And I have entered login <Login>
    And I have entered password <Password>
    And I start listening for confirmation email
    And I press Log in button
    And I verify my email
    Then I see Contact list with no contacts

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |