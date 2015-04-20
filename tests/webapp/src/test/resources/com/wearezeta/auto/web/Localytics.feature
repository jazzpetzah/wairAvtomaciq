Feature: Localytics

#***************************************************
# Start of regFailed event
#***************************************************
  @localytics @id2155
  Scenario Outline: Verify 'regFailed' stats
    Given I take snapshot of <AttrName> attribute count
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password <Password> on Registration page 
    And I submit registration form
    And I wait for 5 seconds

    Examples: 
      | Name      | Password      | Email                     | AttrName                                                             |
      | user1Name | user1Password | smoketester@wearezeta.com | regFailed:reason=The given e-mail address or phone number is in use. |
      | user1Name | user1Password | smoketester+123@gmail.com | regFailed:reason=Unauthorized e-mail address or phone number.        |
# FIXME: Are there any other 'reason' values to check ?

#***************************************************
# End of regFailed events
#***************************************************


#***************************************************
# Start of regAddedPicture event
#***************************************************

# TODO:
#   regAddedPicture:source=fromCamera -> probably, not possible

  @localytics @id2156
  Scenario Outline: Verify 'regAddedPicture:source=fromPhotoLibrary' stats
    Given I take snapshot of <AttrName> attribute count
    Given There is 1 user where <Name> is me without avatar picture
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I choose <PictureName> as my self picture on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I wait for 5 seconds

    Examples:
      | Login      | Password      | Name      | PictureName               | AttrName                                |
      | user1Email | user1Password | user1Name | userpicture_landscape.jpg | regAddedPicture:source=fromPhotoLibrary |

  @localytics @id2157
  Scenario Outline: Verify 'regAddedPicture:source=fromCarousel' stats
    Given I take snapshot of <AttrName> attribute count
    Given There is 1 user where <Name> is me without avatar picture
    And I Sign in using login <Login> and password <Password>
    And I see Self Picture Upload dialog
    And I force carousel mode on Self Picture Upload dialog
    And I select random picture from carousel on Self Picture Upload dialog
    And I confirm picture selection on Self Picture Upload dialog
    And I wait for 5 seconds

    Examples:
      | Login      | Password      | Name      | AttrName                            |
      | user1Email | user1Password | user1Name | regAddedPicture:source=fromCarousel |

#***************************************************
# End of regAddedPicture event
#***************************************************


#***************************************************
# Start of session event
#***************************************************

  @localytics @id2159
  Scenario Outline: Verify 'session:connectRequestsSentActual=1' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait up to 15 seconds until <ContactEmail> exists in backend search results
    When I open People Picker from Contact List
    And I type <ContactEmail> in search field of People Picker
    And I see user <Contact> found in People Picker
    And I click on not connected user <Contact> found in People Picker
    And I see Connect To popover
    And I click Connect button on Connect To popover
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact   | ContactEmail | AttrName                            |
      | user1Email | user1Password | user1Name | user2Name | user2Email   | session:connectRequestsSentActual=1 |

  @localytics @id2160
  Scenario Outline: Verify 'session:totalOutgoingConnectionRequestsActual=1' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given I have sent connection request to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact    | AttrName                                        |
      | user1Email | user1Password | user1Name | user2Name  | session:totalOutgoingConnectionRequestsActual=1 |

  @localytics @id2161
  Scenario Outline: Verify 'session:totalIncomingConnectionRequestsActual=1' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to Me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I see connection request from one user
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact   | AttrName                                        |
      | user1Email | user1Password | user1Name | user2Name | session:totalIncomingConnectionRequestsActual=1 |

  @localytics @id2170
  Scenario Outline: Verify 'session:connectRequestsAcceptedActual=1' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to Me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I see connection request from one user
    And I open the list of incoming connection requests
    And I accept connection request from user <Contact>
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact   | AttrName                                |
      | user1Email | user1Password | user1Name | user2Name | session:connectRequestsAcceptedActual=1 |

  @localytics @id2162
  Scenario Outline: Verify 'session:pingsSentActual=1' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I click ping button  
    Then I see ping message <PING>
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | AttrName                  |
      | user1Email | user1Password | user1Name | user2Name | pinged | session:pingsSentActual=1 |

  @localytics @id2163
  Scenario Outline: Verify 'session:totalContactsActual=3' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | AttrName                      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | session:totalContactsActual=3 |

  @localytics @id2164
  Scenario Outline: Verify 'session:textMessagesSentActual=5' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I write random message
    And I send message
    Then I see random message in conversation
    When I write random message
    And I send message
    Then I see random message in conversation
    When I write random message
    And I send message
    Then I see random message in conversation
    When I write random message
    And I send message
    Then I see random message in conversation
    When I write random message
    And I send message
    Then I see random message in conversation
    And I wait for 65 seconds

    Examples:
      | Login      | Password      | Name      | Contact   | AttrName                         |
      | user1Email | user1Password | user1Name | user2Name | session:textMessagesSentActual=5 |

  @localytics @id2165
  Scenario Outline: Verify 'session:totalGroupConversationsActual=2' stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given Myself has group chat <ChatName2> with <Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | ChatName1  | ChatName2  | AttrName                                |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupChat1 | GroupChat2 | session:totalGroupConversationsActual=2 |

  @localytics @id2167
  Scenario Outline: Verify the 'session:incomingCallsMutedActual=1' attribute is sent
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then I see the calling bar
    And I silence the incoming call
    Then I do not see the calling bar
    And <Contact> stops all calls to me
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @localytics @id2168
  Scenario Outline: Verify the 'session:incomingCallsAcceptedActual=1' attribute is sent
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When <Contact> calls me using <CallBackend>
    Then I see the calling bar
    And I accept the incoming call
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

  @localytics @id2169
  Scenario Outline: Verify the 'session:voiceCallsInitiatedActual=1' attribute is sent
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Me
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open conversation with <Contact>
    And I call
    Then I see the calling bar
    # It is not mandatory for the other side to pick up the call
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |

#***************************************************
# End of session event
#***************************************************


#***************************************************
# Start of regSucceeded event
#***************************************************

  @localytics @id2166
  Scenario Outline: Verify 'regSucceeded' stats
    Given I take snapshot of <EventName> event count
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page
    And I enter user email <Email> on Registration page
    And I enter user password <Password> on Registration page
    And I submit registration form
    And I wait for 5 seconds

    Examples: 
      | Email      | Password      | Name      | EventName    |
      | user1Email | user1Password | user1Name | regSucceeded |

#***************************************************
# End of regSucceeded event
#***************************************************


#***************************************************
# Start of voiceCallEnded event
#***************************************************

  @localytics @id2267
  Scenario Outline: Verify the 'voiceCallEnded' stats
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Me
    Given <Contact> starts waiting instance using <OutCallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When I open conversation with <Contact>
    And I call
    When <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    Then I end the call
    When <Contact> calls me using <InCallBackend>
    And I see the calling bar
    And I accept the incoming call
    Then I end the call
    And I wait for 5 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | OutCallBackend | InCallBackend  | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver      | autocall       | 120     |

#***************************************************
# End of voiceCallEnded event
#***************************************************


#***************************************************
# Common verification
#***************************************************

  @localytics
  Scenario Outline: Verify count of each attribute is increased
    Then I verify the count of <AttrName> attribute has been increased within 400 seconds

    Examples:
      | AttrName                                                             |
      | regFailed:reason=The given e-mail address or phone number is in use. |
      | regFailed:reason=Unauthorized e-mail address or phone number.        |
      | regAddedPicture:source=fromPhotoLibrary                              |
      | regAddedPicture:source=fromCarousel                                  |
      | session:connectRequestsSentActual=1                                  |
      | session:totalOutgoingConnectionRequestsActual=1                      |
      | session:totalIncomingConnectionRequestsActual=1                      |
      | session:connectRequestsAcceptedActual=1                              |
      | session:pingsSentActual=1                                            |
      | session:totalContactsActual=3                                        |
      | session:textMessagesSentActual=5                                     |
      | session:totalGroupConversationsActual=2                              |
      | session:incomingCallsMutedActual=1                                   |
      | session:incomingCallsAcceptedActual=1                                |
      | session:voiceCallsInitiatedActual=1                                  |

  @localytics
  Scenario Outline: Verify count of each event is increased
    Then I verify the count of <EventName> event has been increased within 400 seconds

    Examples:
      | EventName      |
      | regSucceeded   |
      | voiceCallEnded |
