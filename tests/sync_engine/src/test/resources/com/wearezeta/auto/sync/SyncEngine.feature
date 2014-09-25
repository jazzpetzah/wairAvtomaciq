Feature: Sync Engine

  Scenario Outline: Sync Engine smoke test
    Given OSX I Sign in using login <OSXLogin> and password <OSXPassword>
    And OSX I open conversation with <ConversationName>
    And Android I Sign in using login <AndroidLogin> and password <AndroidPassword>
    And Android I open conversation with <ConversationName>
    And IOS I Sign in using login <IOSLogin> and password <IOSPassword>
    And IOS I open conversation with <ConversationName>
    And I run serial sync engine test
    And I collect messages order data
    Then I build results report
    And I perform acceptance checks

    Examples: 
      | OSXLogin | OSXPassword   | AndroidLogin | AndroidPassword | IOSLogin | IOSPassword   | ConversationName |
      | 1stUser  | usersPassword | 2ndUser      | usersPassword   | 3rdUser  | usersPassword | SyncEngineTest   |
