Feature: Calling

  @regression @id2709 @id2623
  Scenario Outline: Verify starting and ending outgoing call by same person [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message
    When I end started call
    Then I dont see calling page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2709 @id2623
  Scenario Outline: Verify starting and ending outgoing call by same person [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message
    When I end started call
    Then I dont see calling page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @calling_basic @id2630
  Scenario Outline: Verify calling from missed call indicator in conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see missed call from contact <Contact>
    And I click missed call button to call contact <Contact>
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2630
  Scenario Outline: Verify calling from missed call indicator in conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see missed call from contact <Contact>
    And I click missed call button to call contact <Contact>
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2712
  Scenario Outline: Verify ignoring of incoming call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I ignore incoming call
    Then I dont see incoming call page

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2712
  Scenario Outline: Verify ignoring of incoming call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I ignore incoming call
    Then I dont see incoming call page

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2713
  Scenario Outline: Verify accepting incoming call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call buttons
    And I see started call message for contact <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2713
  Scenario Outline: Verify accepting incoming call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call buttons
    And I see started call message for contact <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2622
  Scenario Outline: Receiving missed call notification from one user [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see missed call from contact <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2622
  Scenario Outline: Receiving missed call notification from one user [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And I tap on contact name <Contact>
    And I see dialog page
    Then I see missed call from contact <Contact>

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @calling_basic @id2619
  Scenario Outline: In zeta call for more than 15 mins [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I wait for 900 seconds
    And I see mute call, end call buttons
    And I end started call
    And I dont see calling page
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2619
  Scenario Outline: In zeta call for more than 15 mins [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I wait for 900 seconds
    And I see mute call, end call buttons
    And I end started call
    And I dont see calling page
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2631
  Scenario Outline: Screenlock device when in the call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I lock screen for 5 seconds
    And I see mute call, end call buttons
    And I end started call
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2631
  Scenario Outline: Screenlock device when in the call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I lock screen for 5 seconds
    And I see mute call, end call buttons
    And I end started call
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2652
  Scenario Outline: 3rd person tries to call me after I initate a call to somebody [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Contact1> starts waiting instance using <CallBackend>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact1>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact2> calls me using <CallBackend2>
    And I dont see incoming calling message from contact <Contact2>
    And <Contact1> accepts next incoming call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see mute call, end call buttons
    And I end started call
    Then I see missed call from contact <Contact2>
    And I swipe right on Dialog page
    And I see missed call indicator in list for contact <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | user3Name | webdriver   | autocall     | 120     |

  @calling_basic @id2652
  Scenario Outline: 3rd person tries to call me after I initate a call to somebody [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Contact1> starts waiting instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact1>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact2> calls me using <CallBackend2>
    And I dont see incoming calling message from contact <Contact2>
    And <Contact1> accepts next incoming call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see mute call, end call buttons
    And I end started call
    Then I see missed call from contact <Contact2>
    And I swipe right on Dialog page
    And I see missed call indicator in list for contact <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | user3Name | webdriver   | autocall     | 120     |

  @calling_basic @id2618
  Scenario Outline: Put app into background after initiating call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I close the app for 5 seconds
    And I see mute call, end call buttons
    And I end started call
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2618
  Scenario Outline: Put app into background after initiating call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I close the app for 5 seconds
    And I see mute call, end call buttons
    And I end started call
    And <Contact> stops all waiting instances

    Examples: 
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | webdriver   | 120     |

  @calling_basic @id2627
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button) [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call buttons
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples: 
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | webdriver   | autocall     | 120     |

  @calling_basic @id2627
  Scenario Outline: I want to accept a call through the incoming voice dialogue (Button) [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    Then I see mute call, end call buttons
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples: 
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | webdriver   | autocall     | 120     |

  @calling_basic @id2624
  Scenario Outline: I want to end the call from the ongoing voice overlay [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I end started call
    Then I dont see calling page
    And <Contact> verifies that waiting instance status is changed to ready in <Timeout> seconds
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    And I see mute call, end call buttons
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> stops all calls to me
    And I dont see calling page
    And <Contact> verifies that call status to me is changed to inactive in <Timeout> seconds

    Examples: 
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | webdriver   | autocall     | 120     |

  @calling_basic @id2624
  Scenario Outline: I want to end the call from the ongoing voice overlay [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I end started call
    Then I dont see calling page
    And <Contact> verifies that waiting instance status is changed to ready in <Timeout> seconds
    And <Contact> calls me using <CallBackend2>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    And I see mute call, end call buttons
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> stops all calls to me
    And I dont see calling page
    And <Contact> verifies that call status to me is changed to inactive in <Timeout> seconds

    Examples: 
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | webdriver   | autocall     | 120     |

  @staging @id2361
  Scenario Outline: Verify mute button is absent when you turn from portrait to landscape [PORTRAIT to LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press call button
    And I see mute call, end call buttons
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I swipe right on Dialog page
    Then I see mute call button in conversation list
    And I rotate UI to landscape
    Then I dont see mute call button in conversation list on iPad

    Examples: 
      | Name      | Contact   | CallBackend | CallBackend2 | Timeout |
      | user1Name | user2Name | chrome      | autocall     | 60      |
