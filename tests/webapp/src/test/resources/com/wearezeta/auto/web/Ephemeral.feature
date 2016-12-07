Feature: Ephemeral

  @C261723 @ephemeral @regression @localytics @WEBAPP-3302
  Scenario Outline: Verify sending ephemeral text message in 1:1
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I verify the database is containing the message <Message> from <Name> in active conversation
    And I see timer next to the last message
    When I wait for <Wait> seconds
    Then I do not see timer next to the last message
    And I see the last message is obfuscated
    And I see 2 messages in conversation
    And I verify the database is not containing the message <Message> from <Name> in active conversation
    And I see 1 message in database from <Name> in active conversation
    And I see localytics event <Event> with attributes <Attributes>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password>
    And I am signed in properly
    And I open conversation with <Name>
    Then I see text message <Message>
    And I see timer next to the last message
    When I wait for <Wait> seconds
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    And I see 0 message in database from <Contact> in active conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    And I see 0 message in database from <Name> in active conversation

    Examples:
      | Login1     | Password      | Name      | Contact   | Login2     | Wait | Time | TimeLong   | TimeShortUnit | Message | Event                        | Attributes                                                                                                                  |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 5    | 5    | 5 seconds  | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":true,\"ephemeral_time\":5,\"with_bot\":false}"   |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 15   | 15   | 15 seconds | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":true,\"ephemeral_time\":15,\"with_bot\":false}"  |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 30   | 30   | 30 seconds | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":true,\"ephemeral_time\":30,\"with_bot\":false}"  |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 60   | 1    | 1 minute   | m             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":true,\"ephemeral_time\":60,\"with_bot\":false}"  |
     #| user1Email | user1Password | user1Name | user2Name | user2Email | 300  | 5    | 5 minutes  | m             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":true,\"ephemeral_time\":300,\"with_bot\":false}" |

  @C261728 @ephemeral @regression
  Scenario Outline: Verify switching on/off ephemeral message in 1:1
    Given There are 2 users where <Name> is me
    #Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message1>
    And I send message
    Then I see text message <Message1>
    When I wait for <Time> seconds
    And I do not see text message <Message1>
    And I click on ephemeral button
    And I set the timer for ephemeral to OFF
    And I see placeholder of conversation input is Type a message
    And I write message <Message2>
    And I send message
    Then I see text message <Message2>
    When I wait for <Time> seconds
    Then I see text message <Message2>
    And I see the last message is not obfuscated

    Examples:
      | Login1     | Password      | Name      | Contact   | Time | TimeLong  | TimeShortUnit | Message1 | Message2 |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds | s             | Hello1   | Hello2   |

  @C262533 @ephemeral @regression
  Scenario Outline: Verify that messages with previous timer are deleted on start-up when the timeout passed in 1:1
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    When I wait for <Time> seconds
    And I see the last message is obfuscated
    Then I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    When I open conversation with <Name>
    Then I see text message <Message>
    And I wait for <Time> seconds
    And I do not see text message <Message>
    And I see 1 message in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    When I open conversation with <Name>
    Then I do not see text message <Message>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Login2     | Name      | Contact   | TimeLong   | TimeShortUnit | Time  | Message |
      | user1Email | user1Password | user2Email | user1Name | user2Name | 15 seconds | s             | 15    | testing |

  @C262134 @ephemeral @regression
  Scenario Outline: Verify timer is applied to the all messages until turning it off in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    And I write message <Message1>
    And I send message
    And I wait for <ReducedTime> seconds
    And I write message <Message2>
    And I send message
    And I see text message <Message1>
    And I see text message <Message2>
    When I wait for <ReducedTime> seconds
    Then I see the second last message is obfuscated
    When I wait for <ReducedTime> seconds
    Then I see the last message is obfuscated
    When User <Contact> reads the second last message from user <Name> via device Device1
    And I wait for <Time> seconds
    Then I do not see text message <Message1>
    And I do not see text message <Message2>
    Then I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    Then I see 1 message in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | TimeLong   | Time | ReducedTime | Message1 | Message2 |
      | user1Email | user1Password | user1Name | user2Name | 15 seconds | 15   | 6           | testing1 | testing2 |

  @C264664 @ephemeral @regression
  Scenario Outline: Verify I can not edit my last ephemeral message by pressing the up arrow key in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    And I write message <OriginalMessage>
    Then I send message
    And I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When I press Up Arrow to edit message
# TODO implement step for "edit message input is not visible"
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I send message
    Then I see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 3 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | EditedMessage | Time | TimeLong   | TimeShortUnit |
      | user1Email | user1Password | user1Name | user2Name | edit me         | edited        | 30   | 30 seconds | s             |

  @C261733 @ephemeral @smoke
  Scenario Outline: Verify sending different types of ephemeral messages (ping, picture, video, audio, file) in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    Given user <Contact> adds a new device Device1 with label Label1
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
  #timer
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
  #ping
    When I click ping button
    Then I see <PING> action in conversation
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see the last message is obfuscated
    And I see 2 messages in conversation
  #Contact read the message (remote step)
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    Then I do not see ping action in conversation
    And I see 1 messages in conversation
  #picture
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    And I do not see any picture in the conversation view
    And I see 1 messages in conversation
  #video
    When I see file transfer button in conversation input
    When I send <SizeVideo> sized video with name <VideoFile> to the current conversation
    And I wait until video <VideoFile> is uploaded completely
    And I see video message <VideoFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I do not see video message <VideoFile> in the conversation view
    And I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    When I wait for <Time> seconds
    And I do not see video message <VideoFile> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
  #audio
    When I send audio file with length <AudioTime> and name <AudioFile> to the current conversation
    And I wait until audio <AudioFile> is uploaded completely
    Then I see audio message <AudioFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    And I do not see audio message <AudioFile> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
    #file
    When I send <SizeFile> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact> reads the recent message from user <Name> via device Device1
    And I wait for <Time> seconds
    And I do not see file transfer for file <File> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
    And I see 0 messages in database from <Name> in active conversation

    Examples:
      | Login1     | Password      | Name      | Contact   | Time | TimeLong   | TimeShortUnit | PING       | PictureName               | VideoFile   | SizeVideo | AudioFile   | AudioTime | File         | SizeFile | TypeFile |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | s             | you pinged | userpicture_landscape.jpg | C261733.mp4 | 1 MB      | example.wav | 00:20     | C261733.zip  | 512KB    | ZIP      |

  @C310631 @ephemeral @regression
  Scenario Outline: Verify sender can not download asset while it is obfuscated in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    When I wait until file <File> is uploaded completely
    And I wait for <Time> seconds
    And I see block replaces the last message in the conversation view
    And I click context menu of the last message
    And I do not see download button in context menu

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size | Time | TimeLong  | TimeShortUnit |
      | user1Email | user1Password | user1Name | user2Name | C310631.txt | 5MB  | 5    | 5 seconds | s             |

  @C262135 @ephemeral @regression
  Scenario Outline: Verify that missed call has stayed after receiver saw it in 1:1
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given user <Contact> adds a new device Device1 with label Label1
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    #Contact calls me
    When User <Contact> switches user Myself to ephemeral mode via device Device1 with <TimeLong> timeout
    And Contact <Contact> sends message "<Message1>" via device Device1 to user Myself
    And <Contact> calls me
    And I wait for 1 second
    And <Contact> stops calling me
    And Contact <Contact> sends message "<Message2>" via device Device1 to user Myself
    And I wait for 10 seconds
    Then I do not see text message "<Message1>"
    And I do not see text message "<Message2>"
    Then I see <ActionMessage> action in conversation
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | Message2 | TimeLong  | CallBackend | ActionMessage |
      | user1Email | user1Password | user1Name | user2Name | message1 | message2 | 5 seconds | zcall       | called        |

  @C261726 @ephemeral @regression
  Scenario Outline: Verify ephemeral messages are not sent to my other devices while sending in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email1> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    When I enter email "<Email1>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I see the history info page
    When I click confirm on history info page
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I see timer next to the last message
    And I see 2 messages in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see Sign In page
    And I Sign in using login <Email1> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I see 1 messages in conversation
    Then I do not see text message <Message>
    And I see 0 message in database from <Name> in active conversation

    Examples:
      | Email1     | Password      | Name      | Contact   | Time | TimeLong | TimeShortUnit | Message |
      | user1Email | user1Password | user1Name | user2Name | 1    | 1 day    | d             | Hello   |

  @C262537 @ephemeral @regression
  Scenario Outline: Verify I can receive ephemeral text message (5s/15s/1m/5m) timeout starts while being in the conversation in 1:1
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given user <Contact> adds a new device Device1 with label Label1
    Given User <Contact> switches user Myself to ephemeral mode via device Device1 with <TimeLong> timeout
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message "<Message>" via device Device1 to user Myself
    And I see text message <Message>
    And I see 2 messages in conversation
    And I see timer next to the last message
    And I wait for <Wait> seconds
    Then I do not see text message "<Message>"
    And I see 1 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Wait | TimeLong   | Message |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | Hello   |
      | user1Email | user1Password | user1Name | user2Name | 15   | 15 seconds | Hello   |
      | user1Email | user1Password | user1Name | user2Name | 30   | 30 seconds | Hello   |
      | user1Email | user1Password | user1Name | user2Name | 60   | 1 minute   | Hello   |
     #| user1Email | user1Password | user1Name | user2Name | 300  | 5 minutes  | Hello   |

  #GROUP
  @C318623 @ephemeral @staging @WEBAPP-3302 @localytics
  Scenario Outline: Verify sending ephemeral text message in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself have group chat <ChatName> with <Contact1>,<Contact2>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I verify the database is containing the message <Message> from <Name> in active conversation
    And I see timer next to the last message
    When I wait for <Wait> seconds
    Then I do not see timer next to the last message
    And I see the last message is obfuscated
    And I see 2 messages in conversation
    And I verify the database is not containing the message <Message> from <Name> in active conversation
    And I see 1 message in database from <Name> in active conversation
    And I see localytics event <Event> with attributes <Attributes>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see text message <Message>
    And I see timer next to the last message
    When I wait for <Wait> seconds
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    And I see 0 message in database from <Contact1> in active conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    And I see 0 message in database from <Name> in active conversation

     Examples:
       | Login1     | Password      | Name      | Contact1  | Contact2  | ChatName | Login2     | Wait | Time | TimeLong   | TimeShortUnit | Message | Event                        | Attributes                                                                                                                  |
       | user1Email | user1Password | user1Name | user2Name | user3Name | groupEph | user2Email | 5    | 5    | 5 seconds  | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"group\",\"is_ephemeral\":true,\"ephemeral_time\":5,\"with_bot\":false}"   |
#       | user1Email | user1Password | user1Name | user2Name | user3Name | groupEph | user2Email | 15   | 15   | 15 seconds | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"group\",\"is_ephemeral\":true,\"ephemeral_time\":15,\"with_bot\":false}"  |
#       | user1Email | user1Password | user1Name | user2Name | user3Name | groupEph | user2Email | 30   | 30   | 30 seconds | s             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"group\",\"is_ephemeral\":true,\"ephemeral_time\":30,\"with_bot\":false}"  |
#       | user1Email | user1Password | user1Name | user2Name | user3Name | groupEph | user2Email | 60   | 1    | 1 minute   | m             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"group\",\"is_ephemeral\":true,\"ephemeral_time\":60,\"with_bot\":false}"  |
#       | user1Email | user1Password | user1Name | user2Name | user3Name | groupEph | user2Email | 300  | 5    | 5 minutes  | m             | Hello   | media.completed_media_action | {\"action\":\"text\",\"conversation_type\":\"group\",\"is_ephemeral\":true,\"ephemeral_time\":300,\"with_bot\":false}" |

  @C318625 @ephemeral @smoke
  Scenario Outline: Verify switching on/off ephemeral message in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself have group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message1>
    And I send message
    Then I see text message <Message1>
    When I wait for <Time> seconds
    And I do not see text message <Message1>
    And I click on ephemeral button
    And I set the timer for ephemeral to OFF
    And I see placeholder of conversation input is Type a message
    And I write message <Message2>
    And I send message
    Then I see text message <Message2>
    When I wait for <Time> seconds
    Then I see text message <Message2>
    And I see the last message is not obfuscated

     Examples:
       | Login1     | Password      | Name      | Contact1  | Contact2  | ChatName | Time | TimeLong  | TimeShortUnit | Message1 | Message2 |
       | user1Email | user1Password | user1Name | user2Name | user3Name | ephGroup | 5    | 5 seconds | s             | Hello1   | Hello2   |

  @C318628 @ephemeral @regression
  Scenario Outline: Verify that missed call has stayed after receiver saw it in group
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself have group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
  #Contact calls me
    When User <Contact1> switches group conversation <ChatName> to ephemeral mode via device Device1 with <TimeLong> timeout
    And Contact <Contact1> sends message "<Message1>" via device Device1 to group conversation <ChatName>
    And <Contact1> calls <ChatName>
    And I wait for 1 second
    And <Contact1> stops calling <ChatName>
    And Contact <Contact1> sends message "<Message2>" via device Device1 to group conversation <ChatName>
    And I wait for <Time> seconds
    Then I do not see text message "<Message1>"
    And I do not see text message "<Message2>"
    Then I see <ActionMessage> action in conversation
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Message1 | Message2 | TimeLong  | CallBackend | ActionMessage | Time |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ephGroup | message1 | message2 | 5 seconds | zcall       | called        | 5    |

  @C318624 @ephemeral @regression
  Scenario Outline: Verify ephemeral messages in group are not sent to my other devices
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself have group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email1> and password <Password>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    Given I see Sign In page
    When I enter email "<Email1>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I see the history info page
    When I click confirm on history info page
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I see timer next to the last message
    And I see 2 messages in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see Sign In page
    And I Sign in using login <Email1> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    And I see 1 messages in conversation
    Then I do not see text message <Message>
    And I see 0 message in database from <Name> in active conversation

    Examples:
      | Email1     | Password      | Name      | Contact1  | Contact2  | ChatName | Time | TimeLong | TimeShortUnit | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ephGroup | 1    | 1 day    | d             | Hello   |

  @C318633 @ephemeral @regression
  Scenario Outline: Verify message is deleted on sender side when it's read by anyone in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself have group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Email1> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    And I write message <Message>
    And I send message
    Then I see text message <Message>
    And I wait for <Time> seconds
    And I see the last message is obfuscated
    When User <Contact1> reads the recent message from group conversation <ChatName> via device Device1
    And I wait for <Time> seconds
    And I see 1 messages in conversation

    Examples:
      | Email1     | Password      | Name      | Contact1  | Contact2  | ChatName | TimeLong  | Message | Time | TimeShortUnit |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ephGroup | 5 seconds | Hello   | 5    | s             |

  @C318626 @ephemeral @staging
  Scenario Outline: Verify sending different types of ephemeral messages (ping, picture, video, audio, file) in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    Given user <Contact1> adds a new device Device1 with label Label1
    And I am signed in properly
    When I open conversation with <ChatName>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
  #timer
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
  #ping
    When I click ping button
    Then I see <PING> action in conversation
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see the last message is obfuscated
    And I see 2 messages in conversation
  #Contact read the message (remote step)
    When User <Contact1> reads the recent message from group conversation <ChatName> via device Device1
    Then I do not see ping action in conversation
    And I see 1 messages in conversation
  #picture
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    And I see only 1 picture in the conversation
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact1> reads the recent message from user <Name> via device Device1
    And I do not see any picture in the conversation view
    And I see 1 messages in conversation
  #video
    When I see file transfer button in conversation input
    When I send <SizeVideo> sized video with name <VideoFile> to the current conversation
    And I wait until video <VideoFile> is uploaded completely
    And I see video message <VideoFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I do not see video message <VideoFile> in the conversation view
    And I see 2 messages in conversation
    When User <Contact1> reads the recent message from user <Name> via device Device1
    When I wait for <Time> seconds
    And I do not see video message <VideoFile> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
  #audio
    When I send audio file with length <AudioTime> and name <AudioFile> to the current conversation
    And I wait until audio <AudioFile> is uploaded completely
    Then I see audio message <AudioFile> in the conversation view
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact1> reads the recent message from user <Name> via device Device1
    And I do not see audio message <AudioFile> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
  #file
    When I send <SizeFile> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I see block replaces the last message in the conversation view
    And I see 2 messages in conversation
    When User <Contact1> reads the recent message from user <Name> via device Device1
    And I do not see file transfer for file <File> in the conversation view
    And I do not see block replaces the last message in the conversation view
    And I see 1 messages in conversation
    And I see 0 messages in database from <Name> in active conversation

    Examples:
      | Login1     | Password      | Name      | Contact1  | Contact2  | ChatName | Time | TimeLong   | TimeShortUnit | PING       | PictureName               | VideoFile   | SizeVideo | AudioFile   | AudioTime | File         | SizeFile | TypeFile |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ephGroup | 5    | 5 seconds  | s             | you pinged | userpicture_landscape.jpg | C261733.mp4 | 1 MB      | example.wav | 00:20     | C261733.zip  | 512KB    | ZIP      |