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
    And I see Conversations list with conversations
    And I tap on conversation name <Contact1>
    # --- Add Picture ---
    When I tap Add picture button from cursor toolbar
    ## for camera
    And I dismiss security alert
    ## for gallery
    And I dismiss security alert
    Then I do not see extended cursor camera overlay
    # --- Record Video ---
    When I tap Video message button from cursor toolbar
    And I dismiss security alert
    And I dismiss security alert if it is visible
    Then I see conversation view
    # --- Record Audio ---
    When I long tap Audio message button from cursor toolbar
    And I dismiss security alert
    Then I see conversation view
    # --- Share Location ---
    When I tap Share location button from cursor toolbar
    And I dismiss security alert
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view
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
    And I select "Account" settings menu item
    And I select "Picture" settings menu item
    And I dismiss security alert
    Then I do not see Take Photo button on Take Picture view
    And I press Back button
    And I press Back button
    And I press Back button
    # --- Verify no user if visible in invites list if contacts access is denied
    And I open Search UI
    Then I do not see user <Contact3> in Contact list

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
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |