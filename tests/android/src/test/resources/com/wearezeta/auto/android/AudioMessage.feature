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

  @C131175 @staging @C131176
  Scenario Outline:  Verify message is started recording by long tapping on the icon
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button from cursor toolbar
    Then I see audio message is recording

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131179 @staging
  Scenario Outline: Verify sending voice message by long tap > swipe up
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button from cursor toolbar
    And I swipe up on audio message slide
    Then I see cursor toolbar

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131180 @staging
  Scenario Outline: Verify sending voice message by long tap > release the humb > tap on the check ion
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button from cursor toolbar
    And I tap on audio message send button
    Then I see cursor toolbar

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |