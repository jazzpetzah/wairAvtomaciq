Feature: Localytics

#***************************************************
# Start of regFailed event
#***************************************************
  @localytics
  Scenario Outline: Verify 'regFailed:reason=The given e-mail address or phone number is in use.' stats
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
      | user1Name | user1Password | smoketester+123@gmail.com | regFailed:reason=Bad e-mail address                                  |
# FIXME: Are there any other 'reason' values to check ?

#***************************************************
# End of regFailed events
#***************************************************


#***************************************************
# Start of regAddedPicture event
#***************************************************

# TODO:
#   regAddedPicture:source=fromCarousel
#   regAddedPicture:source=fromCamera

  @localytics
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

#***************************************************
# End of regAddedPicture event
#***************************************************


#***************************************************
# Start of session event
#***************************************************

# TODO: session: \
#        "incomingCallsMutedActual",
#        "incomingCallsAcceptedActual",
#        "voiceCallsInitiatedActual",

  @torun @localytics
  Scenario Outline: Verify "session:connectRequestsSentActual=1" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    When I open People Picker from Contact List
    And I wait up to 15 seconds until <ContactEmail> exists in backend search results
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

  @torun @localytics
  Scenario Outline: Verify "session:totalOutgoingConnectionRequestsActual=1" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 1 users where <Name> is me
    Given I have sent connection request to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact    | AttrName                                        |
      | user1Email | user1Password | user1Name | user2Name  | session:totalOutgoingConnectionRequestsActual=1 |

  @torun @localytics
  Scenario Outline: Verify "session:totalIncomingConnectionRequestsActual=1" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to Me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact   | AttrName                                        |
      | user1Email | user1Password | user1Name | user2Name | session:totalIncomingConnectionRequestsActual=1 |

  @torun @localytics
  Scenario Outline: Verify "session:connectRequestsAcceptedActual=1" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given <Contact> has sent connection request to Me
    Given I Sign in using login <Login> and password <Password>
    And I see connection request
    And I open connection requests list
    And I accept connection request from user <Contact>
    And I see Contact list with name <Contact>
    And I wait for 65 seconds

  Examples:
      | Login      | Password      | Name      | Contact   | AttrName                                |
      | user1Email | user1Password | user1Name | user2Name | session:connectRequestsAcceptedActual=1 |

  @torun @localytics
  Scenario Outline: Verify "session:pingsSentActual=1" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <Contact>
    When I click ping button  
    Then I see ping message <PING>
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | AttrName                  |
      | user1Email | user1Password | user1Name | user2Name | pinged | session:pingsSentActual=1 |

  @torun @localytics
  Scenario Outline: Verify "session:totalContactsActual=3" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | AttrName                      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | session:totalContactsActual=3 |

  @torun @localytics
  Scenario Outline: Verify "session:textMessagesSentActual=5" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
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

  @torun @localytics
  Scenario Outline: Verify "session:totalGroupConversationsActual=2" stats
    Given I take snapshot of <AttrName> attribute count
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given Myself has group chat <ChatName2> with <Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I wait for 65 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | ChatName1  | ChatName2  | AttrName                                |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | GroupChat1 | GroupChat2 | session:totalGroupConversationsActual=2 |

#***************************************************
# End of session event
#***************************************************


#***************************************************
# Start of regSucceeded event
#***************************************************

  @torun @localytics
  Scenario Outline: Verify "regSucceeded" stats
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

# TODO: implement calling automation

#***************************************************
# End of voiceCallEnded event
#***************************************************


#***************************************************
# Common verification
#***************************************************

  @torun @localytics
  Scenario Outline: Verify count of each attribute is increased
    Then I verify the count of <AttrName> attribute has been increased within 600 seconds

    Examples:
      | AttrName                                                             |
      | regFailed:reason=The given e-mail address or phone number is in use. |
      | regFailed:reason=Bad e-mail address                                  |
      | regAddedPicture:source=fromPhotoLibrary                              |
      | session:connectRequestsSentActual=1                                  |
      | session:totalOutgoingConnectionRequestsActual=1                      |
      | session:totalIncomingConnectionRequestsActual=1                      |
      | session:connectRequestsAcceptedActual=1                              |
      | session:pingsSentActual=1                                            |
      | session:totalContactsActual=3                                        |
      | session:textMessagesSentActual=5                                     |
      | session:totalGroupConversationsActual=2                              |

  @torun @localytics
  Scenario Outline: Verify count of each event is increased
    Then I verify the count of <EventName> event has been increased within 600 seconds

    Examples:
      | EventName    |
      | regSucceeded |
