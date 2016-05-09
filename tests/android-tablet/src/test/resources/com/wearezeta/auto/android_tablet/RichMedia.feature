Feature: Rich Media

  @C759 @id2830 @regression @rc
  Scenario Outline: Send GIF format pic (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    And I tap the conversation <Contact>
    When I scroll to the bottom of the conversation view
    Then I see a new picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap the new picture in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @C796 @id3141 @regression @rc
  Scenario Outline: Send GIF format pic (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    And I tap the conversation <Contact>
    When I scroll to the bottom of the conversation view
    Then I see a new picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap the new picture in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @C774 @id2884 @regression @rc
  Scenario Outline: Verify you can play/pause media from the conversation list (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I remember the coordinates of conversation item <Contact>
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I swipe right to show the conversations list
    Then I see Pause button next to the conversation name <Contact>
    When I remember the state of Pause button next to the conversation name <Contact>
    And I tap Pause button next to the conversation name <Contact>
    Then I see the state of Pause button next to the conversation name <Contact> is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                             |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @C798 @id3144 @regression @rc
  Scenario Outline: (AN-3300) Verify you can play/pause media from the conversation list (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I remember the coordinates of conversation item <Contact>
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    Then I see Pause button next to the conversation name <Contact>
    When I remember the state of Pause button next to the conversation name <Contact>
    And I tap Pause button next to the conversation name <Contact>
    Then I see the state of Pause button next to the conversation name <Contact> is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                             |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @C788 @id2991 @regression @rc
  Scenario Outline: I can send giphy image by typing some massage and clicking GIF button (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap on text input
    Then I do not see Giphy button in the conversation view
    When I type the message "<Message>" in the conversation view
    Then I see Giphy button in the conversation view
    When I tap Giphy button in the conversation view
    Then I see Giphy preview page
    When I tap Send button on the Giphy preview page
    Then I see the conversation view
    And I see a new picture in the conversation view
    And I scroll to the bottom of the Conversation view
    # And I see the picture in the conversation view is animated

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | H       |

  @C797 @id3142 @regression @rc
  Scenario Outline: I can send giphy image by typing some massage and clicking GIF button (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap on text input
    Then I do not see Giphy button in the conversation view
    When I type the message "<Message>" in the conversation view
    Then I see Giphy button in the conversation view
    When I tap Giphy button in the conversation view
    Then I see Giphy preview page
    When I tap Send button on the Giphy preview page
    Then I see the conversation view
    And I see a new picture in the conversation view
    And I scroll to the bottom of the Conversation view
    # And I see the picture in the conversation view is animated

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | H       |

  @C502 @id2880 @regression
  Scenario Outline: Verify you can play/pause media from the Media Bar in conversation view (portrait only)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    # This is to be able to scroll up until Media Bar appears
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact> sends <MsgsInConvo> encrypted messages to user Myself
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I remember the state of media button in the conversation view
    And I scroll up until Media Bar is visible in the conversation view
    When I tap Pause button on Media Bar in the conversation view
    And I scroll to the bottom of the conversation view
    Then I see the state of media button in the conversation view is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                             | MsgsInConvo |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll | 40          |