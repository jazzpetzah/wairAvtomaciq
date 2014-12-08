Feature: Conversation

  @smoke @id466
  Scenario Outline: Send message to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id467
  Scenario Outline: Ping conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I ping user
    Then I see message YOU PINGED in conversation
    And I ping again user
    Then I see message YOU PINGED AGAIN in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id468
  Scenario Outline: Send picture to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I send picture testing.jpg
    Then I see picture in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 |

  @regression @id444
  Scenario Outline: Send HD picture to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    When I send picture hdpicture.jpg
    Then I see HD picture hdpicture.jpg in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @smoke @id470
  Scenario Outline: Send message to group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I write random message
    And I send message
    Then I see random message in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName             |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SendMessageGroupChat |

  @regression @1408
  Scenario Outline: Send picture to group chat
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I send picture testing.jpg
    Then I see picture in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName             |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SendPictureGroupChat |

  @regression @id81
  Scenario Outline: Play embedded SoundCloud link in conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I post media link <SoundCloudLink>
    And I send message
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap SoundCloud link
    Then I see the embedded media is playing

    Examples: 
      | Login   | Password    | Name    | Contact     | SoundCloudLink                               |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/djsliinkbbc/2-test-me |

  @staging @id379
  Scenario Outline: Play/pause SoundCloud media link from the media bar
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I post messages and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap SoundCloud link
    Then I see the embedded media is playing
    And I scroll media out of sight till media bar appears
    And I pause playing media in media bar
    Then The playing media is paused
    And I press play in media bar
    Then The media is playing
    And I stop media in media bar
    Then The media stops playing

    Examples: 
      | Login   | Password    | Name    | Contact     | SoundCloudLink                               |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/djsliinkbbc/2-test-me |

  @regression @id380
  Scenario Outline: Conversation scrolls back to playing media when clicked on the media bar
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I post messages and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap SoundCloud link
    Then I see the embedded media is playing
    And I scroll media out of sight till media bar appears
    And I press the media bar title
    Then I see media link <SoundCloudLink> and media in dialog
    Then I see the embedded media is playing

    Examples: 
      | Login   | Password    | Name    | Contact     | SoundCloudLink                               |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/djsliinkbbc/2-test-me |

  @regression @id624
  Scenario Outline: Text message sent to group chat is visible on other end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I write random message
    And I send message
    And I see random message in conversation
    When I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    And I open conversation with <ChatName>
    Then I see random message in conversation
    And I am signing out
    And I Sign in using login <Contact2> and password <Password>
    And I see my name <Contact2> in Contact list
    And I open conversation with <ChatName>
    Then I see random message in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName          |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SecondEndTextChat |

  @regression @id623
  Scenario Outline: Image sent to group chat is visible on other end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And I send picture testing.jpg
    And I see picture in conversation
    When I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    And I open conversation with <ChatName>
    Then I see picture in conversation
    And I am signing out
    And I Sign in using login <Contact2> and password <Password>
    And I see my name <Contact2> in Contact list
    And I open conversation with <ChatName>
    Then I see picture in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName           |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SecondEndImageChat |

  @regression @id625
  Scenario Outline: Multimedia message sent to group chat is visible on other end
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I post media link <SoundCloudLink>
    And I see media link <SoundCloudLink> and media in dialog
    And I am signing out
    And I Sign in using login <Contact1> and password <Password>
    And I see my name <Contact1> in Contact list
    And I open conversation with <ChatName>
    Then I see media link <SoundCloudLink> and media in dialog
    And I am signing out
    And I Sign in using login <Contact2> and password <Password>
    And I see my name <Contact2> in Contact list
    And I open conversation with <ChatName>
    Then I see media link <SoundCloudLink> and media in dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | SoundCloudLink                               | ChatName        |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | https://soundcloud.com/djsliinkbbc/2-test-me | SecondEndMMChat |

  @regression @id381
  Scenario Outline: The media bar disappears after playback finishes
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I post messages and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap SoundCloud link
    Then I see the embedded media is playing
    And I scroll media out of sight till media bar appears
    And I wait <time> seconds till playback finishes
    Then I see media bar disappears

    Examples: 
      | Login   | Password    | Name    | Contact     | SoundCloudLink                             | time |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/20sekunder/erika-no | 30   |

  @regression @id378
  Scenario Outline: Media bar disappears when playing media is back in view
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I post messages and media link <SoundCloudLink>
    Then I see media link <SoundCloudLink> and media in dialog
    And I tap SoundCloud link
    Then I see the embedded media is playing
    And I scroll media out of sight till media bar appears
    And I press the media bar title
    Then I see media link <SoundCloudLink> and media in dialog
    Then I see the embedded media is playing
    Then I see media bar disappears

    Examples: 
      | Login   | Password    | Name    | Contact     | SoundCloudLink                               |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | https://soundcloud.com/djsliinkbbc/2-test-me |

  @staging
  Scenario Outline: Drag image to conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <Contact>
    And I count number of images in conversation
    When I open Documents folder in Finder
    And I drag picture testing.jpg to conversation
    Then I see picture in conversation

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
