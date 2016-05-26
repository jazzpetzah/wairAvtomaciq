Feature: Audio Message

  @C131173 @staging
  Scenario Outline: Verify hint appears on voice icon tapping
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Audio message button from cursor toolbar
    Then I see hint message "<HintMessage>" of cursor button

    Examples:
      | Name      | Contact   | HintMessage                          |
      | user1Name | user2Name | Tap and hold to send a voice message |

  @C131179 @staging  @C131175 @C131176
  Scenario Outline: Verify sending voice message by long tap > swipe up
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    Then I see audio message is recording
    When I swipe up on audio message slide
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131180 @staging
  Scenario Outline: Verify sending voice message by long tap > release the humb > tap on the check ion
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap on audio message send button
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131188 @staging
  Scenario Outline: (Not-implemented, empty notification) Verify getting a chathead when voice message is sent in the other conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact2>
    When <Contact1> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Then I see new message notification "<Notification>"

    Examples:
      | Name      | Contact1  | Contact2  | FileName | MIMEType  | DeviceName | Notification         |
      | user1Name | user2Name | user3Name | test.m4a | audio/mp4 | Device1    | SENT A VOICE MESSAGE |