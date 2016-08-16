Feature: Audio Messaging

  @C145953 @rc @regression @fastLogin
  Scenario Outline: Verify recording and sending an audio message [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button for <Duration> seconds from input tools
    And I tap Send record control button
    Then I see audio message container in the conversation view
    When I tap Play audio message button
    Then I see state of button on audio message placeholder is pause
    # TODO: Should be uncommented once ZIOS-6798 is fixed
    #And I see the audio message in placeholder gets played

    Examples:
      | Name      | Contact   | Duration |
      | user1Name | user2Name | 15       |

  @C145954 @rc @regression @fastLogin
  Scenario Outline: Verify receiving and playing an audio message [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    And I tap on contact name <Contact>
    And User <Contact> sends 1 encrypted message to user Myself
    # Wait until the media is loaded
    And I wait for 5 seconds
    And I see state of button on audio message placeholder is play
    And I tap Play audio message button
    #This step waits until state is changed, no need for download sleep anymore
    Then I see state of button on audio message placeholder is pause
    # TODO: Should be uncommented once ZIOS-6798 is fixed
    #And I see the audio messag

    Examples:
      | Name      | Contact   | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

